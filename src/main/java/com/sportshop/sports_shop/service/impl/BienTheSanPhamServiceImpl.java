package com.sportshop.sports_shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.BienTheSanPhamRepository;
import com.sportshop.sports_shop.service.BienTheSanPhamService;

@Service
public class BienTheSanPhamServiceImpl implements BienTheSanPhamService {

    @Autowired
    private BienTheSanPhamRepository repo;

    @Override
    public void saveVariants(SanPham sanPham, List<BienTheSanPham> variants) {
        for (BienTheSanPham v : variants) {
            v.setSanPham(sanPham);
            repo.save(v);
        }
    }

    @Override
    public void saveAll(List<BienTheSanPham> variants) {
        repo.saveAll(variants);
    }

    @Override
        public List<BienTheSanPham> getByMaSp(int maSp) {
            return repo.findBySanPhamMaSp(maSp);
        }

        public List<BienTheSanPham> getByMaSp(Long maSp) {
            return repo.findBySanPhamMaSp(maSp.intValue());
        }
}
