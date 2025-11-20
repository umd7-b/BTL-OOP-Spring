package com.sportshop.sports_shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.repository.KhachHangRepository;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    public KhachHang getKhachHangById(Long id) {
        return khachHangRepository.findById(id.intValue()).orElse(null);
    }

    @Transactional
    public void deleteKhachHang(Long id) {
        khachHangRepository.deleteById(id.intValue());
    }

    public boolean existsByEmail(String email) {
        return khachHangRepository.existsByEmail(email);
    }

    public boolean existsByTenDangNhap(String tenDangNhap) {
        return khachHangRepository.existsByTenDangNhap(tenDangNhap);
    }
}