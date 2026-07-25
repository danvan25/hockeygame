package com.example.hockeygame;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class HostGameActivity extends AppCompatActivity {

    private static final String ROOM_CODE_CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int ROOM_CODE_LENGTH = 6;

    private TextView textViewRoomCode;
    private TextView textViewLobbyStatus;
    private TextView textViewOpponentName;
    private TextView textViewOpponentStatus;

    private Button buttonCopyCode;
    private Button buttonStartGame;
    private Button buttonCancelGame;

    private String roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        initializeViews();
        initializeLobby();
        initializeListeners();
    }

    private void initializeViews() {
        textViewRoomCode = findViewById(R.id.textViewRoomCode);
        textViewLobbyStatus = findViewById(R.id.textViewLobbyStatus);
        textViewOpponentName =
                findViewById(R.id.textViewOpponentName);
        textViewOpponentStatus =
                findViewById(R.id.textViewOpponentStatus);

        buttonCopyCode = findViewById(R.id.buttonCopyCode);
        buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonCancelGame = findViewById(R.id.buttonCancelGame);
    }

    private void initializeLobby() {
        roomCode = generateRoomCode();
        textViewRoomCode.setText(roomCode);
        setWaitingForOpponent();
    }

    private void initializeListeners() {
        buttonCopyCode.setOnClickListener(view -> copyRoomCode());

        buttonStartGame.setOnClickListener(view -> startGame());

        buttonCancelGame.setOnClickListener(view -> finish());
    }

    private String generateRoomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < ROOM_CODE_LENGTH; i++) {
            int index = random.nextInt(
                    ROOM_CODE_CHARACTERS.length()
            );

            code.append(ROOM_CODE_CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    private void copyRoomCode() {
        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(
                        Context.CLIPBOARD_SERVICE
                );

        ClipData clipData = ClipData.newPlainText(
                "Air Hockey room code",
                roomCode
        );

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(
                this,
                "Room code copied.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void setWaitingForOpponent() {
        textViewLobbyStatus.setText("Waiting for opponent...");
        textViewLobbyStatus.setTextColor(
                getColor(R.color.status_waiting)
        );

        textViewOpponentName.setText("Waiting...");
        textViewOpponentStatus.setText("NOT CONNECTED");
        textViewOpponentStatus.setTextColor(
                getColor(R.color.status_waiting)
        );

        buttonStartGame.setEnabled(false);
    }

    private void setOpponentConnected(String username) {
        textViewLobbyStatus.setText("Opponent connected!");
        textViewLobbyStatus.setTextColor(
                getColor(R.color.status_green)
        );

        textViewOpponentName.setText(username);
        textViewOpponentStatus.setText("READY");
        textViewOpponentStatus.setTextColor(
                getColor(R.color.status_green)
        );

        buttonStartGame.setEnabled(true);
    }

    private void startGame() {
        Toast.makeText(
                this,
                "Game started!",
                Toast.LENGTH_SHORT
        ).show();
    }
}