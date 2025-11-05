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
