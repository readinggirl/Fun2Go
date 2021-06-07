package net.htlgrieskirchen.fun2go;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

        laender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "laender";
                ShowFact sf = new ShowFact(thema);
            }
        });

        allgemein.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "allgemein";
                ShowFact sf = new ShowFact(thema);
            }
        });

        mensch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "mensch";
                ShowFact sf = new ShowFact(thema);
            }
        });

        essen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "essen";
                ShowFact sf = new ShowFact(thema);
            }
        });

        film_serie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "film_serie";
                ShowFact sf = new ShowFact(thema);
            }
        });

        tiere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "tiere";
                ShowFact sf = new ShowFact(thema);
            }
        });

        personen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "personen";
                ShowFact sf = new ShowFact(thema);
            }
        });

        technik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "technik";
                ShowFact sf = new ShowFact(thema);
            }
        });

        zufall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thema = "zufall";
                ShowFact sf = new ShowFact(thema);
            }
        });
    }
}
