
console.log("✅ File auth.js đã được load!");

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    const message = document.getElementById("message");

    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const username = form.username.value.trim();
        const password = form.password.value.trim();


        if (!username || !password) {
            message.textContent = "Vui lòng nhập tên đăng nhập và mật khẩu!";
            message.style.color = "red";
            return;
        }

        try {

            message.textContent = "Đang xử lý...";
            message.style.color = "blue";

            const response = await fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {

                message.textContent = "Đăng nhập thành công!";
                message.style.color = "green";

                if (data.token) {
                    window.authToken = data.token;
                }
                if (data.user) {
                    sessionStorage.setItem('username', data.user.username);
                    sessionStorage.setItem('ho_ten', data.user.ho_ten);
                }


                setTimeout(() => {
                    window.location.href = "/";
                }, 1000);
            } else {

                message.textContent = data.message || "Sai tên đăng nhập hoặc mật khẩu!";
                message.style.color = "red";
            }
        } catch (error) {
            console.error("Lỗi:", error);
            message.textContent = "Có lỗi xảy ra. Vui lòng thử lại!";
            message.style.color = "red";
        }
    });
});
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const message = document.getElementById("message");

    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Lấy dữ liệu từ form
        const data = {
            username: form.username.value.trim(),
            password: form.password.value.trim(),
            confirmPassword: form.confirmPassword.value.trim(),
            ho_ten: form.name.value.trim(),
            email: form.email.value.trim(),
            so_dien_thoai: form.phone.value.trim(),
            dia_chi: form.address.value.trim(),
            ngay_sinh: form.birthday.value,
            gioi_tinh: form.querySelector('input[name="gender"]:checked')?.value || null
        };
        console.log(data)
        // ✅ Kiểm tra dữ liệu đầu vào
        const missingField = Object.entries(data).find(([key, value]) => {
            // Không bắt buộc confirmPassword & gioi_tinh
            if (key === "confirmPassword" || key === "gioi_tinh") return false;
            return !value;
        });

        if (missingField) {
            showMessage("Vui lòng nhập đầy đủ thông tin bắt buộc!", "red");
            return;
        }

        if (data.confirmPassword && data.password !== data.confirmPassword) {
            showMessage("Mật khẩu không khớp!", "red");
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(data.email)) {
            showMessage("Email không hợp lệ!", "red");
            return;
        }

        // ✅ Gửi yêu cầu đăng ký
        try {
            showMessage("Đang xử lý...", "blue");

            const response = await fetch("/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    username: data.username,
                    password: data.password,
                    ho_ten: data.ho_ten,
                    email: data.email,
                    so_dien_thoai: data.so_dien_thoai,
                    dia_chi: data.dia_chi,
                    ngay_sinh: data.ngay_sinh,
                    gioi_tinh: data.gioi_tinh
                })
            });

            const result = await response.json();

            if (response.ok) {
                showMessage("Đăng ký thành công! Đang chuyển đến trang đăng nhập...", "green");
                form.reset();
                setTimeout(() => (window.location.href = "/login"), 1000);
            } else {
                showMessage(result.message || "Đăng ký thất bại!", "red");
            }
        } catch (error) {
            console.error("Lỗi:", error);
            showMessage("Có lỗi xảy ra. Vui lòng thử lại!", "red");
        }
    });

    // 📢 Hàm hiển thị thông báo
    function showMessage(text, color) {
        if (message) {
            message.textContent = text;
            message.style.color = color;
        }
    }
});





