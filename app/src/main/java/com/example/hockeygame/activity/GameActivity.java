package com.example.hockeygame.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hockeygame.game.model.ArenaType;
import com.example.hockeygame.game.view.GameView;
import com.example.hockeygame.R;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_ARENA_TYPE = "arena_type";
    public static final String EXTRA_HOST_NAME = "host_name";
    public static final String EXTRA_OPPONENT_NAME = "opponent_name";

    private GameView gameView;

    private TextView textViewTopPlayer;
    private TextView textViewBottomPlayer;
    private TextView textViewScore;

    private ArenaType arenaType;

    private int topPlayerScore = 0;
    private int bottomPlayerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        readIntentData();
        initializeGame();
    }

    private void initializeViews() {
        gameView = findViewById(R.id.gameView);

        textViewTopPlayer =
                findViewById(R.id.textViewTopPlayer);

        textViewBottomPlayer =
                findViewById(R.id.textViewBottomPlayer);

        textViewScore =
                findViewById(R.id.textViewScore);
    }

    private void readIntentData() {
        String arenaName = getIntent().getStringExtra(
                EXTRA_ARENA_TYPE
        );

        String hostName = getIntent().getStringExtra(
                EXTRA_HOST_NAME
        );

        String opponentName = getIntent().getStringExtra(
                EXTRA_OPPONENT_NAME
        );

        arenaType = parseArenaType(arenaName);

        if (hostName == null || hostName.trim().isEmpty()) {
            hostName = "Host";
        }

        if (opponentName == null
                || opponentName.trim().isEmpty()) {

            opponentName = "Opponent";
        }

        textViewTopPlayer.setText(hostName);
        textViewBottomPlayer.setText(opponentName);
    }

    private ArenaType parseArenaType(String arenaName) {
        if (arenaName == null) {
            return ArenaType.ARCTIC;
        }

        try {
            return ArenaType.valueOf(arenaName);
        } catch (IllegalArgumentException exception) {
            return ArenaType.ARCTIC;
        }
    }

    private void initializeGame() {
        gameView.setArenaType(arenaType);
        updateScore();
    }

    private void updateScore() {
        String scoreText =
                topPlayerScore + " : " + bottomPlayerScore;

        textViewScore.setText(scoreText);
    }
}