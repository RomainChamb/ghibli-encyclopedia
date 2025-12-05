package fr.barbebroux.ghibliencyclopedia.controller;

import fr.barbebroux.ghibliencyclopedia.controller.dto.MovieDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class MovieApiController {

    @Value("${movie.api.host}")
    private String movieApiHost;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/movies")
    public ResponseEntity<Object> getMovies() {
        String url = movieApiHost + "/films/" ;
        Object response = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable("id") String id) {
      try {
        String url = movieApiHost + "/films/" + id;
        MovieDTO response = restTemplate.getForObject(url, MovieDTO.class);

        return ResponseEntity.ok(response);
      } catch (HttpClientErrorException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body("Movie not found");
      }
    }
}
