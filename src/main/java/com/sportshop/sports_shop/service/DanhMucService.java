package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.DanhMuc;

import java.util.List;
import java.util.Optional;

public interface DanhMucService {
    List<DanhMuc> getAll();
    Optional<DanhMuc> getById(Integer id);
    DanhMuc save(DanhMuc danhMuc);
    void delete(Integer id);
    DanhMuc getByName(String name);
    boolean existsByName(String name);
}
