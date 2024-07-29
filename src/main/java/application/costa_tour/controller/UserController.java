package application.costa_tour.controller;

import application.costa_tour.dto.request.AuthReqDTO;
import application.costa_tour.dto.request.UpdateAvatarReqDTO;
import application.costa_tour.exception.*;
import application.costa_tour.model.UserRole;
import application.costa_tour.model.Usuario;
import application.costa_tour.service.ClienteService;
import application.costa_tour.service.StorageService;
import application.costa_tour.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private HttpServletRequest req;

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser (
            @ModelAttribute @Valid AuthReqDTO authReq
            ) {

        Usuario user = usuarioService.credentialsValidate(authReq.getEmail(), authReq.getPassword());

        if (user.getTipo().equals(UserRole.CLIENTE)) {
            return new ResponseEntity<>(
                clienteService.getClientByUser(user),
                HttpStatus.OK
            );
        }

        if (user.getTipo().equals(UserRole.ADMINISTRADOR)) {
            return new ResponseEntity<>(
                SuccessResponse.builder()
                    .message("Login administrator")
                    .build(),
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
                filename,
                req
                    .getRequestURL()
                    .toString()
                    .replace(req.getRequestURI(), ""));

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .message("Avatar updated successfully")
                        .build(),
                HttpStatus.OK
        );
    }
}
