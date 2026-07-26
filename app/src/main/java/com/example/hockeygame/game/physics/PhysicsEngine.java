package com.example.hockeygame.game.physics;

import com.example.hockeygame.game.model.Puck;

public class PhysicsEngine {

    public void updatePuck(
            Puck puck,
            float fieldWidth,
            float fieldHeight,
            float fieldMargin,
            float deltaTime
    ) {
        movePuck(puck, deltaTime);

        handleWallCollision(
                puck,
                fieldWidth,
                fieldHeight,
                fieldMargin
        );
    }

    private void movePuck(
            Puck puck,
            float deltaTime
    ) {
        float newX =
                puck.getX()
                        + puck.getVelocityX() * deltaTime;

        float newY =
                puck.getY()
                        + puck.getVelocityY() * deltaTime;

        puck.setPosition(
                newX,
                newY
        );
    }

    private void handleWallCollision(
            Puck puck,
            float fieldWidth,
            float fieldHeight,
            float fieldMargin
    ) {
        float radius = puck.getRadius();

        float minimumX = fieldMargin + radius;
        float maximumX =
                fieldWidth - fieldMargin - radius;

        float minimumY = fieldMargin + radius;
        float maximumY =
                fieldHeight - fieldMargin - radius;

        if (puck.getX() < minimumX) {
            puck.setPosition(
                    minimumX,
                    puck.getY()
            );

            puck.setVelocity(
                    Math.abs(puck.getVelocityX()),
                    puck.getVelocityY()
            );
        } else if (puck.getX() > maximumX) {
            puck.setPosition(
                    maximumX,
                    puck.getY()
            );

            puck.setVelocity(
                    -Math.abs(puck.getVelocityX()),
                    puck.getVelocityY()
            );
        }

        if (puck.getY() < minimumY) {
            puck.setPosition(
                    puck.getX(),
                    minimumY
            );

            puck.setVelocity(
                    puck.getVelocityX(),
                    Math.abs(puck.getVelocityY())
            );
        } else if (puck.getY() > maximumY) {
            puck.setPosition(
                    puck.getX(),
                    maximumY
            );

            puck.setVelocity(
                    puck.getVelocityX(),
                    -Math.abs(puck.getVelocityY())
            );
        }
    }
}