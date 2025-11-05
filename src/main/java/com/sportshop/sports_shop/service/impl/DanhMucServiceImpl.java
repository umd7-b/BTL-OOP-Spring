package com.sportshop.sports_shop.service.impl;

import com.sportshop.sports_shop.model.DanhMuc;
import com.sportshop.sports_shop.repository.DanhMucRepository;
import com.sportshop.sports_shop.service.DanhMucService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    private final DanhMucRepository repo;

    public DanhMucServiceImpl(DanhMucRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<DanhMuc> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<DanhMuc> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public DanhMuc save(DanhMuc danhMuc) {
        return repo.save(danhMuc);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public DanhMuc getByName(String name) {
        return repo.findByTenDanhMuc(name);
    }

    @Override
    public boolean existsByName(String name) {
        return repo.existsByTenDanhMuc(name);
    }
}
