package application.costa_tour.controller;

import application.costa_tour.dto.TuristaCreateDTO;
import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.TuristaUpdateDTO;
import application.costa_tour.dto.mapper.TuristaCreateMapper;
import application.costa_tour.dto.mapper.TuristaUpdateMapper;
import application.costa_tour.exception.*;
import application.costa_tour.jwt.JwtService;
import application.costa_tour.model.Interes;
import application.costa_tour.model.Turista;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/turist")
@CrossOrigin(origins = "http://localhost:3000")
public class TuristaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TuristaService turistaService;

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private PlanService planService;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private InteresService interesService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<?> turistByDni (@RequestParam String dni) {
        TuristaDTO turista = turistaService.getTuristaByDni(dni);
        return ResponseEntity.ok(turista);
    }

    @GetMapping("/validate-dni")
    public ResponseEntity<?>  validateDni(@RequestParam("dni") String dni) {
        if (turistaService.isExistingTurista(dni)) {
            throw new ClientAlredyExistException("Turist already exist");
        }

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message("Dni is available")
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTurist (@RequestBody @Valid TuristaCreateDTO turistaDTO) {

        if (turistaService.isExistingTurista(turistaDTO.getDni())) {
            throw new ClientAlredyExistException("Turist already exist.");
        }

        if (usuarioService.isExitsAccountWithEmail(turistaDTO.getEmail())) {
            throw new UserAlreadyExistException("An account associated with an email already exists");
        }

        if (!ciudadService.isExitsCity(turistaDTO.getIdCiudad())) {
            throw new ResourceNotFoundException("City not found to id=" + turistaDTO.getIdCiudad());
        }

        interesService.isExistsIntereses(turistaDTO.getIntereses());

        Turista turista = TuristaCreateMapper.mapper.turistaCreateDtoToTurista(turistaDTO);

        turista.getUsuario().setTipo(UserRole.TURISTA);
        turista.addIntereses(turistaDTO.getIntereses().stream()
                .map(it -> new Interes(it))
                .collect(Collectors.toList())
        );

        usuarioService.createUser(turista.getUsuario());

        TuristaDTO turistaRes = turistaService.createTurista(turista);
        return new ResponseEntity<>(
                turistaRes,
                HttpStatus.CREATED
                );
    }

    @PutMapping("/update/{dni}")
    public ResponseEntity<?> updateTurist(
            @PathVariable("dni") String dni,
            @RequestBody @Valid TuristaUpdateDTO turistaDTO,
            HttpServletRequest req
    ) {

        if (!turistaService.isExistingTurista(dni)) {
            throw new ResourceNotFoundException(String
                    .format("Turist not found for dni=%s", dni));
        }

        if (!ciudadService.isExitsCity(turistaDTO.getIdCiudad())) {
            throw new ResourceNotFoundException(String
                    .format("City not found to id==%s", turistaDTO.getIdCiudad()));
        }

        String token = jwtService.getTokenFromReq(req);

        String email = jwtService.getEmailFromToken(token);

//      Si el usuario tiene un token que no corresponde a la cuenta que quiere editar se lanzara esta excepcion
        if (!turistaService.macthEmailToken(dni, email)) throw new UnauthorizedException("Account not authorized");

        interesService.isExistsIntereses(turistaDTO.getIntereses());

        List<Interes> intereses = turistaDTO.getIntereses().stream()
                .map(i -> new Interes(i))
                .collect(Collectors.toList());

        Turista turista = TuristaUpdateMapper.mapper.turistaUpdateDtoToTurista(turistaDTO);

        turista.setCiudad(ciudadService.getCiudadById(turistaDTO.getIdCiudad()));

        TuristaDTO turitaUpdated = turistaService.updateTurista(dni, turista, intereses);

        return new ResponseEntity<>(
                turitaUpdated,
                HttpStatus.OK
        );
    }

    @PostMapping("/favorite/toggle")
    public ResponseEntity<?> toggleFavorito(@RequestParam String dni, @RequestParam Long planId) {
        if (!planService.isPlanExists(planId)) {
            throw new ResourceNotFoundException(String
                    .format("Plan not found for id=%s", planId));
        }

        String action = turistaService.toggleFavorite(
                dni,
                planId,
                favoritoService.getFavoritoByTuristaAndPlan(dni, planId)
        );

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message(String.
                                format("Favorite plan %s successfully", action))
                        .build(),
                HttpStatus.OK
        );
    }
}
