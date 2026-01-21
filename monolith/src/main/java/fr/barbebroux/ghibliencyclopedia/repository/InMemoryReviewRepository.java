package fr.barbebroux.ghibliencyclopedia.repository;

import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryReviewRepository implements ReviewRepository {
    
    private final Map<String, ReviewDTO> reviews = new ConcurrentHashMap<>();
    
    @Override
    public ReviewDTO save(ReviewDTO review) {
        String id = review.getReviewId() != null ? review.getReviewId() : UUID.randomUUID().toString();
        ReviewDTO reviewToSave = new ReviewDTO(id, review.getMovieId(), review.getComment(), 
                                              review.getScore(), review.getCreatedAt());
        reviews.put(id, reviewToSave);
        return reviewToSave;
    }
    
    @Override
    public Optional<ReviewDTO> findById(String id) {
        return Optional.ofNullable(reviews.get(id));
    }
    
    @Override
    public List<ReviewDTO> findAll() {
        return List.copyOf(reviews.values());
    }
    
    @Override
    public List<ReviewDTO> findByMovieId(String movieId) {
        return reviews.values().stream()
                .filter(review -> review.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String id) {
        reviews.remove(id);
    }
}