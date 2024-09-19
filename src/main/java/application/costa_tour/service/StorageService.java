package application.costa_tour.service;

import application.costa_tour.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

@Service
public class StorageService {

    @Value("${uploadfiles.avatar.location}")
    private String avatarsLocation;

    @Value("${uploadfiles.plans.location}")
    private String plansLocation;

    public String saveAvatar (Long userId, MultipartFile file) {
        String filename = String
                .format("%s-avatar%s", userId, getFileExtension(file.getOriginalFilename()));

        saveFile(avatarsLocation, file, filename);

        return filename;
    }

    public List<String> savePlanImages (Long idPlan, String planName, List<MultipartFile> images) {
        List<String> urls = new ArrayList<>();
        String planNameFormat = planName.toLowerCase().replace(' ', '-');
        String fullPathSaveImages = String.format("%s/%s-%s", plansLocation, planNameFormat, idPlan);

        for (int i = 0; i < images.size(); i++) {
            String filename = String
                    .format("%s-%s%s", planNameFormat, (i+1), getFileExtension(images.get(i).getOriginalFilename()));

            saveFile(fullPathSaveImages, images.get(i), filename);

            urls.add(String.format("/files/planes/%s-%s/%s", planNameFormat, idPlan, filename));
        }

        return urls;
    }

    public List<String> updatePlanImages (Long idPlan, String prevPlanName, String planName, List<MultipartFile> images) {
        boolean isSameName = prevPlanName.equalsIgnoreCase(planName);

        if (!isSameName) {
            String prevFullPathImages = String.format("%s/%s-%s", plansLocation, prevPlanName.toLowerCase().replace(' ', '-'), idPlan);
            File folder = new File(prevFullPathImages);
            deleteFilesToFolder(folder);
            folder.delete();
        } else {
            String prevFullPathImages = String.format("%s/%s-%s", plansLocation, planName.toLowerCase().replace(' ', '-'), idPlan);
            File folder = new File(prevFullPathImages);
            deleteFilesToFolder(folder);
        }

        return savePlanImages(idPlan, planName,images);
    }

    private void deleteFilesToFolder (File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteFolderPlan (Long idPlan, String planName) {
        String prevFullPathImages = String.format("%s/%s-%s", plansLocation, planName.toLowerCase().replace(' ', '-'), idPlan);
        File folder = new File(prevFullPathImages);
        deleteFilesToFolder(folder);
        folder.delete();
    }

    private void saveFile (String filesLocation, MultipartFile file, String filename) {
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
                throw new ResourceNotFoundException("Resource not found.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Couldn't read file. :D");
        }
    }

    private String getFileExtension (String filename) {
        String fileExtension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return fileExtension = filename.substring(dotIndex);
        }
        return ".jpg";
    }
}
