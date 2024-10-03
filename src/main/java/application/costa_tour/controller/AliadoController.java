package application.costa_tour.controller;

import application.costa_tour.dto.AliadoCreateDTO;
import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.dto.mapper.AliadoCreateMapper;
import application.costa_tour.exception.UserAlreadyExistException;
import application.costa_tour.model.Aliado;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.service.AliadoService;
import application.costa_tour.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ally")
public class AliadoController {

    @Autowired
    private AliadoService aliadoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping()
    public ResponseEntity<?> getAliado(@RequestParam String nitAliado) {
        return ResponseEntity.ok(aliadoService.getAliadoByNit(nitAliado));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAliado(@ModelAttribute @Valid AliadoCreateDTO aliadoDto) {

        if (usuarioService.isExitsAccountWithEmail(aliadoDto.getEmail())) {
            throw new UserAlreadyExistException("An account associated with an email already exists");
        }

        Aliado aliado = AliadoCreateMapper.mapper.aliadoCreateDtoToAliado(aliadoDto);

        aliado.getUsuario().setTipo(UserRole.ALIADO);
        usuarioService.createUser(aliado.getUsuario());

        AliadoDTO aliadoRes = aliadoService.createAliado(aliado);

        return new ResponseEntity<>(
                aliadoRes,
                HttpStatus.CREATED
                );
    }
}
