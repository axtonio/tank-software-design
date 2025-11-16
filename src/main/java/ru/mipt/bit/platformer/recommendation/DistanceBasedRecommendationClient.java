package ru.mipt.bit.platformer.recommendation;

public class DistanceBasedRecommendationClient implements RecommendationEngineClient {
    private final int fireDistance;

    public DistanceBasedRecommendationClient() {
        this(2);
    }

    public DistanceBasedRecommendationClient(int fireDistance) {
        this.fireDistance = Math.max(1, fireDistance);
    }

    @Override
    public RecommendationAction getRecommendation(RecommendationRequest request) {
        int dx = request.getDeltaX();
        int dy = request.getDeltaY();
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        if ((absDx <= fireDistance && dy == 0) || (absDy <= fireDistance && dx == 0)) {
            return RecommendationAction.FIRE;
        }
        if (absDx >= absDy) {
            return dx > 0 ? RecommendationAction.MOVE_RIGHT : RecommendationAction.MOVE_LEFT;
        }
        return dy > 0 ? RecommendationAction.MOVE_UP : RecommendationAction.MOVE_DOWN;
    }
}

