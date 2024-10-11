package application.costa_tour.controller;

import application.costa_tour.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characteristic")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCaracteristicas() {
        return ResponseEntity.ok(caracteristicaService.getAllCaracteristicas());
    }
}
