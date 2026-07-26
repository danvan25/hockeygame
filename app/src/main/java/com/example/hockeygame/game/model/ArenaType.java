package com.example.hockeygame.game.model;


public enum ArenaType {

    ARCTIC("Arctic Arena"),
    NEON("Neon Arena"),
    CLASSIC("Classic Arena");

    private final String displayName;

    ArenaType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}