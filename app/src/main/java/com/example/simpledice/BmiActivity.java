package com.example.simpledice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class BmiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> finish());

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText heightText = findViewById(R.id.heightInput);
                String heightStr = heightText.getText().toString().trim();

                EditText weightText = findViewById(R.id.weightInput);
                String weightStr = weightText.getText().toString().trim();

                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                    Toast.makeText(BmiActivity.this, "Please enter both height and weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double height = Double.parseDouble(heightStr);
                    double weight = Double.parseDouble(weightStr);

                    if (height == 0) {
                        Toast.makeText(BmiActivity.this, "Height cannot be zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (height >= 1000 || weight >= 1000) {
                        Toast.makeText(BmiActivity.this, "Values must be 3 digits or less", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double BMI = weight / (height * height);
                    DecimalFormat df = new DecimalFormat("#.#");
                    String formattedBmi = df.format(BMI);

                    EditText BMIResult = findViewById(R.id.BMIResult);
                    BMIResult.setText(formattedBmi);

                    String BMI_Cat;
                    double nextThreshold = -1, prevThreshold = -1;
                    String nextCatName = "", prevCatName = "";

                    if (BMI < 15) {
                        BMI_Cat = "Very severely underweight";
                        nextThreshold = 15; nextCatName = "Severely underweight";
                    } else if (BMI < 16) {
                        BMI_Cat = "Severely underweight";
                        nextThreshold = 16; nextCatName = "Underweight";
                        prevThreshold = 15; prevCatName = "Very severely underweight";
                    } else if (BMI < 18.5) {
                        BMI_Cat = "Underweight";
                        nextThreshold = 18.5; nextCatName = "Normal";
                        prevThreshold = 16; prevCatName = "Severely underweight";
                    } else if (BMI < 25) {
                        BMI_Cat = "Normal";
                        nextThreshold = 25; nextCatName = "Overweight";
                        prevThreshold = 18.5; prevCatName = "Underweight";
                    } else if (BMI < 30) {
                        BMI_Cat = "Overweight";
                        nextThreshold = 30; nextCatName = "Obese Class 1";
                        prevThreshold = 25; prevCatName = "Normal";
                    } else if (BMI < 35) {
                        BMI_Cat = "Obese Class 1 - Moderately Obese";
                        nextThreshold = 35; nextCatName = "Obese Class 2";
                        prevThreshold = 30; prevCatName = "Overweight";
                    } else if (BMI < 40) {
                        BMI_Cat = "Obese Class 2 - Severely Obese";
                        nextThreshold = 40; nextCatName = "Obese Class 3";
                        prevThreshold = 35; prevCatName = "Obese Class 1";
                    } else {
                        BMI_Cat = "Obese Class 3 - Very Severely Obese";
                        prevThreshold = 40; prevCatName = "Obese Class 2";
                    }

                    TextView BMICategory = findViewById(R.id.BMICategory);
                    BMICategory.setText("BMI Category: " + BMI_Cat);

                    TextView weightThresholds = findViewById(R.id.weightThresholds);
                    StringBuilder thresholdsText = new StringBuilder();

                    if (nextThreshold != -1) {
                        double requiredWeight = nextThreshold * (height * height);
                        thresholdsText.append("Weight for ").append(nextCatName).append(":\n")
                                .append(df.format(requiredWeight)).append(" kg\n\n");
                    } else {
                        thresholdsText.append("You are in the highest category.\n\n");
                    }

                    if (prevThreshold != -1) {
                        double requiredWeight = prevThreshold * (height * height);
                        thresholdsText.append("Weight for ").append(prevCatName).append(":\n")
                                .append(df.format(requiredWeight)).append(" kg");
                    } else {
                        thresholdsText.append("You are in the lowest category.");
                    }

                    weightThresholds.setText(thresholdsText.toString().trim());

                } catch (NumberFormatException e) {
                    Toast.makeText(BmiActivity.this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}