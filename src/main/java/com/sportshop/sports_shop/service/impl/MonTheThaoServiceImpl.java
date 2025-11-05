package com.sportshop.sports_shop.service.impl;

import com.sportshop.sports_shop.model.MonTheThao;
import com.sportshop.sports_shop.repository.MonTheThaoRepository;
import com.sportshop.sports_shop.service.MonTheThaoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonTheThaoServiceImpl implements MonTheThaoService {

    private final MonTheThaoRepository repo;

    public MonTheThaoServiceImpl(MonTheThaoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<MonTheThao> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<MonTheThao> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public MonTheThao save(MonTheThao monTheThao) {
        return repo.save(monTheThao);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public MonTheThao getByName(String name) {
        return repo.findByTenMonTheThao(name);
    }

    @Override
    public boolean existsByName(String name) {
        return repo.existsByTenMonTheThao(name);
    }
}
