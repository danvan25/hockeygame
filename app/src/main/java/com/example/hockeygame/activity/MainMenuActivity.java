package com.example.hockeygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hockeygame.R;

public class MainMenuActivity extends AppCompatActivity {

    private TextView textViewWelcome;

    private Button buttonHostGame;
    private Button buttonJoinGame;
    private Button buttonStatistics;
    private Button buttonProfile;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initializeViews();
        initializeListeners();
        displayUser();
    }

    private void initializeViews() {
        textViewWelcome = findViewById(R.id.textViewWelcome);

        buttonHostGame = findViewById(R.id.buttonHostGame);
        buttonJoinGame = findViewById(R.id.buttonJoinGame);
        buttonStatistics = findViewById(R.id.buttonStatistics);
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
    }

    private void initializeListeners() {
        buttonHostGame.setOnClickListener(view -> hostGame());

        buttonJoinGame.setOnClickListener(view -> joinGame());

        buttonStatistics.setOnClickListener(view -> openStatistics());

        buttonProfile.setOnClickListener(view -> openProfile());

        buttonLogout.setOnClickListener(view -> logout());
    }

    private void displayUser() {
        String username = getIntent().getStringExtra("username");

        if (username == null || username.trim().isEmpty()) {
            username = "Player";
        }

        textViewWelcome.setText("Welcome back, " + username + "!");
    }

    private void hostGame() {
        Intent intent = new Intent(
                MainMenuActivity.this,
                HostGameActivity.class
        );

        startActivity(intent);
    }

    private void joinGame() {
        Intent intent = new Intent(
                MainMenuActivity.this,
                JoinGameActivity.class
        );

        startActivity(intent);
    }

    private void openStatistics() {
        Toast.makeText(
                this,
                "Statistics selected",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openProfile() {
        Toast.makeText(
                this,
                "Profile selected",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void logout() {
        Intent intent = new Intent(
                MainMenuActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
    }
}