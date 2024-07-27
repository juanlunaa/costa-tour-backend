package application.costa_tour.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

    public Resource loadFile (String filename) {
        try {
            Path file = Paths.get(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Couldn't read file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Couldn't read file.");
        }
    }
}
