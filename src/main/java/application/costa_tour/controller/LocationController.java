package application.costa_tour.controller;

import application.costa_tour.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/country/all")
    public ResponseEntity<?> getAllPaises() {
        return ResponseEntity.ok(locationService.getAllPaises());
    }

    @GetMapping("/states/{idPais}")
    public ResponseEntity<?> getEstadosByPais(@PathVariable("idPais") Long idPais) {
        return ResponseEntity.ok(locationService.getAllEstadosByPais(idPais));
    }

    @GetMapping("/cities/{idEstado}")
    public ResponseEntity<?> getCiudadesByEstado(@PathVariable("idEstado") Long idEstado) {
        return ResponseEntity.ok(locationService.getAllCiudadesByEstado(idEstado));
    }
}
