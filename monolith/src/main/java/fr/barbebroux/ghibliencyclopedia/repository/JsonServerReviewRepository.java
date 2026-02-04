package fr.barbebroux.ghibliencyclopedia.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.barbebroux.ghibliencyclopedia.controller.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonServerReviewRepository implements ReviewRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonServerReviewRepository.class);
    private static final String REVIEWS_ENDPOINT = "/reviews";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private final String jsonServerUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public JsonServerReviewRepository(@Value("${jsonserver.url:http://localhost:3001}") String jsonServerUrl,
                                     RestTemplate restTemplate) {
        this.jsonServerUrl = jsonServerUrl.endsWith("/") ? jsonServerUrl.substring(0, jsonServerUrl.length() - 1) : jsonServerUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public ReviewDTO save(ReviewDTO review) {
        try {
            String url = jsonServerUrl + REVIEWS_ENDPOINT;
            Map<String, Object> reviewMap = convertToMap(review);
            
            // Check if it's an update or create
            if (review.getReviewId() != null && !review.getReviewId().isEmpty()) {
                // Check if review exists first
                Optional<ReviewDTO> existingReview = findById(review.getReviewId());
                if (existingReview.isPresent()) {
                    // Update existing review using PATCH
                    url = jsonServerUrl + REVIEWS_ENDPOINT + "/" + review.getReviewId();
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reviewMap);
                    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
                    return convertFromMap(response.getBody());
                } else {
                    // Create new review with provided ID using POST
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reviewMap);
                    ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
                    return convertFromMap(response.getBody());
                }
            } else {
                // Create new review without ID
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reviewMap);
                ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
                return convertFromMap(response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error saving review: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save review", e);
        }
    }
    
    @Override
    public Optional<ReviewDTO> findById(String id) {
        try {
            String url = jsonServerUrl + REVIEWS_ENDPOINT + "/" + id;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(convertFromMap(response.getBody()));
            }
            return Optional.empty();
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding review by id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to find review by id", e);
        }
    }
    
    @Override
    public List<ReviewDTO> findAll() {
        try {
            String url = jsonServerUrl + REVIEWS_ENDPOINT;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getBody() != null) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                if (rootNode.isArray()) {
                    List<ReviewDTO> reviews = new ArrayList<>();
                    for (JsonNode node : rootNode) {
                        Map<String, Object> map = objectMapper.convertValue(node, Map.class);
                        reviews.add(convertFromMap(map));
                    }
                    return reviews;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error finding all reviews: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find all reviews", e);
        }
    }
    
    @Override
    public List<ReviewDTO> findByMovieId(String movieId) {
        try {
            String url = jsonServerUrl + REVIEWS_ENDPOINT + "?movieId=" + movieId;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getBody() != null) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                if (rootNode.isArray()) {
                    List<ReviewDTO> reviews = new ArrayList<>();
                    for (JsonNode node : rootNode) {
                        Map<String, Object> map = objectMapper.convertValue(node, Map.class);
                        reviews.add(convertFromMap(map));
                    }
                    return reviews;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error finding reviews by movieId {}: {}", movieId, e.getMessage(), e);
            throw new RuntimeException("Failed to find reviews by movieId", e);
        }
    }
    
    @Override
    public void deleteById(String id) {
        try {
            String url = jsonServerUrl + REVIEWS_ENDPOINT + "/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            logger.error("Error deleting review by id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete review by id", e);
        }
    }
    
    private Map<String, Object> convertToMap(ReviewDTO review) {
        return Map.of(
            "id", review.getReviewId() != null ? review.getReviewId() : "",
            "movieId", review.getMovieId(),
            "comment", review.getComment(),
            "score", review.getScore(),
            "createdAt", review.getCreatedAt().format(DATE_FORMATTER)
        );
    }
    
    private ReviewDTO convertFromMap(Map<String, Object> map) {
        String reviewId = (String) map.get("id");
        String movieId = (String) map.get("movieId");
        String comment = (String) map.get("comment");
        Integer score = (Integer) map.get("score");
        String createdAtStr = (String) map.get("createdAt");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, DATE_FORMATTER);
        
        return new ReviewDTO(reviewId, movieId, comment, score, createdAt);
    }
}