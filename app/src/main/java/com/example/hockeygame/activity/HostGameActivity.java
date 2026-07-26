package com.example.hockeygame.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hockeygame.game.model.ArenaType;
import com.example.hockeygame.R;

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
    private LinearLayout layoutArcticArena;
    private LinearLayout layoutNeonArena;
    private LinearLayout layoutClassicArena;

    private TextView textViewSelectedArena;

    private ArenaType selectedArena;
    private boolean opponentConnected;

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
        layoutArcticArena = findViewById(R.id.layoutArcticArena);
        layoutNeonArena = findViewById(R.id.layoutNeonArena);
        layoutClassicArena = findViewById(R.id.layoutClassicArena);

        textViewSelectedArena =
                findViewById(R.id.textViewSelectedArena);
    }

    private void initializeLobby() {
        roomCode = generateRoomCode();
        textViewRoomCode.setText(roomCode);
        setWaitingForOpponent();
        setOpponentConnected("Player42");
    }

    private void initializeListeners() {
        buttonCopyCode.setOnClickListener(view -> copyRoomCode());

        buttonStartGame.setOnClickListener(view -> startGame());

        buttonCancelGame.setOnClickListener(view -> finish());
        layoutArcticArena.setOnClickListener(
                view -> selectArena(ArenaType.ARCTIC)
        );

        layoutNeonArena.setOnClickListener(
                view -> selectArena(ArenaType.NEON)
        );

        layoutClassicArena.setOnClickListener(
                view -> selectArena(ArenaType.CLASSIC)
        );
    }

    private void selectArena(ArenaType arenaType) {
        selectedArena = arenaType;

        layoutArcticArena.setSelected(
                arenaType == ArenaType.ARCTIC
        );

        layoutNeonArena.setSelected(
                arenaType == ArenaType.NEON
        );

        layoutClassicArena.setSelected(
                arenaType == ArenaType.CLASSIC
        );

        textViewSelectedArena.setText(
                "Selected arena: " + arenaType.getDisplayName()
        );

        textViewSelectedArena.setTextColor(
                getColor(R.color.status_green)
        );

        updateStartButtonState();
    }

    private void updateStartButtonState() {
        boolean canStartGame =
                opponentConnected && selectedArena != null;

        buttonStartGame.setEnabled(canStartGame);
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

        opponentConnected = false;

        updateStartButtonState();
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

        opponentConnected = true;

        updateStartButtonState();
    }

    private void startGame() {
        if (!opponentConnected) {
            Toast.makeText(
                    this,
                    "Wait for an opponent.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (selectedArena == null) {
            Toast.makeText(
                    this,
                    "Please select an arena.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Intent intent = new Intent(
                HostGameActivity.this,
                GameActivity.class
        );

        intent.putExtra(
                GameActivity.EXTRA_ARENA_TYPE,
                selectedArena.name()
        );

        intent.putExtra(
                GameActivity.EXTRA_HOST_NAME,
                "Daniel"
        );

        intent.putExtra(
                GameActivity.EXTRA_OPPONENT_NAME,
                textViewOpponentName.getText().toString()
        );

        startActivity(intent);
    }
}