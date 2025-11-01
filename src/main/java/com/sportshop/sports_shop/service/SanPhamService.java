package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.SanPham; 
import com.sportshop.sports_shop.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;    
import java.util.Optional; 
@Service 
public class SanPhamService {

    @Autowired 
    private SanPhamRepository sanPhamRepository; 

    // Hàm lấy tất cả sản phẩm
    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }

    // Hàm lấy sản phẩm theo ID
    public Optional<SanPham> findById(Integer id) {
        return sanPhamRepository.findById(id); 
    }
    
    // Hàm lưu (thêm/cập nhật) sản phẩm
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    // Hàm xóa sản phẩm
    public void deleteById(Integer id) {
        sanPhamRepository.deleteById(id);
    }
}