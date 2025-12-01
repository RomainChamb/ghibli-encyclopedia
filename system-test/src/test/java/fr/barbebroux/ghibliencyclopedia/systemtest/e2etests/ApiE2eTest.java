package fr.barbebroux.ghibliencyclopedia.systemtest.e2etests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}