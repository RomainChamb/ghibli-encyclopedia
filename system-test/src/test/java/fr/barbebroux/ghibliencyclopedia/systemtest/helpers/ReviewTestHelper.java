package fr.barbebroux.ghibliencyclopedia.systemtest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTestHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewTestHelper.class);
    private static final String JSON_SERVER_URL = "http://localhost:3001";
    private static final String REVIEWS_ENDPOINT = JSON_SERVER_URL + "/reviews";
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public ReviewTestHelper() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Clean up all reviews from the json-server database
     */
    public void cleanupAllReviews() {
        try {
            // Get all reviews first
            List<String> reviewIds = getAllReviewIds();
            
            // Delete each review
            for (String reviewId : reviewIds) {
                deleteReview(reviewId);
            }
            
            if (!reviewIds.isEmpty()) {
                logger.info("Cleaned up {} reviews from test database", reviewIds.size());
            }
        } catch (Exception e) {
            logger.warn("Failed to cleanup reviews: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Get all review IDs from the json-server
     */
    private List<String> getAllReviewIds() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(REVIEWS_ENDPOINT))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Failed to get reviews for cleanup");
        
        JsonNode rootNode = objectMapper.readTree(response.body());
        List<String> reviewIds = new ArrayList<>();
        
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                JsonNode idNode = node.get("id");
                if (idNode != null && !idNode.asText().isEmpty()) {
                    reviewIds.add(idNode.asText());
                }
            }
        }
        
        return reviewIds;
    }
    
    /**
     * Delete a specific review by ID
     */
    private void deleteReview(String reviewId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(REVIEWS_ENDPOINT + "/" + reviewId))
                .DELETE()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Accept both 200 (OK) and 404 (already deleted) as success
        if (response.statusCode() != 200 && response.statusCode() != 404) {
            logger.warn("Failed to delete review {}: HTTP {}", reviewId, response.statusCode());
        }
    }
    
    /**
     * Check if the json-server is available
     */
    public boolean isJsonServerAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(REVIEWS_ENDPOINT))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}