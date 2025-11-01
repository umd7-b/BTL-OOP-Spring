
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
      const res = await fetch("/admin/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (data.success) {
        message.style.color = "green";
        message.textContent = "✅ " + data.message;
        setTimeout(() => {
          window.location.href = "/admin/dashboard";
        }, 1000);
      } else {
        message.style.color = "red";
        message.textContent = "❌ " + data.message;
      }
    } catch (error) {
      message.style.color = "red";
      message.textContent = "⚠️ Lỗi kết nối đến server!";
    }
  });
});
