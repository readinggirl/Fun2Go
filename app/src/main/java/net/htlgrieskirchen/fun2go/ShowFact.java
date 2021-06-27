package net.htlgrieskirchen.fun2go;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowFact extends AppCompatActivity {
    
    private static final String TAG = "ShowFact";
    String thema;
    private final static int RQ_PREFERENCES = 1;
    Context ctx = this;
    private SharedPreferences prefs;

    List<String> facts;
    Button next;
    TextView tvThema;
    TextView tvFact;
    int lastNr = 0;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;


    public ShowFact() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_fact);
    
        Intent intent = getIntent();
        thema = intent.getStringExtra("thema");
        
        //readFile();
        
        next = findViewById(R.id.next);
        tvThema = findViewById(R.id.thema);
        tvFact = findViewById(R.id.funfact);
        tvFact.setMovementMethod(new ScrollingMovementMethod());
        
        switch (thema) {
            case "BeruehmtePersonen":
                tvThema.setText(R.string.beruehmtePersonen);
                break;
            case "FilmeSerien":
                tvThema.setText(R.string.filmeSerien);
                break;
            case "zufall":
                tvThema.setText(R.string.zufall);
                break;
            default: tvThema.setText(thema);
        }
        
        //if thema = zufall -> read methode für alle files abrufen...
        if ("zufall".equals(thema)) {
            readFileAll();
        } else {
            readFile(thema);
        }

        getRandom();
        tvFact.setText(facts.get(lastNr));
        
        next.setOnClickListener(v -> { //check if listlength < 1 then toast no other available
             getRandom();
            tvFact.setText(facts.get(lastNr));
        });

        Button backToThemenAuswahl = findViewById(R.id.backToThemenAuswahl);

        backToThemenAuswahl.setOnClickListener(v -> startActivity(new Intent(ctx, ThemenAuswahl.class)));

        View background = findViewById(R.id.showFactLayout);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> MainActivity.preferenceChanged(sharedPrefs, key, background, this);
        preferenceChangeListener.onSharedPreferenceChanged(prefs, "darkmode");
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
    
    private void readFileAll() {
        facts = new ArrayList<>();
        String fileContent = readAsset("Allgemein.txt");
        String[] lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("BeruehmtePersonen.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("Essen.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("FilmeSerien.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("Mensch.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("Technik.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);

        fileContent = readAsset("Tiere.txt");
        lines = fileContent.split(";");
        Collections.addAll(facts, lines);
    }

    private void readFile(String thema) {
        facts = new ArrayList<>();
        String fileContent = readAsset(thema + ".txt");
        String[] lines = fileContent.split(";");
        Collections.addAll(facts, lines);
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
                toReturn.append(line).append(i > 0 ? "\n" : "");
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return toReturn.toString();
    }
    
    private void getRandom() {
        int min = 0;
        int max = facts.size() - 1;
        
        int number = (int) (Math.random() * (max - min) + min);
        
        if (number != lastNr) {
            lastNr = number;
        } else getRandom();
    }


    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RQ_PREFERENCES);
    }
}
