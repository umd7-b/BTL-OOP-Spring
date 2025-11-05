package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.AnhSanPham;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.AnhSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AnhSanPhamService {

    @Autowired
    private AnhSanPhamRepository repo;

    private final String UPLOAD_DIR = "uploads/product-images/";

    public void saveImages(SanPham sanPham, MultipartFile[] files) throws IOException {

        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) folder.mkdirs();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String savePath = UPLOAD_DIR + fileName;

            file.transferTo(new File(savePath));

            AnhSanPham anh = new AnhSanPham();
            anh.setSanPham(sanPham);
            anh.setLinkAnh("/" + savePath);

            repo.save(anh);
        }
    }
}
