// package com.sportshop.sports_shop.model;

// import jakarta.persistence.*;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Table(name = "chi_tiet_gio_hang") // Map với bảng 'chi_tiet_gio_hang'
// @Getter
// @Setter
// public class ChiTietGioHang {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "ma_chi_tiet")
//     private Long maChiTiet;


//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "ma_gio_hang", nullable = false) // Tên cột khóa ngoại
//     private GioHang gioHang;


//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "ma_san_pham", nullable = false)
//     private SanPham sanPham;

//     @Column(name = "so_luong", nullable = false)
//     private int soLuong;

//     // --- (Tùy chọn) ---
//     // Bạn không cần lưu giá ở đây, vì giá trong giỏ hàng có thể thay đổi
   
//     // Giá sẽ được lấy trực tiếp từ SanPham khi tính tổng tiền.
// }