

console.log("da ket noi voi js oke oke")
document.addEventListener("DOMContentLoaded", async () => {
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
    // js product home
    const grid = document.getElementById("productGrid");
    const apiUrl = "http://localhost:8081/api/sanpham/all";

    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error("Lỗi khi tải sản phẩm");
        const products = await response.json();

        if (products.length === 0) {
            grid.innerHTML = `<p class="no-products">Không có sản phẩm nào để hiển thị.</p>`;
            return;
        }

        grid.innerHTML = products
            .map((p) => {
                const imgSrc = p.anhDaiDien
                    ? `${p.anhDaiDien}`
                    : "/assets/img/no-image.jpg";

                const ten = p.tenSp || "Sản phẩm chưa có tên";
                const gia = p.giaKm ?? p.giaGoc ?? 0;
                const giaGoc =
                    p.giaGoc && p.giaKm && p.giaKm < p.giaGoc
                        ? `<span class="old-price">${p.giaGoc.toLocaleString()}₫</span>`
                        : "";

                return `
                <div class="product-card">
                    <div class="product-img">
                        <img src="${imgSrc}" alt="${ten}">
                    </div>
                    <div class="product-info">
                        <h3 class="product-name">${ten}</h3>
                        <div class="product-price">
                            <span class="new-price">${gia.toLocaleString()}₫</span>
                            ${giaGoc}
                        </div>
                    </div>
                </div>
            `;
            })
            .join("");
    } catch (error) {
        console.error(error);
        grid.innerHTML = `<p class="no-products">Không thể tải sản phẩm.</p>`;
    }

});