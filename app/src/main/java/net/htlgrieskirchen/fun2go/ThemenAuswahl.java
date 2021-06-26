package net.htlgrieskirchen.fun2go;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ThemenAuswahl extends AppCompatActivity {
    String thema = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themenauswahl);
        
        Button laender = findViewById(R.id.umgebung);
        Button allgemein = findViewById(R.id.allgemein);
        Button mensch = findViewById(R.id.mensch);
        Button essen = findViewById(R.id.essen);
        Button film_serie = findViewById(R.id.film_serie);
        Button tiere = findViewById(R.id.tiere);
        Button personen = findViewById(R.id.personen);
        Button technik = findViewById(R.id.technik);
        Button zufall = findViewById(R.id.zufall);
        Context ctx = this;
        
        laender.setOnClickListener(v -> {
            thema = "Laender";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        allgemein.setOnClickListener(v -> {
            thema = "Allgemein";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        mensch.setOnClickListener(v -> {
            thema = "Mensch";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        essen.setOnClickListener(v -> {
            thema = "Essen";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        film_serie.setOnClickListener(v -> {
            thema = "FileSerien";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        tiere.setOnClickListener(v -> {
            thema = "Tiere";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        personen.setOnClickListener(v -> {
            thema = "BeruehmtePersonen";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        technik.setOnClickListener(v -> {
            thema = "Technik";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
        
        zufall.setOnClickListener(v -> {
            thema = "zufall";
            startActivity(new Intent(ctx, ShowFact.class).putExtra("thema", thema));
        });
    }
}
