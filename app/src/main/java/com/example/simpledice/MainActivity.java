package com.example.simpledice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoToBmi = findViewById(R.id.btnGoToBmi);
        Button btnGoToDice = findViewById(R.id.btnGoToDice);

        btnGoToBmi.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BmiActivity.class));
        });

        btnGoToDice.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PlayerSelectActivity.class));
        });
    }
}