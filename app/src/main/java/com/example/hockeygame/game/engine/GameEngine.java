package com.example.hockeygame.game.engine;

import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;
import com.example.hockeygame.game.physics.PhysicsEngine;

public class GameEngine {

    private final Puck puck;
    private final Mallet topMallet;
    private final Mallet bottomMallet;
    private final Score score;

    private int fieldWidth;
    private int fieldHeight;
    private final PhysicsEngine physicsEngine;
    private float fieldMargin;

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
            float margin
    ) {
        this.fieldWidth = width;
        this.fieldHeight = height;
        this.fieldMargin = margin;
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
                fieldWidth,
                fieldHeight,
                fieldMargin,
                deltaTime
        );
    }
}