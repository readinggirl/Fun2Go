package net.htlgrieskirchen.fun2go;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    protected static final String CHANNEL_ID = "12345";
    protected static final int NOTIFICATION_ID_STANDARD = 1;
    String oldCountry;
    private HashMap<String, String> laenderMap;
    private static final int RQ_ACCESS_FINE_LOCATION = 123;
    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private boolean isGpsAllowed = false;
    private LocationListener locationListener;
    double longitude;
    double latitude;
    private Location currentLocation;
    Context ctx = this;
    private final static int RQ_PREFERENCES = 1;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public static void preferenceChanged(SharedPreferences sharedPreferences, String key, View view) {
        if (key.equals("darkmode") && view != null) {
            if (sharedPreferences.getBoolean(key, false)) {
                view.setBackgroundResource(R.drawable.fun2go_dark);
            } else {
                view.setBackgroundResource(R.drawable.fun2go_light);
            }

        } else if (key.equals("allowNotifications")) {


        }

    }

    private void readLaenderFile() {
        laenderMap = new HashMap<>();
        String laenderFileContent = readAsset("Laender.txt");
        String[] laenderLines = laenderFileContent.split("\n");
        for (String line : laenderLines) {
            String[] splittedLine = line.split(":", 2);
            laenderMap.put(splittedLine[0], splittedLine[1]);
        }
    }

    private InputStream getInputStreamForAsset(String filename) {

        Log.d(TAG, "getInputStreamForAsset: " + filename);
        AssetManager assets = getAssets();
        try {
            return assets.open(filename);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private String readAsset(String fileName) {

        InputStream in = getInputStreamForAsset(fileName);
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder toReturn = new StringBuilder();
        try {
            for (int i = 0; (line = bin.readLine()) != null; i++) {
                /*
                String[] lineArray =line.split(":");
                String country_code= lineArray[0];
                */
                toReturn.append(line).append(i > 0 ? "\n" : "");
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return toReturn.toString();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyNotificationChannel";
            String description = "NotificationChannel for countries";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(View view) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, NotificationDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Notice that the Notification.Builder constructor requires a channel ID.
        // This is required for compatibility with Android 8.0 (API level 26) and higher,
        // but is ignored by older versions.
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.megaphone)
                .setColor(Color.rgb(245, 252, 255))
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Sie befinden sich in einem anderen Land")
                .setStyle(new Notification.BigTextStyle()
                        .bigText("Ein neuer FunFact zu ihrem Standort ist vergügbar!"))
                .setWhen(System.currentTimeMillis())
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID_STANDARD, builder.build());
    }

    private void checkPermissionGPS() {
        Log.d(TAG, "checkPermissionGPS");
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    RQ_ACCESS_FINE_LOCATION);
        } else {
            isGpsAllowed = true;
            gpsGranted();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button themen = findViewById(R.id.themen);
        Button zufall = findViewById(R.id.zufall);

        themen.setOnClickListener(v -> startActivity(new Intent(ctx, ThemenAuswahl.class)));

        zufall.setOnClickListener(v -> startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", "zufall")));


        readLaenderFile();
        createNotificationChannel();
        if (!isGpsAllowed) checkPermissionGPS();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        startLocationService();

        View background = findViewById(R.id.layout);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> preferenceChanged(sharedPrefs, key, null);
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void gpsGranted() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        String provider = locationManager.getBestProvider(criteria, false);
        Location location;
        try {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            Log.i("MainActivity", "lastKnownLocation- lon: " + longitude + "lat: " + latitude);
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("MainActivity", "onLocationChanged");
                currentLocation = location;
                Log.i(TAG, "currentLocation:" + currentLocation);
                sendGETRequest(currentLocation);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("MainActivity", "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.d("MainActivity", "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d("MainActivity", "onProviderDisabled");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 50, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != RQ_ACCESS_FINE_LOCATION) return;
        if (grantResults.length > 0 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission ACCESS_FINE_LOCATION denied!", Toast.LENGTH_LONG).show();
        } else {
            gpsGranted();
        }
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            //this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                MainActivity.this.startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.codingwithmitch.googledirectionstest.services.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity", "onResume");
        super.onResume();
        if (isGpsAllowed) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private void sendGETRequest(Location location) {
       /* URL url = null;
        try {
            url = new URL("http://example.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        double lat = location == null ? -1 : location.getLatitude();
        double lon = location == null ? -1 : location.getLongitude();

        String url = "https://eu1.locationiq.com/v1/reverse.php?key=pk.afe3e3d6e5d739a4dc3b934a555e3e90&lat=" + lat + "&lon=" + lon + "&format=json";
        new Thread(() -> {
            try {
                URL url1 = new URL(url);
                InputStream urlStream = url1.openStream();
                BufferedReader stream = new BufferedReader(new InputStreamReader(urlStream));
                String line;
                while ((line = stream.readLine()) != null) {
                    JSONObject countryObject = new JSONObject(line);
                    String country = countryObject.getJSONObject("address").getString("country_code");
                    Log.d(TAG, "Country: " + country);

                    if (!country.equals(oldCountry) && laenderMap.containsKey(country)) {
                        showNotification(null);
                    }
                    oldCountry = country;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RQ_PREFERENCES);
    }
}