package net.htlgrieskirchen.fun2go;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    List<String> facts;
    Button next;
    TextView tvThema;
    TextView tvFact;
    int lastNr = 0;
    
    //public ShowFact(String thema) {
    //    this.thema = thema;
    //}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_fact);
    
        Intent intent = getIntent();
        thema = intent.getStringExtra("thema");
        
        readFile();
        
        next = findViewById(R.id.next);
        tvThema = findViewById(R.id.thema);
        tvFact = findViewById(R.id.funfact);
        tvFact.setMovementMethod(new ScrollingMovementMethod());
        
        tvThema.setText(thema);
        
        //if thema = zufall -> read methode für alle files abrufen...
        
        tvFact.setText(facts.get(lastNr));
        
        next.setOnClickListener(v -> {
            Toast.makeText(this, "new fact", Toast.LENGTH_SHORT).show();
            getRandom();
            tvFact.setText(facts.get(lastNr));
        });
    }
    
    private void readFile() {
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
    
}
