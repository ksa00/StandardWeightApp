package com.example.standardweight;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText heightInput;
    private TextView messageView;
    private SharedPreferences preferences;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // UI references
        Button  calculateButton = findViewById(R.id.CalculateButton);
         heightInput = findViewById(R.id.edittextHeight);
         messageView = findViewById(R.id.MessageView);
         resources = getResources();


// Load saved preferences
 preferences = getSharedPreferences("StandardWeightApp", Activity.MODE_PRIVATE);
 String savedHeight = preferences.getString("Height_", "");
 heightInput.setText(savedHeight);

        // Button click listener
        calculateButton.setOnClickListener(v -> {
            String heightStr = heightInput.getText().toString().trim();

            if (heightStr.isEmpty()) {
                messageView.setText(resources.getString(R.string.InputMessage));
                return;
            }

            try {
                float heightCm = Float.parseFloat(heightStr);
                float heightM =heightCm / 100.0f;
                float standardWeight = 22 * heightM * heightM;
                String resultMessage = String.format(resources.getString(R.string.result_message), standardWeight);
                messageView.setText(resultMessage);


// Save height to preferences(Used When you want data even after the app closes)
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Height_", heightStr);
                editor.apply();

            } catch (NumberFormatException e) {
                messageView.setText(resources.getString(R.string.invalid_input));
            }
//            //preference data save
//            SharedPreferences preferences=getSharedPreferences("StandardWeightApp", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor= preferences.edit();
//            //store height after calculation
//            editor.putString("Height_",heightStr);
//            editor.commit();
//            //as it is now the standard weight data will  get lost
        });
    }
    //"saving" data  before screen orientation change
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("STD_WEIGHT_MSG", messageView.getText().toString());
    }
    //"reading" saved data after screen orientation changes

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String restoredMessage = savedInstanceState.getString("STD_WEIGHT_MSG", "");
        messageView.setText(restoredMessage);
    }
}

