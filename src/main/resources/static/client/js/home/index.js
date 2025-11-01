

console.log("da ket noi voi js")
document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            console.log("lang nghe su kien ")
            window.location.href = "/login";
        });
    }
});