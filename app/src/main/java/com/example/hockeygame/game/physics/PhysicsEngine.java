package com.example.hockeygame.game.physics;

import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Mallet;
import com.example.hockeygame.game.model.Puck;
public class PhysicsEngine {

    public void updatePuck(
            Puck puck,
            Mallet topMallet,
            Mallet bottomMallet,
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

        handleMalletCollision(puck, topMallet);
        handleMalletCollision(puck, bottomMallet);
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

        /*
         * Eltoljuk a korongot az ütő felületére,
         * hogy ne maradjon beleragadva.
         */
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

        /*
         * Ha a korong már távolodik az ütőtől,
         * nem fordítjuk meg még egyszer.
         */
        if (velocityAlongNormal >= 0f) {
            return;
        }

        float newVelocityX =
                puck.getVelocityX()
                        - 2f * velocityAlongNormal * normalX;

        float newVelocityY =
                puck.getVelocityY()
                        - 2f * velocityAlongNormal * normalY;

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