package application.costa_tour.controller;

import application.costa_tour.dto.AdministradorDTO;
import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.dto.response.AuthResDTO;
import application.costa_tour.dto.TuristaDTO;
import application.costa_tour.dto.request.AuthReqDTO;
import application.costa_tour.dto.request.UpdateAvatarReqDTO;
import application.costa_tour.exception.*;
import application.costa_tour.jwt.JwtService;
import application.costa_tour.model.enums.UserRole;
import application.costa_tour.model.Usuario;
import application.costa_tour.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser (
            @RequestBody @Valid AuthReqDTO authReq,
            HttpServletResponse res
            ) {

        Pair<String, Usuario> tokenAndUser = usuarioService.credentialsValidate(authReq.getEmail(), authReq.getPassword());

        if (tokenAndUser != null) {
            res.addCookie(createJwtCookie(tokenAndUser.getFirst(), 1000 * 60 * 60));

            Object fullUser = getUsertypeByUser(tokenAndUser.getSecond());

            if (fullUser == null) throw new BadRequestException(String.format("Full user not found by user id=", tokenAndUser.getSecond().getId()));

            return new ResponseEntity<>(
                    fullUser,
                    HttpStatus.OK
            );
        }

//        if (tokenAndUser.getSecond().getTipo().equals(UserRole.TURISTA)) {
//            return new ResponseEntity<>(
//                    turistaService.getTuristaByUser(tokenAndUser.getSecond()),
//                    HttpStatus.OK
//            );
//        }
//
//        if (tokenAndUser.getSecond().getTipo().equals(UserRole.ADMINISTRADOR)) {
//            return new ResponseEntity<>(
//                    administradorService.getAdminByUser(tokenAndUser.getSecond()),
//                    HttpStatus.OK
//            );
//        }
//
//        if (tokenAndUser.getSecond().getTipo().equals(UserRole.ALIADO)) {
//            return new ResponseEntity<>(
//                    aliadoService.getAliadoByUser(tokenAndUser.getSecond()),
//                    HttpStatus.OK
//            );
//        }

        throw new BadRequestException("Error in auth module");
    }

//  Metodo para obtener la informacion de un usuario con un JWT valido
    @GetMapping("/profile")
    public ResponseEntity<?> getProfileUser(HttpServletRequest req) {
        String token = jwtService.getTokenFromReq(req);

        String email = jwtService.getEmailFromToken(token);

        Usuario user = usuarioService.getUserByEmail(email);

        Object fullUser = getUsertypeByUser(user);
        if (fullUser == null) throw new BadRequestException(String.format("Full user not found by user id=", user.getId()));

        return new ResponseEntity<>(
                fullUser,
                HttpStatus.OK
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest req, HttpServletResponse res) {
        String token = jwtService.getTokenFromReq(req);

//      Se verifica si el token es valido
        jwtService.getEmailFromToken(token);

//      Se reemplaza la cookie anterior con esta que tiene el token en null y el tiempo
//      de expiracion en 0 para que se elimine del cliente
        res.addCookie(createJwtCookie(null, 0));

        return new ResponseEntity<>(
                SuccessResponse.builder().message("Logout succesfully").build(),
                HttpStatus.OK
        );
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

    private Cookie createJwtCookie(String token, int expirationTime) {
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true); // La cookie no es accesible mediante JavaScript
        jwtCookie.setSecure(false);   // Solo se envía a través de HTTPS
        jwtCookie.setPath("/");      // Hacerla disponible para toda la aplicación
        jwtCookie.setMaxAge(expirationTime); // Expira en 7 días
        return jwtCookie;
    }

    private Object getUsertypeByUser(Usuario user) {
        if (user.getTipo().equals(UserRole.TURISTA)) {
            return turistaService.getTuristaByUser(user);
        }

        if (user.getTipo().equals(UserRole.ADMINISTRADOR)) {
            return administradorService.getAdminByUser(user);
        }

        if (user.getTipo().equals(UserRole.ALIADO)) {
            return aliadoService.getAliadoByUser(user);
        }

        return null;
    }
}
