package net.htlgrieskirchen.fun2go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ShowFact extends AppCompatActivity {

    String thema;

    public ShowFact(String thema) {
        this.thema = thema;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_fact);
    }
    
    //get longitude and latitude
    
    
}
