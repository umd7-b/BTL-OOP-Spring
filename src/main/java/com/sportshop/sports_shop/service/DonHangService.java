package com.sportshop.sports_shop.service;

import java.util.List;
import java.util.Map;

import com.sportshop.sports_shop.dto.CreateOrderRequest;
import com.sportshop.sports_shop.dto.OrderItemDetailDto;
import com.sportshop.sports_shop.dto.OrderResponse;
import com.sportshop.sports_shop.dto.OrderSummaryDto;
import com.sportshop.sports_shop.model.DonHang;

public interface DonHangService {
    
    // ===== Methods đã có =====
    
    /**
     * Tạo đơn hàng mới
     */
    OrderResponse createOrder(CreateOrderRequest request);
    
    /**
     * Lấy đơn hàng theo ID (Entity)
     */
    DonHang getOrderById(Integer maDonHang);
    
    /**
     * Lấy danh sách đơn hàng của khách hàng (Entity)
     */
    List<DonHang> getOrdersByCustomer(Long maKh);
    
    /**
     * Format trạng thái đơn hàng sang tiếng Việt
     */
    String formatOrderStatus(String trangThai);
    
    // ===== Methods mới cho trang đơn hàng =====
    
    /**
     * Lấy danh sách đơn hàng của khách hàng (DTO)
     */
    List<OrderResponse> getDonHangByKhachHang(Long maKh);
    
    /**
     * Lấy chi tiết một đơn hàng (DTO)
     */
    OrderResponse getDonHangById(Integer maDonHang);
    
    /**
     * Lấy danh sách sản phẩm trong đơn hàng
     */
    List<OrderItemDetailDto> getChiTietDonHang(Integer maDonHang);
    
    /**
     * Kiểm tra đơn hàng có thuộc về khách hàng không
     */
    boolean kiemTraDonHangCuaKhachHang(Integer maDonHang, Long maKh);
    
    /**
     * Hủy đơn hàng
     */
    OrderResponse huyDonHang(Integer maDonHang);
     /**
     * Lấy tất cả đơn hàng (Admin)
     */
    List<OrderSummaryDto> getAllOrders();
    
    /**
     * Lấy thống kê đơn hàng
     */
    Map<String, Object> getOrderStatistics();
    
    /**
     * Cập nhật trạng thái đơn hàng (Admin)
     */
    OrderResponse updateOrderStatus(Integer maDonHang, String newStatus);
    
   
    void deleteOrder(Integer maDonHang);
    long countOrdersByKhachHang(Long maKh);
        long countPendingOrdersByKhachHang(Long maKh);

}