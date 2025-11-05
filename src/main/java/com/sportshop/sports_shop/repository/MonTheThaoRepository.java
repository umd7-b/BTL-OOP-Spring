package com.sportshop.sports_shop.repository;

import com.sportshop.sports_shop.model.MonTheThao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonTheThaoRepository extends JpaRepository<MonTheThao, Integer> {
    MonTheThao findByTenMonTheThao(String ten);
    boolean existsByTenMonTheThao(String ten);
}
