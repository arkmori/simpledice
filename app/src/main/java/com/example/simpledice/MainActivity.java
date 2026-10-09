package com.example.simpledice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButtonListenerMethod();
    }

    public void myButtonListenerMethod() {
        Button button = findViewById(R.id.rollButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int roll1 = rand.nextInt(6) + 1;
                int roll2 = rand.nextInt(6) + 1;
                int total = roll1 + roll2;

                TextView totalResult = findViewById(R.id.totalResult);
                totalResult.setText(String.valueOf(total));

                int[] diceDrawables = {
                        0,
                        R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
                        R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
                };

                ImageView img1 = findViewById(R.id.diceImage1);
                img1.setImageResource(diceDrawables[roll1]);

                ImageView img2 = findViewById(R.id.diceImage2);
                img2.setImageResource(diceDrawables[roll2]);
            }
        });
    }
}