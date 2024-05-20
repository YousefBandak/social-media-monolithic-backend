package object_orienters.techspot.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        try {
            Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
                                                                                          
            Files.createDirectories(fileStorageLocation);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath()
                    .toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete file " + fileName + ". Please try again!", e);
        }
    }

}