// Xử lý active menu + điều hướng
function showSection(page) {
    
    document.querySelectorAll(".menu-item").forEach(item => {
        item.classList.remove("active");
    });

  
    const activeItem = [...document.querySelectorAll(".menu-item")]
        .find(item => item.getAttribute("data-page") === page);
    if (activeItem) {
        activeItem.classList.add("active");
    }

  
    const paths = {
        dashboard: "/admin/dashboard",
        users: "/admin/users",
        products: "/admin/products",
        orders: "/admin/orders",
        analytics: "/admin/analytics",
        settings: "/admin/settings"
    };

    if (paths[page]) {
        window.location.href = paths[page];
    }
}
async function applyDateFilter() {
    const start = document.getElementById("startDate").value;
    const end = document.getElementById("endDate").value;

    if (!start || !end) {
        alert("Vui lòng chọn đủ ngày!");
        return;
    }

    loadDashboard(start, end);
}

async function loadDashboard(startDate, endDate) {
    try {
        const res = await fetch(`/api/admin/dashboard?start=${startDate}&end=${endDate}`);
        const data = await res.json();

        // ✅ update stats
        document.getElementById("totalUsers").textContent = data.totalUsers;
        document.getElementById("totalOrders").textContent = data.totalOrders;
        document.getElementById("totalProducts").textContent = data.totalProducts;
        document.getElementById("totalRevenue").textContent = formatMoney(data.revenue);

        // ✅ update order table
        const tbody = document.getElementById("orderTableBody");
        tbody.innerHTML = "";

        data.recentOrders.forEach(o => {
            const tr = document.createElement("tr");

            tr.innerHTML = `
                <td>#${o.maDonHang}</td>
                <td>${o.tenKhachHang}</td>
                <td>${formatMoney(o.tongTien)}</td>
                <td><span class="status ${o.trangThai.toLowerCase()}">${o.trangThai}</span></td>
                <td>${o.ngayDat}</td>
                <td>
                    <button class="btn btn-primary" onclick="viewOrder(${o.maDonHang})">Xem</button>
                    <button class="btn btn-danger" onclick="deleteOrder(${o.maDonHang})">Xóa</button>
                </td>
            `;

            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Lỗi load dashboard:", error);
    }
}

function formatMoney(n) {
    return new Intl.NumberFormat("vi-VN").format(n) + "₫";
}

