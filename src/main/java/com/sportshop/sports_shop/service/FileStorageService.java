

package com.sportshop.sports_shop.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service // <-- Quan trọng!
public class FileStorageService {

    // Thư mục gốc để lưu ảnh (trong /resources/static)
    private final Path root = Paths.get("src/main/resources/static/uploads/products");

    public FileStorageService() {
        try {
            // Tạo thư mục nếu nó chưa tồn tại
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }
    public void delete(String filename) {
        try {
            Path file = this.root.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa file: " + filename, e);
        }
    }

    /**
     * Lưu file và trả về tên file duy nhất
     */
    public String save(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            
            // Tạo tên file duy nhất để tránh trùng lặp
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            // Đường dẫn đầy đủ đến file
            Path destinationFile = this.root.resolve(uniqueFileName);

            // Sao chép file vào thư mục đích
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Trả về tên file duy nhất đã lưu
            return uniqueFileName;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
} 