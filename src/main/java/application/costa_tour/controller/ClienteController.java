package application.costa_tour.controller;

import application.costa_tour.model.Cliente;
import application.costa_tour.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<?> clientByDni (@RequestParam String dni) {
        Cliente cliente = clienteService.getClientByDni(dni);
        return ResponseEntity.ok(cliente);
    }
}
