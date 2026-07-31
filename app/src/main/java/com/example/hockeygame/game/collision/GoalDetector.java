package com.example.hockeygame.game.collision;

import com.example.hockeygame.game.model.Puck;

public class GoalDetector {

    public GoalResult detectGoal(
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

        boolean puckInsideGoalWidth =
                puck.getX() >= goalLeft
                        && puck.getX() <= goalRight;

        if (!puckInsideGoalWidth) {
            return GoalResult.NONE;
        }

        float puckTop =
                puck.getY() - puck.getRadius();

        float puckBottom =
                puck.getY() + puck.getRadius();

        float topGoalLine =
                fieldMargin;

        float bottomGoalLine =
                fieldHeight - fieldMargin;

        if (puckBottom < topGoalLine) {
            return GoalResult.TOP_GOAL;
        }

        if (puckTop > bottomGoalLine) {
            return GoalResult.BOTTOM_GOAL;
        }

        return GoalResult.NONE;
    }
}