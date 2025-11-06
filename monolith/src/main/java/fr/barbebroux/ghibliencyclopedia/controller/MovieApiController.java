package fr.barbebroux.ghibliencyclopedia.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
