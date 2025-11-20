package com.sportshop.sports_shop.controller.admin.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.dto.DashboardSummaryDto;
import com.sportshop.sports_shop.repository.DonHangRepository;
import com.sportshop.sports_shop.service.DashboardService;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardRestController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public DashboardSummaryDto getDashboard() {
        return dashboardService.getSummary();
    }

    // ==========================
    // API ANALYTICS (NO CUSTOM)
    // ==========================
   
     @GetMapping("/analytics")
    public Map<String, Object> analytics(@RequestParam String filter) {

        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();

        List<String> labels = new ArrayList<>();
        List<Long> revenues = new ArrayList<>();
        List<Long> orders = new ArrayList<>();


        switch (filter) {

            // ============================
            // 1️⃣ 30 ngày gần nhất
            // ============================
            case "day" -> {
                LocalDate start = today.minusDays(30);

                var revRows = donHangRepository.getRevenueByDateRange(
                        start.atStartOfDay(), today.atTime(23, 59, 59)
                );

                var orderRows = donHangRepository.getOrderCountByDate(
                        start.atStartOfDay(), today.atTime(23, 59, 59)
                );

                Map<String, Long> orderMap = new HashMap<>();
                orderRows.forEach(r -> orderMap.put(r[0].toString(), (Long) r[1]));

                revRows.forEach(r -> {
                    LocalDate date = ((java.sql.Date) r[0]).toLocalDate();

                    labels.add(date.format(DateTimeFormatter.ofPattern("dd-MM")));
                    revenues.add(((BigDecimal) r[1]).longValue());
                    orders.add(orderMap.getOrDefault(date.toString(), 0L));
                });
            }


            // ============================
            // 2️⃣ Tổng theo THÁNG trong năm
            // ============================
            case "month" -> {
                int year = today.getYear();

                var revRows = donHangRepository.getMonthlyRevenue(year);
                var orderRows = donHangRepository.getMonthlyOrderCount(year);

                Map<Integer, Long> orderMap = new HashMap<>();
                orderRows.forEach(r -> orderMap.put((Integer) r[0], (Long) r[1]));

                revRows.forEach(r -> {
                    int month = (Integer) r[0];

                    labels.add("T" + month);
                    revenues.add(((BigDecimal) r[1]).longValue());
                    orders.add(orderMap.getOrDefault(month, 0L));
                });
            }


            // ============================
            // 3️⃣ Tổng theo NĂM (5 năm)
            // ============================
            case "year" -> {
                int endYear = today.getYear();
                int startYear = endYear - 4;

                var revRows = donHangRepository.getRevenueLast5Years(startYear, endYear);
                var orderRows = donHangRepository.getOrderCountLast5Years(startYear, endYear);

                Map<Integer, Long> orderMap = new HashMap<>();
                orderRows.forEach(r -> orderMap.put((Integer) r[0], (Long) r[1]));

                revRows.forEach(r -> {
                    int year = (Integer) r[0];

                    labels.add(String.valueOf(year));
                    revenues.add(((BigDecimal) r[1]).longValue());
                    orders.add(orderMap.getOrDefault(year, 0L));
                });
            }
        }

        result.put("labels", labels);
        result.put("revenues", revenues);
        result.put("orders", orders);

        result.put("totalRevenue", revenues.stream().mapToLong(v -> v).sum());
        result.put("completedOrders", orders.stream().mapToLong(v -> v).sum());

        return result;
    }

}
