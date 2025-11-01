package com.sportshop.sports_shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportshop.sports_shop.model.DanhMuc;
import com.sportshop.sports_shop.repository.DanhMucRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    /**
     * Lấy tất cả các danh mục.
     * @return List<DanhMuc>
     */
    public List<DanhMuc> getAllDanhMucs() {
        return danhMucRepository.findAll();
    }

    /**
     * Lấy thông tin một danh mục bằng ID.
     * @param maDanhMuc ID của danh mục
     * @return DanhMuc
     */
    // ✅ SỬA Ở ĐÂY: Đổi "Long" thành "Integer"
    public DanhMuc getDanhMucById(Integer maDanhMuc) { 
        return danhMucRepository.findById(maDanhMuc) // Giờ nó đã khớp
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục với ID: " + maDanhMuc));
    }
}