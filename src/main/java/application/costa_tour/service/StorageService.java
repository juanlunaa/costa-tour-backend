package application.costa_tour.service;

import application.costa_tour.exception.BadRequestException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//    public List<String> updatePlanImages (Long idPlan, String prevPlanName, String planName, List<MultipartFile> images) {
//        boolean isSameName = prevPlanName.equalsIgnoreCase(planName);
//
//        if (!isSameName) {
//            String prevFullPathImages = String.format("%s/%s-%s", plansLocation, prevPlanName.toLowerCase().replace(' ', '-'), idPlan);
//            File folder = new File(prevFullPathImages);
//            deleteFilesToFolder(folder);
//            folder.delete();
//        } else {
//            String prevFullPathImages = String.format("%s/%s-%s", plansLocation, planName.toLowerCase().replace(' ', '-'), idPlan);
//            File folder = new File(prevFullPathImages);
//            deleteFilesToFolder(folder);
//        }
//
//        return savePlanImages(idPlan, planName,images);
//    }

    public List<String> updatePlanImages (
            Long idPlan,
            String prevPlanName,
            String newPlanName,
            List<String> existingImagesPaths,
            List<MultipartFile> newImages
    ) {
        String prevPlanNameFormat = formatPlanName(prevPlanName);
        String newPlanNameFormat = formatPlanName(newPlanName);

        String prevFolderPath = String.format("%s/%s-%s", plansLocation, prevPlanNameFormat, idPlan);
        String newFolderPath = String.format("%s/%s-%s", plansLocation, newPlanNameFormat, idPlan);
        System.out.println(prevFolderPath + "\n" + newFolderPath);

        List<String> updateUrls = new ArrayList<>();

        boolean planNameChanged = !prevFolderPath.equals(newPlanNameFormat);

        // Se renombra la carpeta en la que estaban almacenadas las imagenes si el nombre del
        // plan cambia
        if (planNameChanged) {
            File oldDir = new File(prevFolderPath);
            File newDir = new File(newFolderPath);
            if (oldDir.exists() && oldDir.isDirectory()) {
                oldDir.renameTo(newDir);
            }
        }

        if (existingImagesPaths != null) {
            for (String existingPath : existingImagesPaths) {
                System.out.println(existingPath);
                if (isValidImagePath(existingPath, idPlan)) {
                    // Si el nombre del plan cambio tambien se deben renombrar las imagenes ya existentes
                    // y se añade la nueva ruta relativa de la imagen a updateUrls
                    if (planNameChanged) {
                        String newPath = updateImagePath(
                                existingPath,
                                prevPlanNameFormat,
                                newPlanNameFormat,
                                idPlan);

                        updateUrls.add(newPath);
                        // Si no, simplemente se añade la ruta relativa de la imagen a updateUrls
                    } else {
                        updateUrls.add(existingPath);
                    }
                }
            }
        }

        // Agregar nuevas imágenes
        int nextImageNumber = getNextImageNumber(updateUrls);

        if (newImages != null) {
            for (MultipartFile newImage : newImages) {
                String filename = String.format("%s-%s%s", newPlanNameFormat, nextImageNumber, getFileExtension(newImage.getOriginalFilename()));
                saveFile(newFolderPath, newImage, filename);
                String newImagePath = String.format("/files/planes/%s-%s/%s", newPlanNameFormat, idPlan, filename);
                updateUrls.add(newImagePath);
                nextImageNumber++;
            }
        }

        deleteUnusedImages(newFolderPath, updateUrls);

        return updateUrls;
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

    private void deleteUnusedImages(String imagesFolderPath, List<String> updatedUrls) {
        File folder = new File(imagesFolderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // Se crea la ruta en la que la imagen es accedida
                // Ej: /files/planes/plan-name-1/image-1.jpg
                // => /files/planes es el endpoint que sirver las imagenes
                // => /plan-name-1 es el nombre del folder donde estan las imagenes en la ruta relativa /media/planes
                String filePath = String
                        .format("/files/planes/%s/%s",
                                imagesFolderPath.substring(imagesFolderPath.lastIndexOf('/') + 1),
                                file.getName());
                // Si la imagen no esta en la lista de imagenes actualizadas, se elimina
                if (!updatedUrls.contains(filePath)) {
                    file.delete();
                }
            }
        }
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
            throw new BadRequestException("Failed saving file");
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

    private String updateImagePath(
            String prevImageUrl,
            String prevPlanNameFormat,
            String newPlanNameFormat,
            Long idPlan) {

        String filename = prevImageUrl.substring(prevImageUrl.lastIndexOf('/') + 1);
        // Se reemplaza el antiguo nombre con el nuevo sin afectar el index (numero) de la imagen
        String newFilename = filename.replace(prevPlanNameFormat, newPlanNameFormat);

        // Renombrar el archivo físicamente
        String folderPath = String.format("%s/%s-%s/", plansLocation, newPlanNameFormat, idPlan);

        File oldFile = new File(folderPath + filename);
        File newFile = new File(folderPath + newFilename);
        oldFile.renameTo(newFile);

        String newPath = String.format("/files/planes/%s-%s/%s", newPlanNameFormat, idPlan, newFilename);
        return newPath;
    }

    private int getNextImageNumber(List<String> existingImagesPaths) {
        return existingImagesPaths.stream()
                .map(this::extractImageNumber)
                .max(Integer::compare)
                .orElse(0) + 1;
    }

    private int extractImageNumber(String path) {
        String filename = path.substring(path.lastIndexOf('/') + 1);
        String[] parts = filename.split("-");
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            int dotIndex = lastPart.lastIndexOf('.');
            if (dotIndex > 0) {
                try {
                    return Integer.parseInt(lastPart.substring(0, dotIndex));
                } catch (NumberFormatException e) {
                    // En caso de que la parte antes del punto no sea un número
                    return 0;
                }
            }
        }
        return 0; // Si no se encuentra un número, retorna 0
    }

    private String formatPlanName(String planName) {
        return planName.toLowerCase().replace(' ', '-').trim();
    }

    private boolean isValidImagePath(String path, Long idPlan) {
        return path.matches(String.format("/files/planes/.*-%d/.*", idPlan));
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
