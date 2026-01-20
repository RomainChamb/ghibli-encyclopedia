package fr.barbebroux.ghibliencyclopedia.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {

  @NotBlank(message = "Movie ID is required")
  private String movieId;

  @NotBlank(message = "Comment is required")
  @Size(max = 1000, message = "Comment must be less than 1000 characters")
  private String comment;

  @NotNull(message = "Score is required")
  @Min(value = 1, message = "Score must be between 1 and 5")
  @Max(value = 5, message = "Score must be between 1 and 5")
  private Integer score;

  public String getMovieId() {
    return movieId;
  }

  public String getComment() {
    return comment;
  }

  public Integer getScore() {
    return score;
  }

  public void setMovieId(String movieId) {
    this.movieId = movieId;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setScore(Integer score) {
    this.score = score;
  }
}
