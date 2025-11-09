package com.sportshop.sports_shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.dto.OrderItemDetailDto;
import com.sportshop.sports_shop.dto.OrderResponse;
import com.sportshop.sports_shop.service.DonHangService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/order")
public class TrangDonHangController {

    @Autowired
    private DonHangService donHangService;

    /**
     * API lấy danh sách đơn hàng của khách hàng đang đăng nhập
     * GET /api/don-hang/khach-hang
     */
    @GetMapping("/customer")
    public ResponseEntity<?> getDonHangCuaKhachHang(HttpSession session) {
        try {
            // Lấy mã khách hàng từ session
            Long maKh = (Long) session.getAttribute("userId");
            
            if (maKh == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Vui lòng đăng nhập để xem đơn hàng");
            }

            // Lấy danh sách đơn hàng
            List<OrderResponse> orders = donHangService.getDonHangByKhachHang(maKh);
            
            return ResponseEntity.ok(orders);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi tải đơn hàng: " + e.getMessage());
        }
    }

    /**
     * API lấy chi tiết đơn hàng (danh sách sản phẩm)
     * GET /api/don-hang/{maDonHang}/chi-tiet
     */
    @GetMapping("/{maDonHang}/detail")
    public ResponseEntity<?> getChiTietDonHang(
            @PathVariable Integer maDonHang,
            HttpSession session) {
        try {
            // Lấy mã khách hàng từ session
            Long maKh = (Long) session.getAttribute("maKh");
            
            if (maKh == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Vui lòng đăng nhập");
            }

            // Kiểm tra đơn hàng có thuộc về khách hàng này không
            if (!donHangService.kiemTraDonHangCuaKhachHang(maDonHang, maKh)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xem đơn hàng này");
            }

            // Lấy chi tiết đơn hàng
            List<OrderItemDetailDto> items = donHangService.getChiTietDonHang(maDonHang);
            
            return ResponseEntity.ok(items);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi tải chi tiết đơn hàng: " + e.getMessage());
        }
    }

    /**
     * API hủy đơn hàng
     * PUT /api/don-hang/{maDonHang}/huy
     */
    @PutMapping("/{maDonHang}/delete-order")
    public ResponseEntity<?> huyDonHang(
            @PathVariable Integer maDonHang,
            HttpSession session) {
        try {
            // Lấy mã khách hàng từ session
            Long maKh = (Long) session.getAttribute("maKh");
            
            if (maKh == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Vui lòng đăng nhập");
            }

            // Kiểm tra đơn hàng có thuộc về khách hàng này không
            if (!donHangService.kiemTraDonHangCuaKhachHang(maDonHang, maKh)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền hủy đơn hàng này");
            }

            // Hủy đơn hàng
            OrderResponse response = donHangService.huyDonHang(maDonHang);
            
            if (response.getMessage() != null && response.getMessage().contains("thành công")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi hủy đơn hàng: " + e.getMessage());
        }
    }

    /**
     * API lấy thông tin chi tiết 1 đơn hàng (dùng cho trang chi tiết)
     * GET /api/don-hang/{maDonHang}
     */
    @GetMapping("/{maDonHang}")
    public ResponseEntity<?> getDonHangDetail(
            @PathVariable Integer maDonHang,
            HttpSession session) {
        try {
            // Lấy mã khách hàng từ session
            Long maKh = (Long) session.getAttribute("maKh");
            
            if (maKh == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Vui lòng đăng nhập");
            }

            // Kiểm tra đơn hàng có thuộc về khách hàng này không
            if (!donHangService.kiemTraDonHangCuaKhachHang(maDonHang, maKh)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xem đơn hàng này");
            }

            // Lấy thông tin đơn hàng
            OrderResponse order = donHangService.getDonHangById(maDonHang);
            
            return ResponseEntity.ok(order);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi tải đơn hàng: " + e.getMessage());
        }
    }
}