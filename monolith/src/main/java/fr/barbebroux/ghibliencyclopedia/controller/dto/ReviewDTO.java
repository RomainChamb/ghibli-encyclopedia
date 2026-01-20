package fr.barbebroux.ghibliencyclopedia.controller.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
  private final String reviewId;
  private final String movieId;
  private final String comment;
  private final Integer score;
  private final LocalDateTime createdAt;

  public ReviewDTO(String reviewId, String movieId, String comment, Integer score, LocalDateTime createdAt) {
    this.reviewId = reviewId;
    this.movieId = movieId;
    this.comment = comment;
    this.score = score;
    this.createdAt = createdAt;
  }
  
  public String getReviewId() { return reviewId; }
  public String getMovieId() { return movieId; }
  public String getComment() { return comment; }
  public Integer getScore() { return score; }
  public LocalDateTime getCreatedAt() { return createdAt; }
}
