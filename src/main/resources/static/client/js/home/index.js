

console.log("da ket noi voi js oke oke")
document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            console.log("lang nghe su kien ")
            window.location.href = "/login";
        });
    }

    function setupDropdown(buttonId, listId, apiUrl, labelKey) {
        const btn = document.getElementById(buttonId);
        const list = document.getElementById(listId);

        btn.addEventListener("click", async (event) => {
            event.preventDefault();
            event.stopPropagation(); // Ngăn sự kiện lan ra ngoài

            // Ẩn các dropdown khác nếu có
            document.querySelectorAll(".dropdown-content.show").forEach((el) => {
                if (el !== list) el.classList.remove("show");
            });

            // Toggle dropdown hiện tại
            list.classList.toggle("show");

            // Nếu dropdown đang trống thì mới load API
            if (list.innerHTML.trim() === "") {
                try {
                    const response = await fetch(apiUrl);
                    const data = await response.json();

                    list.innerHTML = data
                        .map((item) => `<li><a href="#">${item[labelKey]}</a></li>`)
                        .join("");
                } catch (error) {
                    console.error(`Lỗi khi tải dữ liệu từ ${apiUrl}:`, error);
                }
            }
        });

        // Khi click ra ngoài, đóng dropdown
        document.addEventListener("click", (event) => {
            if (!btn.contains(event.target) && !list.contains(event.target)) {
                list.classList.remove("show");
            }
        });
    }

    // --- Gọi hàm cho từng dropdown ---
    setupDropdown(
        "thuongHieuBtn",
        "thuongHieuList",
        "http://localhost:8081/api/thuonghieu/all",
        "tenThuongHieu"
    );

    setupDropdown(
        "monTheThaoBtn",
        "monTheThaoList",
        "http://localhost:8081/api/monthethao/all",
        "tenMonTheThao"
    );
    setupDropdown(
        "tatCaBtn",
        "tatCaList",
        "http://localhost:8081/api/danhmuc/all",
        "tenDanhMuc"
    );


});