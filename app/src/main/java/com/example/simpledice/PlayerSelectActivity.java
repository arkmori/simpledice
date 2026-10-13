package com.example.simpledice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlayerSelectActivity extends AppCompatActivity {

    private Spinner playerSpinner;
    private Group[] playerGroups = new Group[5];
    private EditText[] playerEditTexts = new EditText[5];
    private Spinner[] playerColorSpinners = new Spinner[5];
    private Button playButton;
    private int selectedPlayerCount = 1;

    private String[] colorNames = {"Red", "Blue", "Green", "Magenta", "Orange", "Cyan", "Black"};
    private int[] colorValues = {0xFFFF0000, 0xFF0000FF, 0xFF008000, 0xFFFF00FF, 0xFFFFA500, 0xFF00FFFF, 0xFF000000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_select);

        playerSpinner = findViewById(R.id.playerspinner);
        playButton = findViewById(R.id.playbutton);

        playerGroups[0] = findViewById(R.id.group1);
        playerGroups[1] = findViewById(R.id.group2);
        playerGroups[2] = findViewById(R.id.group3);
        playerGroups[3] = findViewById(R.id.group4);
        playerGroups[4] = findViewById(R.id.group5);

        playerEditTexts[0] = findViewById(R.id.p1name);
        playerEditTexts[1] = findViewById(R.id.p2name);
        playerEditTexts[2] = findViewById(R.id.p3name);
        playerEditTexts[3] = findViewById(R.id.p4name);
        playerEditTexts[4] = findViewById(R.id.p5name);

        playerColorSpinners[0] = findViewById(R.id.p1color);
        playerColorSpinners[1] = findViewById(R.id.p2color);
        playerColorSpinners[2] = findViewById(R.id.p3color);
        playerColorSpinners[3] = findViewById(R.id.p4color);
        playerColorSpinners[4] = findViewById(R.id.p5color);

        setupSpinners();
        setupColorSpinners();

        playButton.setOnClickListener(v -> {
            ArrayList<String> names = new ArrayList<>();
            int[] selectedColors = new int[selectedPlayerCount];
            Set<String> uniqueNameCheck = new HashSet<>();

            for (int i = 0; i < selectedPlayerCount; i++) {
                String name = playerEditTexts[i].getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(PlayerSelectActivity.this, "Player names cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!uniqueNameCheck.add(name.toLowerCase())) {
                    Toast.makeText(PlayerSelectActivity.this, "Player names must be unique!", Toast.LENGTH_SHORT).show();
                    return;
                }

                names.add(name);
                selectedColors[i] = colorValues[playerColorSpinners[i].getSelectedItemPosition()];
            }

            Intent intent = new Intent(PlayerSelectActivity.this, DiceActivity.class);
            intent.putStringArrayListExtra("PLAYER_NAMES", names);
            intent.putExtra("PLAYER_COLORS", selectedColors);
            startActivity(intent);
            finish(); // Close setup screen to keep navigation clean
        });
    }

    private void setupSpinners() {
        String[] options = {"1 Player", "2 Players", "3 Players", "4 Players", "5 Players"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(adapter);

        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlayerCount = position + 1;
                updateGroupVisibility();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupColorSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < 5; i++) {
            playerColorSpinners[i].setAdapter(adapter);
            playerColorSpinners[i].setSelection(i % colorNames.length); // Default staggered colors
        }
    }

    private void updateGroupVisibility() {
        for (int i = 0; i < 5; i++) {
            if (i < selectedPlayerCount) {
                playerGroups[i].setVisibility(View.VISIBLE);
            } else {
                playerGroups[i].setVisibility(View.GONE);
            }
        }
    }
}