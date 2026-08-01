package com.example.hockeygame.game.physics;

import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
public class PhysicsEngine {
    private static final float MAX_PUCK_SPEED = 1600f;
    private static final float PUCK_DAMPING_PER_SECOND = 0.35f;
    private static final float PUCK_STOP_SPEED = 12f;

    private void limitPuckSpeed(Puck puck) {
        float velocityX = puck.getVelocityX();
        float velocityY = puck.getVelocityY();

        float speedSquared =
                velocityX * velocityX
                        + velocityY * velocityY;

        float maximumSpeedSquared =
                MAX_PUCK_SPEED * MAX_PUCK_SPEED;

        if (speedSquared <= maximumSpeedSquared) {
            return;
        }

        float speed =
                (float) Math.sqrt(speedSquared);

        float scale =
                MAX_PUCK_SPEED / speed;

        puck.setVelocity(
                velocityX * scale,
                velocityY * scale
        );
    }
    public void updatePuck(
            Puck puck,
            Mallet topMallet,
            Mallet bottomMallet,
            float fieldWidth,
            float fieldHeight,
            float fieldMargin,
            float goalWidth,
            float deltaTime
    ) {
        movePuck(puck, deltaTime);

        handleWallCollision(
                puck,
                fieldWidth,
                fieldHeight,
                fieldMargin,
                goalWidth
        );

        handleMalletCollision(puck, topMallet);
        handleMalletCollision(puck, bottomMallet);

        limitPuckSpeed(puck);
        applyPuckDamping(puck, deltaTime);
    }

    private void handleMalletCollision(
            Puck puck,
            Mallet mallet
    ) {
        float differenceX =
                puck.getX() - mallet.getX();

        float differenceY =
                puck.getY() - mallet.getY();

        float distanceSquared =
                differenceX * differenceX
                        + differenceY * differenceY;

        float minimumDistance =
                puck.getRadius() + mallet.getRadius();

        float minimumDistanceSquared =
                minimumDistance * minimumDistance;

        /*
         * Ha a középpontok távolsága nagyobb,
         * mint a sugarak összege, nincs ütközés.
         */
        if (distanceSquared >= minimumDistanceSquared) {
            return;
        }

        float distance =
                (float) Math.sqrt(distanceSquared);

        /*
         * Ritka eset: a két kör középpontja
         * pontosan ugyanazon a ponton van.
         */
        if (distance < 0.0001f) {
            differenceX = 0f;
            differenceY = -1f;
            distance = 1f;
        }

        /*
         * Az ütőtől a korong felé mutató
         * egységnyi normálvektor.
         */
        float normalX = differenceX / distance;
        float normalY = differenceY / distance;

        float correctedX =
                mallet.getX()
                        + normalX * minimumDistance;

        float correctedY =
                mallet.getY()
                        + normalY * minimumDistance;

        puck.setPosition(
                correctedX,
                correctedY
        );

        float velocityAlongNormal =
                puck.getVelocityX() * normalX
                        + puck.getVelocityY() * normalY;

        float newVelocityX =
                puck.getVelocityX();

        float newVelocityY =
                puck.getVelocityY();

        if (velocityAlongNormal < 0f) {
            newVelocityX =
                    puck.getVelocityX()
                            - 2f
                            * velocityAlongNormal
                            * normalX;

            newVelocityY =
                    puck.getVelocityY()
                            - 2f
                            * velocityAlongNormal
                            * normalY;
        }

        float malletInfluence = 35f;

        newVelocityX +=
                mallet.getVelocityX() * malletInfluence;

        newVelocityY +=
                mallet.getVelocityY() * malletInfluence;

        puck.setVelocity(
                newVelocityX,
                newVelocityY
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
            float fieldMargin,
            float goalWidth
    ) {
        float goalLeft =
                (fieldWidth - goalWidth) / 2f;

        float goalRight =
                goalLeft + goalWidth;

        boolean puckInsideGoalOpening =
                puck.getX() >= goalLeft
                        && puck.getX() <= goalRight;

        float radius = puck.getRadius();

        float minimumX = fieldMargin + radius;
        float maximumX =
                fieldWidth - fieldMargin - radius;

        float minimumY = fieldMargin + radius;
        float maximumY =
                fieldHeight - fieldMargin - radius;

        /*
         * Bal fal
         */
        if (puck.getX() < minimumX) {

            puck.setPosition(
                    minimumX,
                    puck.getY()
            );

            puck.setVelocity(
                    Math.abs(puck.getVelocityX()),
                    puck.getVelocityY()
            );
        }

        /*
         * Jobb fal
         */
        else if (puck.getX() > maximumX) {

            puck.setPosition(
                    maximumX,
                    puck.getY()
            );

            puck.setVelocity(
                    -Math.abs(puck.getVelocityX()),
                    puck.getVelocityY()
            );
        }

        if (!puckInsideGoalOpening
                && puck.getY() < minimumY) {

            puck.setPosition(
                    puck.getX(),
                    minimumY
            );

            puck.setVelocity(
                    puck.getVelocityX(),
                    Math.abs(puck.getVelocityY())
            );
        } else if (!puckInsideGoalOpening
                && puck.getY() > maximumY) {

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

    private void applyPuckDamping(
            Puck puck,
            float deltaTime
    ) {
        float dampingFactor =
                (float) Math.exp(
                        -PUCK_DAMPING_PER_SECOND * deltaTime
                );

        float velocityX =
                puck.getVelocityX() * dampingFactor;

        float velocityY =
                puck.getVelocityY() * dampingFactor;

        float speedSquared =
                velocityX * velocityX
                        + velocityY * velocityY;

        if (speedSquared
                < PUCK_STOP_SPEED * PUCK_STOP_SPEED) {

            puck.setVelocity(
                    0f,
                    0f
            );

            return;
        }

        puck.setVelocity(
                velocityX,
                velocityY
        );
    }
}