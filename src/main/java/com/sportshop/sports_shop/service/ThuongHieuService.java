package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.ThuongHieu;
import com.sportshop.sports_shop.repository.ThuongHieuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ThuongHieuService {

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    /**
     * Lấy tất cả các thương hiệu.
     * @return List<ThuongHieu>
     */
    public List<ThuongHieu> getAllThuongHieus() {
        return thuongHieuRepository.findAll();
    }


    public ThuongHieu getThuongHieuById(Integer maThuongHieu) {
        return thuongHieuRepository.findById(maThuongHieu)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu với ID: " + maThuongHieu));
    }
}