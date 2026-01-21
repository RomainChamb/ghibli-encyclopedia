package fr.barbebroux.ghibliencyclopedia.controller;

import fr.barbebroux.ghibliencyclopedia.controller.dto.MovieDTO;
import fr.barbebroux.ghibliencyclopedia.controller.dto.MovieWithReviewsDTO;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewDTO;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewRequest;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewStatistics;
import fr.barbebroux.ghibliencyclopedia.repository.ReviewRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MovieApiController {

    @Value("${movie.api.host}")
    private String movieApiHost;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ReviewRepository reviewRepository;

    public MovieApiController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/movies")
    public ResponseEntity<Object> getMovies() {
        String url = movieApiHost + "/films/" ;
        Object response = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable("id") String id,
                                          @RequestParam(defaultValue = "false") boolean includeReviews) {
      try {
        String url = movieApiHost + "/films/" + id;
        MovieDTO movie = restTemplate.getForObject(url, MovieDTO.class);

        if (includeReviews) {
            List<ReviewDTO> reviews = reviewRepository.findByMovieId(id);
            ReviewStatistics statistics = calculateReviewStatistics(reviews);
            MovieWithReviewsDTO response = new MovieWithReviewsDTO(movie, reviews, statistics);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(movie);
        }
      } catch (HttpClientErrorException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body("Movie not found");
      }
    }

    @PostMapping(value = "/movies/{id}/reviews", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable("id") String id, 
                                               @Validated @RequestBody ReviewRequest request) {
        // Validate movie exists
        MovieDTO movie = fetchMovieById(id);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Ensure movieId in request matches path variable
        if (!id.equals(request.getMovieId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String reviewId = UUID.randomUUID().toString();
        ReviewDTO review = new ReviewDTO(reviewId, request.getMovieId(), request.getComment(),
                request.getScore(), LocalDateTime.now());

        ReviewDTO savedReview = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @GetMapping(value = "/movies/{id}/reviews/{reviewId}", produces = "application/json")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable("id") String id, 
                                             @PathVariable("reviewId") String reviewId) {
        Optional<ReviewDTO> review = reviewRepository.findById(reviewId);
        
        if (review.isPresent() && review.get().getMovieId().equals(id)) {
            return ResponseEntity.ok(review.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/movies/{id}/reviews", produces = "application/json")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovie(@PathVariable("id") String id) {
        // Validate movie exists
        MovieDTO movie = fetchMovieById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReviewDTO> reviews = reviewRepository.findByMovieId(id);
        return ResponseEntity.ok(reviews);
    }

    private ReviewStatistics calculateReviewStatistics(List<ReviewDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return new ReviewStatistics(null, null, null, 0);
        }

        double average = reviews.stream()
                .mapToInt(ReviewDTO::getScore)
                .average()
                .orElse(0.0);

        int min = reviews.stream()
                .mapToInt(ReviewDTO::getScore)
                .min()
                .orElse(0);

        int max = reviews.stream()
                .mapToInt(ReviewDTO::getScore)
                .max()
                .orElse(0);

        return new ReviewStatistics(average, min, max, reviews.size());
    }

    private MovieDTO fetchMovieById(String id) {
        String url = movieApiHost + "/films/" + id;
        try {
            return restTemplate.getForObject(url, MovieDTO.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch film " + id + ": " + e.getMessage());
            return null;
        }
    }
}
