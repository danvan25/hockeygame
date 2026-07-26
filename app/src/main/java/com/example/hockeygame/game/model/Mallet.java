package com.example.hockeygame.game.model;

public class Mallet {

    private float x;
    private float y;
    private float radius;

    public Mallet(float radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}