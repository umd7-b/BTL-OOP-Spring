package com.sportshop.sports_shop.service;

import com.sportshop.sports_shop.model.ThuongHieu;

import java.util.List;
import java.util.Optional;

public interface ThuongHieuService {
    List<ThuongHieu> getAll();
    Optional<ThuongHieu> getById(Integer id);
    ThuongHieu save(ThuongHieu thuongHieu);
    void delete(Integer id);
    ThuongHieu getByName(String name);
    boolean existsByName(String name);
}
