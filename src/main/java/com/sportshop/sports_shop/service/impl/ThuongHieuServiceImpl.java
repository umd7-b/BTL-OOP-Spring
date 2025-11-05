package com.sportshop.sports_shop.service.impl;

import com.sportshop.sports_shop.model.ThuongHieu;
import com.sportshop.sports_shop.repository.ThuongHieuRepository;
import com.sportshop.sports_shop.service.ThuongHieuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {

    private final ThuongHieuRepository repo;

    public ThuongHieuServiceImpl(ThuongHieuRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ThuongHieu> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<ThuongHieu> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public ThuongHieu save(ThuongHieu thuongHieu) {
        return repo.save(thuongHieu);
    }

    @Override
    public void delete(Integer  id) {
        repo.deleteById(id);
    }

    @Override
    public ThuongHieu getByName(String name) {
        return repo.findByTenThuongHieu(name);
    }

    @Override
    public boolean existsByName(String name) {
        return repo.existsByTenThuongHieu(name);
    }
}
