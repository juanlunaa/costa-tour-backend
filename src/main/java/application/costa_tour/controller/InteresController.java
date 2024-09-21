package application.costa_tour.controller;

import application.costa_tour.service.InteresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interest")
public class InteresController {

    @Autowired
    private InteresService interesService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllIntereses() {
        return ResponseEntity.ok(interesService.getAllIntereses());
    }
}
