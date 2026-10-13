package com.example.simpledice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    private ImageView[] diceImages = new ImageView[4];
    private Spinner diceCountSpinner;
    private TextView totalResult;
    private TextView diceResult;
    private TextView currentPlayerText;
    private TextView turnOrderText;

    private Button rollButton;
    private Button prevRollButton;
    private Button nextRollButton;
    private Button nextPlayerButton;
    private Button prevPlayerButton;
    private Button btnHome;

    private int activeDiceCount = 2;
    private ArrayList<int[]> currentRollHistory = new ArrayList<>();
    private int currentHistoryIndex = -1;

    private ArrayList<String> playerNames = new ArrayList<>();
    private int[] playerColors;
    private int currentPlayerIndex = 0;

    int[] diceDrawables = {
            R.drawable.dicegeneral,
            R.drawable.dice1, R.drawable.dice2,
            R.drawable.dice3, R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        playerNames = getIntent().getStringArrayListExtra("PLAYER_NAMES");
        if (playerNames == null || playerNames.isEmpty()) {
            playerNames = new ArrayList<>();
            playerNames.add("Player 1");
        }

        playerColors = getIntent().getIntArrayExtra("PLAYER_COLORS");
        if (playerColors == null || playerColors.length < playerNames.size()) {
            playerColors = new int[]{0xFFFF0000, 0xFF0000FF, 0xFF008000, 0xFFFF00FF, 0xFFFFA500};
        }

        diceImages[0] = findViewById(R.id.diceImage1);
        diceImages[1] = findViewById(R.id.diceImage2);
        diceImages[2] = findViewById(R.id.diceImage3);
        diceImages[3] = findViewById(R.id.diceImage4);

        totalResult = findViewById(R.id.totalResult);
        diceResult = findViewById(R.id.diceResult);
        currentPlayerText = findViewById(R.id.currentPlayerText);
        turnOrderText = findViewById(R.id.turnOrderText);

        rollButton = findViewById(R.id.rollButton);
        prevRollButton = findViewById(R.id.prevRollButton);
        nextRollButton = findViewById(R.id.nextRollButton);
        nextPlayerButton = findViewById(R.id.nextPlayerButton);
        prevPlayerButton = findViewById(R.id.prevPlayerButton);
        diceCountSpinner = findViewById(R.id.diceCountSpinner);

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> finish()); // Return to Main Menu

        setupSpinners();
        updatePlayerUI();
        setupClickListeners();
        updateHistoryButtons();
    }

    private void setupSpinners() {
        String[] counts = {"1 Die", "2 Dice", "3 Dice", "4 Dice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, counts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diceCountSpinner.setAdapter(adapter);
        diceCountSpinner.setSelection(1);

        diceCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activeDiceCount = position + 1;
                adjustDiceVisibility();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupClickListeners() {
        rollButton.setOnClickListener(v -> performRoll());

        prevRollButton.setOnClickListener(v -> {
            if (currentHistoryIndex > 0) {
                currentHistoryIndex--;
                displayRoll(currentRollHistory.get(currentHistoryIndex));
            }
        });

        nextRollButton.setOnClickListener(v -> {
            if (currentHistoryIndex < currentRollHistory.size() - 1) {
                currentHistoryIndex++;
                displayRoll(currentRollHistory.get(currentHistoryIndex));
            }
        });

        nextPlayerButton.setOnClickListener(v -> switchPlayer(1));
        prevPlayerButton.setOnClickListener(v -> switchPlayer(-1));
    }

    private void performRoll() {
        if (currentHistoryIndex < currentRollHistory.size() - 1 && currentHistoryIndex >= 0) {
            currentRollHistory.subList(currentHistoryIndex + 1, currentRollHistory.size()).clear();
        }

        Random rand = new Random();
        int[] newRoll = new int[activeDiceCount];
        for (int i = 0; i < activeDiceCount; i++) {
            newRoll[i] = rand.nextInt(6) + 1;
        }

        currentRollHistory.add(newRoll);
        currentHistoryIndex = currentRollHistory.size() - 1;

        displayRoll(newRoll);
        rollButton.setText("REROLL!");
    }

    private void displayRoll(int[] rollData) {
        int total = 0;

        for (int i = 0; i < 4; i++) {
            if (i < rollData.length) {
                diceImages[i].setImageResource(diceDrawables[rollData[i]]);
                diceImages[i].setVisibility(View.VISIBLE);
                total += rollData[i];
            } else {
                diceImages[i].setVisibility(View.GONE);
            }
        }

        if (activeDiceCount != rollData.length) {
            diceCountSpinner.setSelection(rollData.length - 1);
        }

        totalResult.setText("Total: " + total);
        diceResult.setText("Roll #" + (currentHistoryIndex + 1));
        updateHistoryButtons();
    }

    private void adjustDiceVisibility() {
        for (int i = 0; i < 4; i++) {
            if (i < activeDiceCount) {
                diceImages[i].setVisibility(View.VISIBLE);
            } else {
                diceImages[i].setVisibility(View.GONE);
            }
        }
    }

    private void updateHistoryButtons() {
        prevRollButton.setEnabled(currentHistoryIndex > 0);
        nextRollButton.setEnabled(currentHistoryIndex < currentRollHistory.size() - 1);
    }

    private void switchPlayer(int direction) {
        currentPlayerIndex += direction;

        if (currentPlayerIndex >= playerNames.size()) {
            currentPlayerIndex = 0;
        } else if (currentPlayerIndex < 0) {
            currentPlayerIndex = playerNames.size() - 1;
        }

        currentRollHistory.clear();
        currentHistoryIndex = -1;
        totalResult.setText("Total: 0");
        diceResult.setText("Please tap the button...");
        rollButton.setText("ROLL!");

        resetDiceImages();
        updatePlayerUI();
        updateHistoryButtons();
    }

    private void updatePlayerUI() {
        currentPlayerText.setText(playerNames.get(currentPlayerIndex) + "'s Turn");
        currentPlayerText.setTextColor(playerColors[currentPlayerIndex]);

        boolean enablePlayerSwitch = playerNames.size() > 1;
        prevPlayerButton.setEnabled(enablePlayerSwitch);
        nextPlayerButton.setEnabled(enablePlayerSwitch);

        if (enablePlayerSwitch) {
            int prevIndex = (currentPlayerIndex - 1 + playerNames.size()) % playerNames.size();
            int nextIndex = (currentPlayerIndex + 1) % playerNames.size();

            turnOrderText.setText("Previous: " + playerNames.get(prevIndex) + " | Next: " + playerNames.get(nextIndex));

            prevPlayerButton.setText("< " + playerNames.get(prevIndex));
            nextPlayerButton.setText(playerNames.get(nextIndex) + " >");
        } else {
            turnOrderText.setText("Solo Play");
            prevPlayerButton.setText("PREV");
            nextPlayerButton.setText("NEXT");
        }
    }

    private void resetDiceImages() {
        for (int i = 0; i < 4; i++) {
            diceImages[i].setImageResource(diceDrawables[0]);
        }
    }
}