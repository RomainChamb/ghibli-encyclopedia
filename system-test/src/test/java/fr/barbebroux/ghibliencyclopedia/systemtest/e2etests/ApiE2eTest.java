package fr.barbebroux.ghibliencyclopedia.systemtest.e2etests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

class ApiE2eTest {

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
    }
}