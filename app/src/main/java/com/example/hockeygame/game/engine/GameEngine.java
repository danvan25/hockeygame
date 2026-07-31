package com.example.hockeygame.game.engine;

import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;
import com.example.hockeygame.game.physics.PhysicsEngine;
import com.example.hockeygame.game.rules.GameRules;
import com.example.hockeygame.game.collision.GoalResult;

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

    public void startPuck(float velocityX, float velocityY) {
        puck.setVelocity(
                velocityX,
                velocityY
        );
    }

    public void update(float deltaTime) {
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
                gameRules.update(
                        puck,
                        fieldWidth,
                        fieldHeight,
                        fieldMargin,
                        goalWidth
                );

        if (goalResult != GoalResult.NONE) {
            resetAfterGoal(goalResult);
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

        float clampedX = Math.max(
                minimumX,
                Math.min(x, maximumX)
        );

        float clampedY = Math.max(
                minimumY,
                Math.min(y, maximumY)
        );

        bottomMallet.setPosition(
                clampedX,
                clampedY
        );

        bottomMallet.setVelocity(
                clampedX - previousX,
                clampedY - previousY
        );
    }

    private void resetAfterGoal(GoalResult goalResult) {
        resetPositions();

        if (goalResult == GoalResult.TOP_GOAL) {
            startPuck(
                    400f,
                    300f
            );
        } else if (goalResult == GoalResult.BOTTOM_GOAL) {
            startPuck(
                    -400f,
                    -300f
            );
        }
    }
}