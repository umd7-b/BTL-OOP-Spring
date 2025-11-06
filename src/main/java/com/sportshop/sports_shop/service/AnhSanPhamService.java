package com.sportshop.sports_shop.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sportshop.sports_shop.model.AnhSanPham;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.AnhSanPhamRepository;

@Service
public class AnhSanPhamService {

    @Autowired
    private AnhSanPhamRepository repo;

    // ✅ Folder upload cố định nằm trong static
    private final String UPLOAD_DIR = System.getProperty("user.dir")
            + "/src/main/resources/static/uploads/products/";

    public void saveImages(SanPham sp, MultipartFile[] files) throws IOException {

        // ✅ nếu chưa có thư mục -> tạo
        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            // ✅ Tạo tên file duy nhất
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // ✅ Đường dẫn đích
            Path path = Paths.get(UPLOAD_DIR + filename);

            // ✅ Ghi file vào đúng thư mục trong static (KHÔNG dùng temp)
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // ✅ Lưu DB
            AnhSanPham img = new AnhSanPham();
            img.setSanPham(sp);
            img.setLinkAnh(filename);
            repo.save(img);
        }
    }

   
    
}