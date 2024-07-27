package application.costa_tour.controller;

import application.costa_tour.dto.ClienteCreateDTO;
import application.costa_tour.dto.ClienteDTO;
import application.costa_tour.model.Cliente;
import application.costa_tour.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<?> clientByDni (@RequestParam String dni) {
        ClienteDTO cliente = clienteService.getClientByDni(dni);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClient (@ModelAttribute ClienteCreateDTO cliente) {
        clienteService.createClient(cliente);
        return ResponseEntity.ok("Cliente creado correctamente");
    }

    @PostMapping("/update/avatar")
    public ResponseEntity<?> changeAvatar (
            @RequestParam("dni") String dniCliente,
            @RequestParam("avatar") MultipartFile file
    ) {
        clienteService.uploadAvatar(dniCliente, file);
        return ResponseEntity.ok("Imagen actulizada correctamente");
    }
}
