package application.costa_tour.controller;

import application.costa_tour.dto.AliadoDTO;
import application.costa_tour.dto.ChangePasswordDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
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
    private SuscripcionService suscripcionService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser (
            @RequestBody @Valid AuthReqDTO authReq,
            HttpServletResponse res
            ) {

        Pair<String, Usuario> tokenAndUser = usuarioService.credentialsValidate(authReq.getEmail(), authReq.getPassword());

        if (tokenAndUser != null) {
            res.addCookie(createJwtCookie(tokenAndUser.getFirst(), 3600));

            Object fullUser = getUsertypeByUser(tokenAndUser.getSecond());

            if (fullUser == null) throw new BadRequestException(String.format("Full user not found by user id=", tokenAndUser.getSecond().getId()));

            return new ResponseEntity<>(
                    fullUser,
                    HttpStatus.OK
            );
        }
        
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
                SuccessResponse.builder()
                        .message("Logout succesfully")
                        .build(),
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

        String fullpathAvatar = usuarioService
            .updateUserAvatarPath(
                userId,
                filename);

        return new ResponseEntity<>(
                Map.of("avatar", fullpathAvatar),
                HttpStatus.OK
        );
    }

    @GetMapping("/validate-email")
    public ResponseEntity<?> validateEmail(@RequestParam("email") String email) {
        if (usuarioService.isExitsAccountWithEmail(email)) {
            throw new UserAlreadyExistException("An account associated with an email already exists");
        }

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message("Email is available")
                        .build(),
                HttpStatus.OK
                );
    }

    @PutMapping("/update/credentials")
    public ResponseEntity<?> updateCredentials(
            @RequestParam("userId") Long userId,
            @RequestBody ChangePasswordDTO newPassword,
            HttpServletRequest req
    ) {

        if (!usuarioService.isExistsUser(userId)) {
            throw new ResourceNotFoundException(String
                    .format("User not found for id=%s", userId));
        }

        String token = jwtService.getTokenFromReq(req);

        String email = jwtService.getEmailFromToken(token);

//      Si el usuario tiene un token que no corresponde a la cuenta que quiere editar se lanzara esta excepcion
        if (!usuarioService.macthEmailToken(userId, email)) {
            throw new UnauthorizedException("Account not authorized");
        }

        usuarioService.updatePassword(userId, newPassword.getNewPassword());

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message("Password updated successfully")
                        .build(),
                HttpStatus.OK
        );

    }

    private Cookie createJwtCookie(String token, int expirationTime) {
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(false); // La cookie no es accesible mediante JavaScript
        jwtCookie.setSecure(false);   // Solo se envía a través de HTTPS
        jwtCookie.setPath("/");      // Hacerla disponible para toda la aplicación
        jwtCookie.setMaxAge(expirationTime); // Expira en 7 días
        return jwtCookie;
    }

    private Object getUsertypeByUser(Usuario user) {
        if (user.getTipo().equals(UserRole.TURISTA)) {
            TuristaDTO turistaDTO = turistaService.getTuristaByUser(user);
            turistaDTO.setExclusivo(suscripcionService.hasActiveSubscription(user));
            return turistaDTO;
        }

        if (user.getTipo().equals(UserRole.ADMINISTRADOR)) {
            return administradorService.getAdminByUser(user);
        }

        if (user.getTipo().equals(UserRole.ALIADO)) {
            AliadoDTO aliadoDTO = aliadoService.getAliadoByUser(user);
            aliadoDTO.setExclusivo(suscripcionService.hasActiveSubscription(user));
            return aliadoDTO;
        }

        return null;
    }
}
