package fr.barbebroux.ghibliencyclopedia.repository;

import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewDTO;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    ReviewDTO save(ReviewDTO review);
    Optional<ReviewDTO> findById(String id);
    List<ReviewDTO> findAll();
    List<ReviewDTO> findByMovieId(String movieId);
    void deleteById(String id);
}