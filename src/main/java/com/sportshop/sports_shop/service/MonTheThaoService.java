package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.MonTheThao;

import java.util.List;
import java.util.Optional;

public interface MonTheThaoService {
    List<MonTheThao> getAll();
    Optional<MonTheThao> getById(Integer id);
    MonTheThao save(MonTheThao monTheThao);
    void delete(Integer id);
    MonTheThao getByName(String name);
    boolean existsByName(String name);
}
