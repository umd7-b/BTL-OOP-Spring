package com.sportshop.sports_shop.service;

import java.util.List;

import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.SanPham;

public interface BienTheSanPhamService {
    void saveVariants(SanPham sanPham, List<BienTheSanPham> variants);
    void saveAll(List<BienTheSanPham> variants);
    List<BienTheSanPham> getByMaSp(int intValue);
}
