package fr.barbebroux.ghibliencyclopedia.controller.dto;

public class ReviewStatistics {
    private final Double averageScore;
    private final Integer minScore;
    private final Integer maxScore;
    private final Integer totalReviews;

    public ReviewStatistics(Double averageScore, Integer minScore, Integer maxScore, Integer totalReviews) {
        this.averageScore = averageScore;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.totalReviews = totalReviews;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }
}