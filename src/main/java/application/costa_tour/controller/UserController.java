package application.costa_tour.controller;

import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.dto.response.AuthResDTO;
import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.request.AuthReqDTO;
import application.costa_tour.dto.request.UpdateAvatarReqDTO;
import application.costa_tour.exception.*;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.model.Usuario;
import application.costa_tour.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private TuristaService turistaService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private AliadoService aliadoService;

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser (
            @RequestBody @Valid AuthReqDTO authReq,
            HttpServletResponse res
            ) {

        Pair<String, Usuario> tokenAndUser = usuarioService.credentialsValidate(authReq.getEmail(), authReq.getPassword());

        if (tokenAndUser != null) {
            res.addCookie(createJwtCookie(tokenAndUser.getFirst()));
        }

        if (tokenAndUser.getSecond().getTipo().equals(UserRole.TURISTA)) {
            return new ResponseEntity<>(
                    turistaService.getTuristaByUser(tokenAndUser.getSecond()),
                    HttpStatus.OK
            );
        }

        if (tokenAndUser.getSecond().getTipo().equals(UserRole.ADMINISTRADOR)) {
            return new ResponseEntity<>(
                    administradorService.getAdminByUser(tokenAndUser.getSecond()),
                    HttpStatus.OK
            );
        }

        if (tokenAndUser.getSecond().getTipo().equals(UserRole.ALIADO)) {
            return new ResponseEntity<>(
                    aliadoService.getAliadoByUser(tokenAndUser.getSecond()),
                    HttpStatus.OK
            );
        }

        throw new BadRequestException("Error in auth module");
    }

    @PutMapping("/update/avatar")
    public ResponseEntity<?> changeAvatar (
            @ModelAttribute @Valid UpdateAvatarReqDTO updateAvatarReq
            ) {

        Long userId = updateAvatarReq.getUserId();
        MultipartFile file = updateAvatarReq.getAvatar();

        if (!usuarioService.isExistsUser(userId)) {
            throw new ResourceNotFoundException("User not found to update avatar");
        }

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        String filename = storageService.saveAvatar(userId, file);

        usuarioService
            .updateUserAvatarPath(
                userId,
                filename);

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message("Avatar updated successfully")
                        .build(),
                HttpStatus.OK
        );
    }

    private Cookie createJwtCookie(String token) {
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true); // La cookie no es accesible mediante JavaScript
        jwtCookie.setSecure(false);   // Solo se envía a través de HTTPS
        jwtCookie.setPath("/");      // Hacerla disponible para toda la aplicación
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Expira en 7 días

        return jwtCookie;
    }
}
