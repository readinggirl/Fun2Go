package net.htlgrieskirchen.fun2go;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button themen = findViewById(R.id.themen);
        Button zufall = findViewById(R.id.zufall);

        themen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ThemenAuswahl themenAuswahl = new ThemenAuswahl();
            }
        });

        zufall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowFact showFact = new ShowFact("zufall");
            }
        });
    }
}