package application.costa_tour.controller;

import application.costa_tour.dto.AdministradorCreateDTO;
import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.mapper.AdministradorCreateMapper;
import application.costa_tour.exception.AdminAlreadyExistException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.model.AdminRole;
import application.costa_tour.model.Administrador;
import application.costa_tour.model.UserRole;
import application.costa_tour.service.AdministradorService;
import application.costa_tour.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/create")
    public ResponseEntity<?> createBasicAdmin (@ModelAttribute @Valid AdministradorCreateDTO adminDto) {

        if (!(adminDto.getRol().equals(AdminRole.ADMIN) || adminDto.getRol().equals(AdminRole.SUPERADMIN))) {
            throw new ResourceNotFoundException("Admin role not found to adminRole=" + adminDto.getRol());
        }

        if (usuarioService.isExitsAccountWithEmail(adminDto.getEmail())) {
            throw new AdminAlreadyExistException("An account associated with an email already exists");
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
                HttpStatus.OK);
    }
}
