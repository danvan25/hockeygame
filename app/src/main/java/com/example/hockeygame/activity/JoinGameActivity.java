package com.example.hockeygame.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hockeygame.R;

import java.util.Locale;

public class JoinGameActivity extends AppCompatActivity {

    private static final int ROOM_CODE_LENGTH = 6;

    private LinearLayout layoutJoinForm;
    private LinearLayout layoutJoinedLobby;

    private EditText editTextRoomCode;

    private TextView textViewJoinedRoomCode;
    private TextView textViewJoinedHostName;
    private TextView textViewJoinedPlayerName;

    private Button buttonJoinRoom;
    private Button buttonBackToMenu;
    private Button buttonLeaveLobby;

    private String currentRoomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        initializeViews();
        initializeListeners();
        showJoinForm();
    }

    private void initializeViews() {
        layoutJoinForm = findViewById(R.id.layoutJoinForm);
        layoutJoinedLobby = findViewById(R.id.layoutJoinedLobby);

        editTextRoomCode = findViewById(R.id.editTextRoomCode);

        textViewJoinedRoomCode =
                findViewById(R.id.textViewJoinedRoomCode);

        textViewJoinedHostName =
                findViewById(R.id.textViewJoinedHostName);

        textViewJoinedPlayerName =
                findViewById(R.id.textViewJoinedPlayerName);

        buttonJoinRoom = findViewById(R.id.buttonJoinRoom);
        buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonLeaveLobby = findViewById(R.id.buttonLeaveLobby);
    }

    private void initializeListeners() {
        buttonJoinRoom.setOnClickListener(view -> handleJoinGame());

        buttonBackToMenu.setOnClickListener(view -> finish());

        buttonLeaveLobby.setOnClickListener(view -> leaveLobby());

        editTextRoomCode.setOnEditorActionListener(
                (textView, actionId, event) -> {
                    handleJoinGame();
                    return true;
                }
        );
    }

    private void handleJoinGame() {
        String roomCode = editTextRoomCode
                .getText()
                .toString()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (!validateRoomCode(roomCode)) {
            return;
        }

        joinRoom(roomCode);
    }

    private boolean validateRoomCode(String roomCode) {
        if (roomCode.isEmpty()) {
            editTextRoomCode.setError("Room code is required.");
            editTextRoomCode.requestFocus();
            return false;
        }

        if (roomCode.length() != ROOM_CODE_LENGTH) {
            editTextRoomCode.setError(
                    "Room code must contain exactly 6 characters."
            );

            editTextRoomCode.requestFocus();
            return false;
        }

        if (!roomCode.matches("^[A-Z0-9]+$")) {
            editTextRoomCode.setError(
                    "Room code may contain only letters and numbers."
            );

            editTextRoomCode.requestFocus();
            return false;
        }

        return true;
    }

    private void joinRoom(String roomCode) {
        /*
         * Később itt küldjük el a szobakódot a Spring Boot
         * szervernek. A szerver ellenőrzi majd, hogy:
         *
         * - létezik-e a szoba;
         * - nincs-e tele;
         * - nem indult-e már el a játék;
         * - csatlakozhat-e a felhasználó.
         */

        currentRoomCode = roomCode;

        hideKeyboard();

        // Ideiglenes tesztadatok.
        String hostUsername = "Daniel";
        String playerUsername = "Player42";

        showJoinedLobby(
                roomCode,
                hostUsername,
                playerUsername
        );

        Toast.makeText(
                this,
                "Joined room " + roomCode,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showJoinForm() {
        layoutJoinForm.setVisibility(View.VISIBLE);
        layoutJoinedLobby.setVisibility(View.GONE);
    }

    private void showJoinedLobby(
            String roomCode,
            String hostUsername,
            String playerUsername
    ) {
        textViewJoinedRoomCode.setText(roomCode);
        textViewJoinedHostName.setText(hostUsername);
        textViewJoinedPlayerName.setText(playerUsername);

        layoutJoinForm.setVisibility(View.GONE);
        layoutJoinedLobby.setVisibility(View.VISIBLE);
    }

    private void leaveLobby() {
        currentRoomCode = null;
        editTextRoomCode.setText("");

        showJoinForm();

        Toast.makeText(
                this,
                "You left the lobby.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void hideKeyboard() {
        View focusedView = getCurrentFocus();

        if (focusedView == null) {
            return;
        }

        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );

        inputMethodManager.hideSoftInputFromWindow(
                focusedView.getWindowToken(),
                0
        );

        focusedView.clearFocus();
    }
}