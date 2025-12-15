package fr.barbebroux.ghibliencyclopedia.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FavoriteMovieDTO {

  private MovieDTO movie;
  private LocalDateTime addedDate;

  public FavoriteMovieDTO(MovieDTO movie, LocalDateTime date) {
    this.movie = movie;
    this.addedDate = date;
  }

  public MovieDTO getMovie() {
    return movie;
  }

  public void setMovie(MovieDTO movie) {
    this.movie = movie;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }
}
