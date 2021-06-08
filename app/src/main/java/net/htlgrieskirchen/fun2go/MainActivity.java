package net.htlgrieskirchen.fun2go;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int RQ_ACCESS_FINE_LOCATION = 123;
    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private boolean isGpsAllowed = false;
    private LocationListener locationListener;
    double longitude;
    double latitude;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (!isGpsAllowed) checkPermissionGPS();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        startLocationService();
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != RQ_ACCESS_FINE_LOCATION) return;
        if (grantResults.length > 0 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission ACCESS_FINE_LOCATION denied!", Toast.LENGTH_LONG).show();
        } else {
            //funfacts zu länder entfernen
        }
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
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
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
    
    private void sendGETRequest(){
        URL url = null;
        try {
            url = new URL("http://example.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}
    
