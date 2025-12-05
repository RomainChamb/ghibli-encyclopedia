package fr.barbebroux.ghibliencyclopedia.controller;

import fr.barbebroux.ghibliencyclopedia.controller.dto.MovieDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class FavoritesController {

  private final JdbcTemplate jdbcTemplate;

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${movie.api.host}")
  private String movieApiHost;

  public FavoritesController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @PostMapping(value = "favorites", consumes = "application/json", produces = "application/json")
  public ResponseEntity<FavoriteResponseDTO> toggleFavorite(@Validated @RequestBody FavoriteRequestDto request) {
    String movieId = request.getId();

    String sqlSelect = "SELECT id from favorites where movie_id = ?";
    List<String> results = jdbcTemplate.queryForList(sqlSelect,String.class, movieId);

    if(results.isEmpty()) {
      LocalDate date = LocalDate.now();
      String sqlInsert = "INSERT INTO favorites (movie_id, date) VALUES (?, ?)";
      jdbcTemplate.update(sqlInsert, movieId, date);

      FavoriteResponseDTO response = new FavoriteResponseDTO(movieId, "Added to favorite", true);

      return ResponseEntity.status(201).body(response);
    } else {
      String sqlDelete = "DELETE FROM favorites where id = ?";
      jdbcTemplate.update(sqlDelete, results.get(0));

      FavoriteResponseDTO response = new FavoriteResponseDTO(movieId, "Removed from favorite", false);

      return ResponseEntity.status(200).body(response);
    }

  }

  @GetMapping(value="favorites", produces = "application/json")
  public ResponseEntity<List<MovieDTO>> getFavorites() {
    String sqlSelect = "SELECT MOVIE_ID from favorites";
    List<String> ids = jdbcTemplate.queryForList(sqlSelect, String.class);

    List<MovieDTO> favoriteMovies = ids.stream()
      .map(this::fetchMovieById)
      .filter(Objects::nonNull)
      .toList();

    return ResponseEntity.ok(favoriteMovies);
  }

  private MovieDTO fetchMovieById(String id) {
    String url = movieApiHost + "/films/" + id ;
    try {
      return restTemplate.getForObject(url, MovieDTO.class);
    } catch (Exception e) {
      System.err.println("Failed to fetch film " + id + ": " + e.getMessage());
      return null;
    }
  }

  private static class FavoriteRequestDto {
    @NotBlank
    private String id;

    public FavoriteRequestDto() {
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

  private static class FavoriteResponseDTO {
    private String movieId;
    private String message;
    private boolean added;

    public FavoriteResponseDTO(String movieId, String message, boolean added) {
      this.movieId = movieId;
      this.message = message;
      this.added = added;
    }

    public String getMovieId() {
      return movieId;
    }

    public String getMessage() {
      return message;
    }

    public boolean isAdded() {
      return added;
    }

    public void setMovieId(String movieId) {
      this.movieId = movieId;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public void setAdded(boolean added) {
      this.added = added;
    }
  }
}
