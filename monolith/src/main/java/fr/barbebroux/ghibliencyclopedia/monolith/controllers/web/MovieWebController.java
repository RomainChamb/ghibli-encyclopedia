package fr.barbebroux.ghibliencyclopedia.monolith.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieWebController {

    @GetMapping("/movies")
    public String todos() {
        return "movies.html";
    }
}