package application.costa_tour.controller;

import application.costa_tour.exception.ResourceNotFoundException;
import application.costa_tour.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/files")
public class MediaController {

    @Autowired
    private StorageService storageService;

    @Value("${uploadfiles.avatar.location}")
    private String avatarsLocation;

    @Value("${uploadfiles.plans.location}")
    private String plansLocation;

    @GetMapping("/avatars/{filename:.+}")
    public ResponseEntity<Resource> getAvatar (
            @PathVariable("filename") String filename
    ) throws IOException {
        Resource file = storageService.loadFile(avatarsLocation + "/" + filename);

        if (file == null) {
            throw new ResourceNotFoundException("Resource not found");
        }

        String contentType = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

    @GetMapping("/planes/{namePlan}/{filename:.+}")
    public ResponseEntity<Resource> getPlanImage (
            @PathVariable("namePlan") String namePlan,
            @PathVariable("filename") String filename
    ) throws IOException {
        Resource file = storageService.loadFile(plansLocation + "/" + namePlan + "/" + filename);

        if (file == null) {
            throw new ResourceNotFoundException("Resource not found");
        }

        String contentType = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }
}
