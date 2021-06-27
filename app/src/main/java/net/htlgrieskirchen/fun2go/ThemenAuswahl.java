package net.htlgrieskirchen.fun2go;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class ThemenAuswahl extends AppCompatActivity {
    String thema = "";
    private final static int RQ_PREFERENCES = 1;
    Context ctx = this;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

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

        Button back = findViewById(R.id.backToMainMenu);

        back.setOnClickListener(v -> startActivity(new Intent(ctx, MainActivity.class)));


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
            thema = "FilmeSerien";
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

        View background = findViewById(R.id.relativeLayout);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> MainActivity.preferenceChanged(sharedPrefs, key, background, this);
        preferenceChangeListener.onSharedPreferenceChanged(prefs, "darkmode");
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RQ_PREFERENCES);
    }
}
