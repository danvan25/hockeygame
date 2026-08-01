package com.example.hockeygame.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hockeygame.game.model.ArenaType;
import com.example.hockeygame.game.view.GameView;
import com.example.hockeygame.R;
import com.example.hockeygame.game.model.Score;
import android.view.View;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_ARENA_TYPE = "arena_type";
    public static final String EXTRA_HOST_NAME = "host_name";
    public static final String EXTRA_OPPONENT_NAME = "opponent_name";
    private GameView gameView;
    private TextView textViewTopPlayer;
    private TextView textViewBottomPlayer;
    private TextView textViewScore;
    private ArenaType arenaType;
    private final Runnable scoreUpdater = new Runnable() {
        @Override
        public void run() {
            updateScoreText();

            textViewScore.postDelayed(
                    this,
                    100L
            );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideSystemBars();
        initializeViews();
        readIntentData();
        initializeGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemBars();
        textViewScore.post(scoreUpdater);
    }

    @Override
    protected void onPause() {
        textViewScore.removeCallbacks(scoreUpdater);

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemBars();
        }
    }

    private void updateScoreText() {
        if (gameView.getGameEngine() == null) {
            return;
        }

        Score score =
                gameView
                        .getGameEngine()
                        .getScore();

        String scoreText =
                score.getHostScore()
                        + " : "
                        + score.getOpponentScore();

        textViewScore.setText(scoreText);
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
        updateScoreText();
    }

    private void hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        View decorView =
                getWindow().getDecorView();

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        decorView
                );

        controller.hide(
                WindowInsetsCompat.Type.systemBars()
        );

        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }

}