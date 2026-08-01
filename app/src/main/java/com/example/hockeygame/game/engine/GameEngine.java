package com.example.hockeygame.game.engine;

import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;
import com.example.hockeygame.game.physics.PhysicsEngine;
import com.example.hockeygame.game.rules.GameRules;
import com.example.hockeygame.game.collision.GoalResult;
import com.example.hockeygame.game.model.GameState;

public class GameEngine {
    private final Puck puck;
    private final Mallet topMallet;
    private final Mallet bottomMallet;
    private final Score score;
    private int fieldWidth;
    private int fieldHeight;
    private final PhysicsEngine physicsEngine;
    private float fieldMargin;
    private float goalWidth;
    private final GameRules gameRules;
    private static final float COUNTDOWN_DURATION_SECONDS = 3f;
    private GameState gameState = GameState.COUNTDOWN;
    private float countdownRemaining = COUNTDOWN_DURATION_SECONDS;
    private static final float MALLET_VELOCITY_SMOOTHING = 0.40f;

    public GameEngine(
            Puck puck,
            Mallet topMallet,
            Mallet bottomMallet,
            Score score
    ) {
        this.puck = puck;
        this.topMallet = topMallet;
        this.bottomMallet = bottomMallet;
        this.score = score;
        this.physicsEngine = new PhysicsEngine();
        this.gameRules = new GameRules(score);
    }

    public Puck getPuck() {
        return puck;
    }

    public Mallet getTopMallet() {
        return topMallet;
    }

    public Mallet getBottomMallet() {
        return bottomMallet;
    }

    public Score getScore() {
        return score;
    }

    public void setFieldSize(
            int width,
            int height,
            float margin,
            float goalWidth
    ) {
        this.fieldWidth = width;
        this.fieldHeight = height;
        this.fieldMargin = margin;
        this.goalWidth = goalWidth;
    }

    public void resetPositions() {
        if (fieldWidth <= 0 || fieldHeight <= 0) {
            return;
        }

        puck.setPosition(
                fieldWidth / 2f,
                fieldHeight / 2f
        );

        puck.setVelocity(
                0f,
                0f
        );

        topMallet.setPosition(
                fieldWidth / 2f,
                fieldHeight * 0.25f
        );

        bottomMallet.setPosition(
                fieldWidth / 2f,
                fieldHeight * 0.75f
        );
    }

    public void startPuck(float velocityX, float velocityY)
        {
            puck.setVelocity(velocityX, velocityY
        );
    }

    public void startCountdown() {
        resetPositions();
        countdownRemaining = COUNTDOWN_DURATION_SECONDS;
        gameState = GameState.COUNTDOWN;
    }

    public void update(float deltaTime) {
        if (gameState == GameState.COUNTDOWN)
        {
            updateCountdown(deltaTime);
            return;
        }

        physicsEngine.updatePuck(
                puck,
                topMallet,
                bottomMallet,
                fieldWidth,
                fieldHeight,
                fieldMargin,
                goalWidth,
                deltaTime
        );

        GoalResult goalResult =
                gameRules.update(puck, fieldWidth, fieldHeight, fieldMargin, goalWidth);

        if (goalResult != GoalResult.NONE) {
            startCountdown();
        }
    }

    private void updateCountdown(float deltaTime) {
        countdownRemaining -= deltaTime;

        if (countdownRemaining <= 0f) {
            countdownRemaining = 0f;
            gameState = GameState.PLAYING;

            /*
             * A korong továbbra is áll.
             * Az első ütés indítja el.
             */
            puck.setVelocity(0f, 0f
            );
        }
    }
    public void setBottomMalletPosition(float x, float y) {
        float previousX = bottomMallet.getX();
        float previousY = bottomMallet.getY();

        float radius = bottomMallet.getRadius();

        float minimumX = fieldMargin + radius;
        float maximumX = fieldWidth - fieldMargin - radius;

        float minimumY = fieldHeight / 2f + radius;
        float maximumY = fieldHeight - fieldMargin - radius;

        float clampedX = Math.max(minimumX, Math.min(x, maximumX)
        );

        float clampedY = Math.max(minimumY, Math.min(y, maximumY)
        );

        float measuredVelocityX = clampedX - previousX;
        float measuredVelocityY = clampedY - previousY;

        float smoothedVelocityX = bottomMallet.getVelocityX() + (measuredVelocityX
                - bottomMallet.getVelocityX()) * MALLET_VELOCITY_SMOOTHING;

        float smoothedVelocityY =
                bottomMallet.getVelocityY() + (measuredVelocityY - bottomMallet.getVelocityY())
                        * MALLET_VELOCITY_SMOOTHING;

        bottomMallet.setPosition(clampedX, clampedY
        );

        bottomMallet.setVelocity(smoothedVelocityX, smoothedVelocityY
        );
    }

    public boolean isCountdownActive() {
        return gameState == GameState.COUNTDOWN;
    }

    public int getCountdownNumber() {
        if (!isCountdownActive()) {
            return 0;
        }

        return Math.max(1, (int) Math.ceil(countdownRemaining)
        );
    }

}