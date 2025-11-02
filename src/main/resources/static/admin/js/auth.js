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
      const res = await fetch("/login/admin", {   // ✅ Sửa lại đường dẫn
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
