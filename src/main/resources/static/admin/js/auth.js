
console.log("da load toi script");

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const message = document.getElementById("message");

  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = form.username.value.trim();
    const password = form.password.value.trim();
    message.textContent = "";

    try {
      const res = await fetch("/admin/login", {   // ✅ Sửa lại đường dẫn
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (res.ok) {  // ✅ Sửa lại điều kiện
        message.style.color = "green";
        message.textContent = "✅ " + (data.message || "Đăng nhập thành công!");
        setTimeout(() => {
          window.location.href = data.redirect || "/admin/dashboard";
        }, 1000);
      } else {
        message.style.color = "red";
        message.textContent = "❌ " + (data.message || "Đăng nhập thất bại!");
      }
    } catch (error) {
      message.style.color = "red";
      message.textContent = "⚠️ Lỗi kết nối đến server!";
    }
  });
});
// ==================== MENU NAVIGATION ====================
function showSection(section) {
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => item.classList.remove('active'));
    
    event.target.closest('.menu-item').classList.add('active');
    
    const titles = {
        'dashboard': 'Dashboard',
        'users': 'Quản lý người dùng',
        'products': 'Quản lý sản phẩm',
        'orders': 'Quản lý đơn hàng',
        'analytics': 'Thống kê & Báo cáo',
        'settings': 'Cài đặt hệ thống'
    };
    
    document.querySelector('.header h1').textContent = titles[section];
}

// ==================== DROPDOWN TOGGLE ====================
function toggleDropdown(event) {
    event.stopPropagation();
    
    const dropdown = document.getElementById('dropdownMenu');
    const userInfo = document.querySelector('.user-info');
    
    dropdown.classList.toggle('show');
    userInfo.classList.toggle('active');
}

// Đóng dropdown khi click bên ngoài
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById('dropdownMenu');
    const userInfo = document.querySelector('.user-info');
    
    if (dropdown && !userInfo.contains(event.target)) {
        dropdown.classList.remove('show');
        userInfo.classList.remove('active');
    }
});


// ==================== LOGOUT FUNCTION ====================
async function handleLogout(event) {
    event.stopPropagation();
    
    // Xác nhận đăng xuất
    if (!confirm('Bạn có chắc chắn muốn đăng xuất?')) {
        return;
    }
    
    const spinner = document.getElementById('logoutSpinner');
    const logoutItem = event.currentTarget;
    
    try {
        // Hiển thị loading spinner
        spinner.classList.add('active');
        logoutItem.style.pointerEvents = 'none';
        
        // Gọi API logout
        const response = await fetch('/admin/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'same-origin' // Gửi kèm session cookie
        });
        
        if (!response.ok) {
            throw new Error('Logout failed');
        }
        
        const data = await response.json();
        
        // Hiển thị thông báo thành công
        console.log(data.message);
        
        // Redirect về trang login
        setTimeout(() => {
            window.location.href = data.redirect || '/admin/login';
        }, 500);
        
    } catch (error) {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi đăng xuất. Vui lòng thử lại!');
        
        // Ẩn spinner và cho phép click lại
        spinner.classList.remove('active');
        logoutItem.style.pointerEvents = 'auto';
    }
}

// ==================== KHỞI TẠO ====================
document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard loaded successfully');
});
