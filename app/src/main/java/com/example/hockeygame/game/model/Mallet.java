package com.example.hockeygame.game.model;

public class Mallet {

    private float x;
    private float y;
    private float radius;
    private float velocityX;
    private float velocityY;

    public Mallet(float radius) {
        this.radius = radius;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
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