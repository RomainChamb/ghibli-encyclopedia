package fr.barbebroux.ghibliencyclopedia.controller.dto;

import java.util.List;

public class MovieWithReviewsDTO extends MovieDTO {
    private final List<ReviewDTO> reviews;
    private final ReviewStatistics reviewStatistics;

    public MovieWithReviewsDTO(MovieDTO movie, List<ReviewDTO> reviews, ReviewStatistics reviewStatistics) {
        // Copy all fields from MovieDTO
        this.setId(movie.getId());
        this.setTitle(movie.getTitle());
        this.setOriginal_title(movie.getOriginal_title());
        this.setOriginal_title_romanised(movie.getOriginal_title_romanised());
        this.setDescription(movie.getDescription());
        this.setDirector(movie.getDirector());
        this.setProducer(movie.getProducer());
        this.setRelease_date(movie.getRelease_date());
        this.setRunning_time(movie.getRunning_time());
        this.setRt_score(movie.getRt_score());
        this.setPeople(movie.getPeople());
        this.setSpecies(movie.getSpecies());
        this.setLocations(movie.getLocations());
        this.setVehicles(movie.getVehicles());
        this.setUrl(movie.getUrl());
        
        this.reviews = reviews;
        this.reviewStatistics = reviewStatistics;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public ReviewStatistics getReviewStatistics() {
        return reviewStatistics;
    }
}