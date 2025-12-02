package fr.barbebroux.ghibliencyclopedia.controller;

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

  public static class MovieDTO {
    private String id;
    private String title;
    private String original_title;
    private String original_title_romanised;
    private String description;
    private String director;
    private String producer;
    private String release_date;
    private String running_time;
    private String rt_score;
    private String[] people;
    private String[] species;
    private String[] locations;
    private String[] vehicles;
    private String url;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getOriginal_title() {
      return original_title;
    }

    public void setOriginal_title(String original_title) {
      this.original_title = original_title;
    }

    public String getOriginal_title_romanised() {
      return original_title_romanised;
    }

    public void setOriginal_title_romanised(String original_title_romanised) {
      this.original_title_romanised = original_title_romanised;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getDirector() {
      return director;
    }

    public void setDirector(String director) {
      this.director = director;
    }

    public String getProducer() {
      return producer;
    }

    public void setProducer(String producer) {
      this.producer = producer;
    }

    public String getRelease_date() {
      return release_date;
    }

    public void setRelease_date(String release_date) {
      this.release_date = release_date;
    }

    public String getRunning_time() {
      return running_time;
    }

    public void setRunning_time(String running_time) {
      this.running_time = running_time;
    }

    public String getRt_score() {
      return rt_score;
    }

    public void setRt_score(String rt_score) {
      this.rt_score = rt_score;
    }

    public String[] getPeople() {
      return people;
    }

    public void setPeople(String[] people) {
      this.people = people;
    }

    public String[] getSpecies() {
      return species;
    }

    public void setSpecies(String[] species) {
      this.species = species;
    }

    public String[] getLocations() {
      return locations;
    }

    public void setLocations(String[] locations) {
      this.locations = locations;
    }

    public String[] getVehicles() {
      return vehicles;
    }

    public void setVehicles(String[] vehicles) {
      this.vehicles = vehicles;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
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
