package application.costa_tour.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    public void saveFile (String filesLocation, MultipartFile file, String filename) {
        try{
            Path rootLocation = Paths.get(filesLocation);
            Files.createDirectories(rootLocation);

            Path filePath = rootLocation
                    .resolve(Paths.get(filename))
                    .normalize()
                    .toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed saving file.", e);
        }
    }
}
