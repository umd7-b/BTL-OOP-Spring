package com.sportshop.sports_shop.service;



import java.util.List;
import java.util.Optional;

import com.sportshop.sports_shop.model.SanPham;

public interface SanPhamService {
    List<SanPham> getAll();
    Optional<SanPham> getById(Long id);
    SanPham save(SanPham sanPham);
    void delete(Long    id);
    List<SanPham> search(String keyword);
    
        
    Optional<SanPham> getByIdFull(Long id);
    
}
