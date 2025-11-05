package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.SanPham;

import java.util.List;

public interface BienTheSanPhamService {
    void saveVariants(SanPham sanPham, List<BienTheSanPham> variants);
    void saveAll(List<BienTheSanPham> variants);
}
