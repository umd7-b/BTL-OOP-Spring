// ==============================
//  ACTIVE MENU + NAVIGATION
// ==============================
function showSection(page) {
    document.querySelectorAll(".menu-item").forEach(item => item.classList.remove("active"));

    const activeItem = [...document.querySelectorAll(".menu-item")]
        .find(item => item.getAttribute("data-page") === page);

    if (activeItem) activeItem.classList.add("active");

    const paths = {
        dashboard: "/admin/dashboard",
        users: "/admin/users",
        products: "/admin/products",
        orders: "/admin/orders",
    };

    if (paths[page]) window.location.href = paths[page];
}



// ==============================
// LOAD DASHBOARD BASE
// ==============================
document.addEventListener("DOMContentLoaded", () => {
    loadDashboard();
    loadAnalytics("day");   // mặc định load theo ngày
});


async function loadDashboard() {
    try {
        const res = await fetch("/api/admin/dashboard");
        const data = await res.json();

        document.getElementById("totalUsers").textContent = data.totalUsers;
        document.getElementById("totalOrders").textContent = data.totalOrders;
        document.getElementById("totalProducts").textContent = data.totalProducts;
        document.getElementById("totalRevenue").textContent = formatMoney(data.totalRevenue);

    } catch (error) {
        console.error("Lỗi load dashboard:", error);
    }
}



// ==============================
// FILTER (DAY - MONTH - YEAR)
// ==============================
function changeTimeFilter(type) {
    document.querySelectorAll(".filter-tab").forEach(btn => btn.classList.remove("active"));
    event.target.classList.add("active");
    loadAnalytics(type);
}

let analyticsChart = null;

async function loadAnalytics(type) {

    const res = await fetch(`/api/admin/analytics?filter=${type}`);
    const data = await res.json();

    renderChart(data.labels, data.revenues, data.orders);

    document.getElementById("totalRevenue").textContent = formatMoney(data.totalRevenue);
    document.getElementById("completedOrders").textContent = data.completedOrders;
}

function renderChart(labels, revenues, orders) {

    const ctx = document.getElementById("analyticsChart").getContext("2d");
    if (analyticsChart) analyticsChart.destroy();

    analyticsChart = new Chart(ctx, {
        data: {
            labels: labels,
            datasets: [
                {
                    type: "line",
                    label: "Doanh thu",
                    data: revenues,
                    borderColor: "#2b8eff",
                    backgroundColor: "rgba(43,142,255,0.25)",
                    borderWidth: 2,
                    tension: 0.3,
                    yAxisID: "y"
                },
                {
                    type: "bar",
                    label: "Số đơn hàng",
                    data: orders,
                    backgroundColor: "rgba(255,140,0,0.8)",
                    borderRadius: 4,
                    yAxisID: "y1"
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    position: "left",
                    title: { display: true, text: "Doanh thu (VND)" }
                },
                y1: {
                    beginAtZero: true,
                    position: "right",
                    grid: { drawOnChartArea: false },
                    title: { display: true, text: "Số đơn hàng" }
                }
            }
        }
    });
}
function formatMoney(n) {
    return new Intl.NumberFormat("vi-VN").format(n) + "₫";
}
