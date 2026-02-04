package fr.barbebroux.ghibliencyclopedia.systemtest.e2etests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.barbebroux.ghibliencyclopedia.systemtest.helpers.ReviewTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ApiE2eTest {

    private ReviewTestHelper reviewTestHelper;

    @BeforeEach
    void setUp() {
        reviewTestHelper = new ReviewTestHelper();
        // Skip review tests if json-server is not available
        assumeTrue(reviewTestHelper.isJsonServerAvailable(), 
                  "json-server is not available, skipping review tests");
    }

    @AfterEach
    void tearDown() {
        if (reviewTestHelper != null) {
            reviewTestHelper.cleanupAllReviews();
        }
    }

    @Test
    void getMovies_shouldReturnMovieWithExpectedFormat() throws Exception {
        // DISCLAIMER: This is an example of a badly written test
        // which unfortunately simulates real-life software test projects.
        // This is the starting point for our ATDD Accelerator exercises.

        // Arrange
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://ghibliapi.vercel.app/films"))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        
        String responseBody = response.body();
        
        // Verify JSON structure contains expected fields
        assertThat(responseBody)
                .as("Response should contain id field")
                .contains("\"id\"");

        assertThat(responseBody)
                .as("Response should contain original title field")
                .contains("\"original_title\"");

        assertThat(responseBody)
                .as("Response should contain director field")
                .contains("\"director\"");
        
        // Verify the result size
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode moviesArray = objectMapper.readTree(responseBody);
        assertThat(moviesArray.isArray()).as("Response should be a JSON array").isTrue();
        assertThat(moviesArray.size())
                .as("Response should contain 22 movies")
                .isEqualTo(22);
    }

    @ParameterizedTest
    @CsvSource({
            "2baf70d1-42bb-4437-b551-e5fed5a87abe, Castle in the Sky",
            "12cfb892-aac0-4c5b-94af-521852e46d6a, Grave of the Fireflies"
    })
    void getMovieById_shouldReturnTheMovie(String movieId, String expectedTitle) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:8080/api/movies/%s", movieId)))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertThat(response.statusCode()).isEqualTo(200);

        String responseBody = response.body();

        // Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);

        // Assert exact fields
        assertThat(json.get("id").asText()).as("Movie id should match").isEqualTo(movieId);

        assertThat(json.get("title").asText()).as("Original title should match").isEqualTo(expectedTitle);
    }

    @Test
    void getMovieById_shouldReturnErrorIfIdIsNotFound() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:8080/api/movies/%s", 1)))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
                .as("Response for unknown ID should be 404 or 400")
                .withFailMessage("Movie not found")
                .isIn(400, 404);
    }

    @Test
    void toggleFavorite_shouldReturnErrorIfIdIsNull() throws Exception {
        String nullIdJson = "{\"id\": null}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/favorites"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(nullIdJson))
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
                .as("Response for unknown ID should be 404 or 400")
                .isIn(400, 404);
    }

    @Test
    void toggleFavorite_shouldReturnErrorIfBodyTypeIsInvalid() throws Exception {
        String invalidTypeId = "{\"id\": \"Castle in the sky\"}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/favorites"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidTypeId))
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
                .as("Response for invalid type should be 400")
                .isEqualTo(400);
    }

    @Test
    void getFavoriteShouldReturnDateYYYY_MM_DD() throws Exception {
        String uuid = "12cfb892-aac0-4c5b-94af-521852e46d6a";
        String movieId = String.format("{\"id\": \"%s\"}", uuid);

        HttpClient client = HttpClient.newHttpClient();

        // POST favorite
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/favorites"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(movieId))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
                .as("Favorite has been saved")
                .isIn(200, 201);

        // GET favorites
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/favorites"))
                .GET()
                .build();

        HttpResponse<String> getResponse =
                client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode())
                .as("Favorites have been found")
                .isEqualTo(200);

        String body = getResponse.body();

        // Assert movie id
        assertThat(body).contains(uuid);

        // Assert date (only YYYY-MM-DD)
        String today = LocalDate.now().toString();
        assertThat(body).contains(today);
    }

    @Test
    void getReview_shouldReturnCommentAndScore() throws Exception {
        // Test constants
        String movieId = "758bf02e-3122-46e0-884e-67cf83df1786";
        String testComment = "Amazing movie with beautiful animation!";
        Integer testScore = 5;

        HttpClient client = HttpClient.newHttpClient();

        // POST review
        String reviewRequestJson = String.format(
            "{\"movieId\": \"%s\", \"comment\": \"%s\", \"score\": %d}", 
            movieId, testComment, testScore
        );

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(reviewRequestJson))
            .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(postResponse.statusCode())
            .as("Review should be created successfully")
            .isEqualTo(201);

        // Extract reviewId
        ObjectMapper postMapper = new ObjectMapper();
        JsonNode postResponseJson = postMapper.readTree(postResponse.body());
        String reviewId = postResponseJson.get("reviewId").asText();

        // Verify reviewId is not empty
        assertThat(reviewId)
            .as("Review ID should be generated")
            .isNotEmpty();

        // GET review
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews/" + reviewId))
            .GET()
            .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode())
            .as("Review should be retrieved successfully")
            .isEqualTo(200);

        // Assert equality on comment and score
        ObjectMapper getMapper = new ObjectMapper();
        JsonNode getResponseJson = getMapper.readTree(getResponse.body());

        assertThat(getResponseJson.get("comment").asText())
            .as("Review comment should match exactly")
            .isEqualTo(testComment);

        assertThat(getResponseJson.get("score").asInt())
            .as("Review score should match exactly")
            .isEqualTo(testScore);

        // Verify movieId matches
        assertThat(getResponseJson.get("movieId").asText())
            .as("Movie ID should match exactly")
            .isEqualTo(movieId);

        // Verify createdAt is present and valid
        assertThat(getResponseJson.get("createdAt"))
            .as("createdAt should be present")
            .isNotNull();
    }

    @Test
    void getReviewsByMovie_shouldReturnAllReviewsForMovie() throws Exception {
        String movieId = "758bf02e-3122-46e0-884e-67cf83df1786";
        HttpClient client = HttpClient.newHttpClient();

        // Create multiple reviews for the same movie
        for (int i = 1; i <= 3; i++) {
            String reviewRequestJson = String.format(
                "{\"movieId\": \"%s\", \"comment\": \"Review %d\", \"score\": %d}", 
                movieId, i, i
            );

            HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reviewRequestJson))
                .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertThat(postResponse.statusCode()).isEqualTo(201);
        }

        // Get all reviews for the movie
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews"))
            .GET()
            .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode())
            .as("Reviews should be retrieved successfully")
            .isEqualTo(200);

        // Parse response and verify we have at least 3 reviews
        ObjectMapper mapper = new ObjectMapper();
        JsonNode reviewsArray = mapper.readTree(getResponse.body());
        
        assertThat(reviewsArray.isArray())
            .as("Response should be an array")
            .isTrue();
            
        assertThat(reviewsArray.size())
            .as("Should have at least 3 reviews")
            .isGreaterThanOrEqualTo(3);
    }

    @Test
    void createReview_shouldReturnErrorForInvalidScore() throws Exception {
        String movieId = "758bf02e-3122-46e0-884e-67cf83df1786";
        HttpClient client = HttpClient.newHttpClient();

        // Try to create a review with invalid score (6, which is > 5)
        String invalidReviewJson = String.format(
            "{\"movieId\": \"%s\", \"comment\": \"Great movie\", \"score\": 6}", 
            movieId
        );

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(invalidReviewJson))
            .build();

        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
            .as("Should return bad request for invalid score")
            .isEqualTo(400);
    }

    @Test
    void createReview_shouldReturnErrorForMissingMovieId() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Try to create a review without movieId
        String invalidReviewJson = "{\"comment\": \"Great movie\", \"score\": 5}";

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/nonexistent/reviews"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(invalidReviewJson))
            .build();

        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode())
            .as("Should return bad request for missing movieId")
            .isIn(400, 404);
    }

    @Test
    void getMovieWithReviews_shouldIncludeStatistics() throws Exception {
        String movieId = "758bf02e-3122-46e0-884e-67cf83df1786";
        HttpClient client = HttpClient.newHttpClient();

        // Create a review
        String reviewRequestJson = String.format(
            "{\"movieId\": \"%s\", \"comment\": \"Excellent movie!\", \"score\": 5}", 
            movieId
        );

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "/reviews"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(reviewRequestJson))
            .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        assertThat(postResponse.statusCode()).isEqualTo(201);

        // Get movie with reviews and statistics
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/movies/" + movieId + "?includeReviews=true"))
            .GET()
            .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode()).isEqualTo(200);

        // Parse response and verify statistics are present
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(getResponse.body());
        
        assertThat(responseJson.get("reviews"))
            .as("Reviews should be included")
            .isNotNull();
            
        assertThat(responseJson.get("reviewStatistics"))
            .as("Statistics should be included")
            .isNotNull();
            
        JsonNode stats = responseJson.get("reviewStatistics");
        assertThat(stats.get("averageScore").asDouble())
            .as("Average score should be 5.0")
            .isEqualTo(5.0);
            
        assertThat(stats.get("totalReviews").asInt())
            .as("Total reviews should be 1")
            .isEqualTo(1);
    }
}