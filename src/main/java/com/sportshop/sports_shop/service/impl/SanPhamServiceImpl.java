package com.sportshop.sports_shop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.SanPhamRepository;
import com.sportshop.sports_shop.service.SanPhamService;

@Service
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    public SanPhamServiceImpl(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public List<SanPham> getAll() {
        return sanPhamRepository.findAll();
    }

    @Override
    public Optional<SanPham> getById(Long id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    @Override
    public void delete(Long id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public List<SanPham> search(String keyword) {
        return sanPhamRepository.findByTenSpContainingIgnoreCase(keyword);
    }

    @Override
    public Optional<SanPham> getByIdFull(Long id) {
        // Giả định rằng bạn đã tạo phương thức findByIdWithRelations 
        // trong SanPhamRepository như bước trước
        return sanPhamRepository.findByIdWithRelations(id);
    }

    //filter
    @Override
public List<SanPham> filterSanPham(Integer thuongHieuId, Integer monTheThaoId, Integer danhMucId,
                                   java.math.BigDecimal giaMin, java.math.BigDecimal giaMax) {
    return sanPhamRepository.filterSanPham(thuongHieuId, monTheThaoId, danhMucId, giaMin, giaMax);
}

}
