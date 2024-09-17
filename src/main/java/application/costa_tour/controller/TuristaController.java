package application.costa_tour.controller;

import application.costa_tour.dto.TuristaCreateDTO;
import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.mapper.TuristaCreateMapper;
import application.costa_tour.exception.AdminAlreadyExistException;
import application.costa_tour.exception.ClientAlredyExistException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.model.Turista;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.service.CiudadService;
import application.costa_tour.service.TuristaService;
import application.costa_tour.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turist")
public class TuristaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TuristaService turistaService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public ResponseEntity<?> turistByDni (@RequestParam String dni) {
        TuristaDTO turista = turistaService.getTuristaByDni(dni);
        return ResponseEntity.ok(turista);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTurist (@ModelAttribute @Valid TuristaCreateDTO turistaDTO) {

        if (turistaService.isExistingTurista(turistaDTO.getDni())) {
            throw new ClientAlredyExistException("Client already exist.");
        }

        if (usuarioService.isExitsAccountWithEmail(turistaDTO.getEmail())) {
            throw new AdminAlreadyExistException("An account associated with an email already exists");
        }

        if (!ciudadService.isExitsCity(turistaDTO.getIdCiudad())) {
            throw new ResourceNotFoundException("City not found to id=" + turistaDTO.getIdCiudad());
        }

        Turista turista = TuristaCreateMapper.mapper.turistaCreateDtoToTurista(turistaDTO);

        turista.getUsuario().setTipo(UserRole.TURISTA);
        usuarioService.createUser(turista.getUsuario());

        TuristaDTO turistaRes = turistaService.createTurista(turista);
        return new ResponseEntity<>(
                turistaRes,
                HttpStatus.CREATED
                );
    }
}
