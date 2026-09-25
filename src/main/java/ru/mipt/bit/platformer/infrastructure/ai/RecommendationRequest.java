package ru.mipt.bit.platformer.infrastructure.ai;

public class RecommendationRequest {
    private final int deltaX;
    private final int deltaY;

    public RecommendationRequest(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
