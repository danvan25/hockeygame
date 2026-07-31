package com.example.hockeygame.game.rules;

import com.example.hockeygame.game.model.Puck;
import com.example.hockeygame.game.model.Score;

public class GameRules {

    private final Score score;

    public GameRules(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }

    public void update(Puck puck) {


    }
}