package net.htlgrieskirchen.fun2go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShowFact sf = new ShowFact("Länder");
    }
}
