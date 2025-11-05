package com.sportshop.sports_shop.repository;

import com.sportshop.sports_shop.model.AnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {
    List<AnhSanPham> findBySanPhamMaSp(Integer maSp);
    void deleteBySanPhamMaSp(Integer maSp);
}
