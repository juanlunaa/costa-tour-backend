package application.costa_tour.controller;

import application.costa_tour.exception.BadRequestException;
import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.exception.SuccessResponse;
import application.costa_tour.service.StorageService;
import application.costa_tour.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private HttpServletRequest req;

    @PutMapping("/update/avatar")
    public ResponseEntity<?> changeAvatar (
            @RequestParam("userId") Long userId,
            @RequestParam("avatar") MultipartFile file
    ) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (userId == null) {
            throw new BadRequestException("userId is required");
        }

        if (!usuarioService.isExistsUser(userId)) {
            throw new ResourceNotFoundException("User not found to update avatar");
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
