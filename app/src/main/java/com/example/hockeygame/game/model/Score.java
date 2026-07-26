package com.example.hockeygame.game.model;

public class Score {

    private int hostScore;
    private int opponentScore;

    public int getHostScore() {
        return hostScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void increaseHostScore() {
        hostScore++;
    }

    public void increaseOpponentScore() {
        opponentScore++;
    }

    public void reset() {
        hostScore = 0;
        opponentScore = 0;
    }
}