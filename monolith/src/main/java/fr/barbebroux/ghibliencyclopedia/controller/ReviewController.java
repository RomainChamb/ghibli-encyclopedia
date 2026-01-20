package fr.barbebroux.ghibliencyclopedia.controller;

import fr.barbebroux.ghibliencyclopedia.controller.dto.MovieDTO;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewDTO;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewRequest;
import fr.barbebroux.ghibliencyclopedia.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate;

    @Value("${movie.api.host}")
    private String movieApiHost;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping(value = "/reviews", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReviewDTO> postReview(@Validated @RequestBody ReviewRequest request) {
        // Validate movie exists
        MovieDTO movie = fetchMovieById(request.getMovieId());
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        String reviewId = UUID.randomUUID().toString();
        ReviewDTO review = new ReviewDTO(reviewId, request.getMovieId(), request.getComment(),
                request.getScore(), LocalDateTime.now());

        ReviewDTO savedReview = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @GetMapping(value = "/reviews", produces = "application/json")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping(value = "/reviews/{id}", produces = "application/json")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable String id) {
        return reviewRepository.findById(id)
                .map(review -> ResponseEntity.ok(review))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/movies/{movieId}/reviews", produces = "application/json")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovie(@PathVariable String movieId) {
        // Validate movie exists
        MovieDTO movie = fetchMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReviewDTO> reviews = reviewRepository.findByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        if (!reviewRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
