package com.sportshop.sports_shop.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.sportshop.sports_shop.dto.DashboardSummaryDto;
import com.sportshop.sports_shop.repository.DonHangRepository;
import com.sportshop.sports_shop.repository.KhachHangRepository;
import com.sportshop.sports_shop.repository.SanPhamRepository;
import com.sportshop.sports_shop.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final KhachHangRepository khachHangRepository;
    private final DonHangRepository donHangRepository;
    private final SanPhamRepository sanPhamRepository;

    public DashboardServiceImpl(KhachHangRepository khachHangRepository,
                                DonHangRepository donHangRepository,
                                SanPhamRepository sanPhamRepository) {
        this.khachHangRepository = khachHangRepository;
        this.donHangRepository = donHangRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public DashboardSummaryDto getSummary() {
        DashboardSummaryDto dto = new DashboardSummaryDto();

        // tổng số người dùng = tổng khách hàng
        dto.setTotalUsers(khachHangRepository.count());

        // tổng số đơn hàng
        dto.setTotalOrders(donHangRepository.count());

        // tổng số sản phẩm
        dto.setTotalProducts(sanPhamRepository.count());

       
        String STATUS_DONE = "DA_GIAO";

        BigDecimal revenue = donHangRepository.sumRevenueByStatus(STATUS_DONE);
        dto.setTotalRevenue(revenue);

        return dto;
    }
}
