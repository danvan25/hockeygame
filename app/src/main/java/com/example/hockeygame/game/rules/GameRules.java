package com.example.hockeygame.game.rules;

import com.example.hockeygame.game.collision.GoalDetector;
import com.example.hockeygame.game.collision.GoalResult;
import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;

public class GameRules {

    private final Score score;
    private final GoalDetector goalDetector;

    public GameRules(Score score) {
        this.score = score;
        this.goalDetector = new GoalDetector();
    }

    public GoalResult update(
            Puck puck,
            float fieldWidth,
            float fieldHeight,
            float fieldMargin,
            float goalWidth
    ) {
        GoalResult goalResult =
                goalDetector.detectGoal(
                        puck,
                        fieldWidth,
                        fieldHeight,
                        fieldMargin,
                        goalWidth
                );

        if (goalResult == GoalResult.TOP_GOAL) {
            score.increaseOpponentScore();
        } else if (goalResult == GoalResult.BOTTOM_GOAL) {
            score.increaseHostScore();
        }

        return goalResult;
    }

    public Score getScore() {
        return score;
    }
}