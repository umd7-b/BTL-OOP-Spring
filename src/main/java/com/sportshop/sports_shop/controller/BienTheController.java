    package com.sportshop.sports_shop.controller;

    import java.util.HashMap;
    import java.util.Map;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RestController;

    import com.sportshop.sports_shop.repository.BienTheSanPhamRepository;

    @RestController
    public class BienTheController {

        @Autowired
        private BienTheSanPhamRepository bienTheRepository;

        @GetMapping("/api/bienthe/{id}")
        public ResponseEntity<?> getBienThe(@PathVariable Integer id) {

            var bt = bienTheRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể"));

            var sp = bt.getSanPham();

            Map<String, Object> dto = new HashMap<>();
            dto.put("maBienThe", bt.getMaBienThe());
            dto.put("maSp", sp.getMaSp());
            dto.put("tenSp", sp.getTenSp());
            dto.put("gia", sp.getGia());
            dto.put("mauSac", bt.getMauSac());
            dto.put("size", bt.getKichThuoc());
            dto.put("phanLoaiText", bt.getMauSac() + " - " + bt.getKichThuoc());

            if (sp.getDanhSachAnh() != null && !sp.getDanhSachAnh().isEmpty()) {
                dto.put("anhSp", sp.getDanhSachAnh().get(0).getLinkAnh());
            } else {
                dto.put("anhSp", "no-image.png");
            }

            return ResponseEntity.ok(dto);
        }
    }
