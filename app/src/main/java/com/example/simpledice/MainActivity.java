package com.example.simpledice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Spinner playerCountSpinner;

    private Spinner diceCountSpinner;
    private ImageView[] diceImages;
    private int[] diceDrawables = {
            0,
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_select);


        diceCountSpinner = findViewById(R.id.playerspinner);

        Integer[] players = new Integer[]{1, 2, 3, 4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, players);
        playerCountSpinner.setAdapter(adapter);



        diceCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDiceLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        diceCountSpinner.setSelection(1);

        myButtonListenerMethod();
    }

    private void updatePlayerLayout() {
        int selectedPlayerCount = (Integer) playerCountSpinner.getSelectedItem();

        ArrayList<TextView> textViews = new ArrayList<>();
        int[] playerGroupField = {R.id.group1, R.id.group2, R.id.group3, R.id.group4, R.id.group5};

    }

    private void updateDiceLayout() {
        int selectedCount = (Integer) diceCountSpinner.getSelectedItem();

        for (int i = 0; i < 4; i++) {
            diceImages[i].setImageResource(R.drawable.dicegeneral);

            if (i < selectedCount) {
                diceImages[i].setVisibility(View.VISIBLE);
            } else {
                diceImages[i].setVisibility(View.GONE);
            }
        }

        TextView totalResult = findViewById(R.id.totalResult);
        totalResult.setText("Total: 0");
    }

    public void myButtonListenerMethod() {
        Button button = findViewById(R.id.rollButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int total = 0;
                int selectedCount = (Integer) diceCountSpinner.getSelectedItem();

                for (int i = 0; i < selectedCount; i++) {
                    int roll = rand.nextInt(6) + 1;
                    total += roll;
                    diceImages[i].setImageResource(diceDrawables[roll]);
                }

                TextView totalResult = findViewById(R.id.totalResult);
                totalResult.setText("Total: " + total);
            }
        });
    }
}