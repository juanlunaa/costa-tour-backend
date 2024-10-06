package application.costa_tour.controller;

import application.costa_tour.dto.AdministradorCreateDTO;
import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.AdministradorUpdateDTO;
import application.costa_tour.dto.mapper.AdministradorCreateMapper;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.UnauthorizedException;
import application.costa_tour.exception.UserAlreadyExistException;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.jwt.JwtService;
import application.costa_tour.model.Administrador;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.service.AdministradorService;
import application.costa_tour.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> createBasicAdmin (@RequestBody @Valid AdministradorCreateDTO adminDto) {

        if (usuarioService.isExitsAccountWithEmail(adminDto.getEmail())) {
            throw new UserAlreadyExistException("An account associated with an email already exists");
        }

        Administrador admin = AdministradorCreateMapper.mapper.administradorCreateDtoToAdministrador(adminDto);

        admin.getUsuario().setTipo(UserRole.ADMINISTRADOR);
        usuarioService.createUser(admin.getUsuario());

        administradorService.createAdmin(admin);

        return new ResponseEntity<>(
                SuccessResponse
                        .builder()
                        .message("Administrator created successfully")
                        .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid AdministradorUpdateDTO administradorDTO,
            HttpServletRequest req
    ) {
        if (!administradorService.isExistingAdminByUserId(userId)) {
            throw new ResourceNotFoundException(String
                    .format("Admin not found for user id=%s", userId));
        }

        String token = jwtService.getTokenFromReq(req);

        String email = jwtService.getEmailFromToken(token);

        if (!usuarioService.macthEmailToken(userId, email)) throw new UnauthorizedException("Account not authorized");

        AdministradorDTO adminUpdated = administradorService.updateAdmin(userId, administradorDTO);

        return new ResponseEntity<>(
                adminUpdated,
                HttpStatus.OK
        );
    }
}
