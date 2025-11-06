package fr.barbebroux.ghibliencyclopedia.monolith.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards non-API routes to the Angular index.html so the SPA can handle routing.
 */
@Controller
public class FrontendController {

    // Root path
    @GetMapping({"/"})
    public String root() {
        return "forward:/index.html";
    }
}
