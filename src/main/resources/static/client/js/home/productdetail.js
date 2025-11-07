console.log("da ket noi voi js oke oke")
document.addEventListener("DOMContentLoaded", async () => {
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            console.log("lang nghe su kien ")
            window.location.href = "/login";
        });
    }
})

function setupDropdown(buttonId, listId, apiUrl, labelKey) {
    const btn = document.getElementById(buttonId);
    const list = document.getElementById(listId);

    btn.addEventListener("click", async (event) => {
        event.preventDefault();
        event.stopPropagation();

        document.querySelectorAll(".dropdown-content.show").forEach((el) => {
            if (el !== list) el.classList.remove("show");
        });

        list.classList.toggle("show");

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

    document.addEventListener("click", (event) => {
        if (!btn.contains(event.target) && !list.contains(event.target)) {
            list.classList.remove("show");
        }
    });
}




const params = new URLSearchParams(window.location.search);
const id = params.get("id");
if (!id) {
    document.body.innerHTML = "<h2>Không tìm thấy sản phẩm `${id}</h2>";
    return;
}

const apiUrl = `http://localhost:8081/api/sanpham/${id}`;

try {
    const res = await fetch(apiUrl);
    if (!res.ok) throw new Error("Không thể tải chi tiết sản phẩm");
    const p = await res.json();

    // Hiển thị thông tin cơ bản
    document.getElementById("productName").textContent = p.tenSp;
    document.getElementById("newPrice").textContent = `${(p.giaKm ?? p.giaGoc).toLocaleString()}₫`;
    document.getElementById("oldPrice").textContent =
        p.giaKm && p.giaKm < p.giaGoc ? `${p.giaGoc.toLocaleString()}₫` : "";

    // Ảnh chính
    const mainImg = document.getElementById("mainImage");
    mainImg.src = p.anhDaiDien
        ? `/uploads/product/${p.anhDaiDien}`
        : "/client/images/no-image.png";

    // Ảnh phụ
    const thumbContainer = document.getElementById("thumbContainer");
    if (p.danhSachAnh && p.danhSachAnh.length > 0) {
        thumbContainer.innerHTML = p.danhSachAnh
            .map(
                (a, i) =>
                    `<img src="http://localhost:8081/uploads/product/${a.linkAnh}" 
                               class="${i === 0 ? "active" : ""}" 
                               onclick="changeImage(this)">`
            )
            .join("");
    }

    // Biến thể (phân loại)
    const variantContainer = document.getElementById("variantContainer");
    if (p.bienThe && p.bienThe.length > 0) {
        variantContainer.innerHTML = `
                <label>Phân loại:</label>
                <div class="variant-list">
                    ${p.bienThe
                .map(
                    (bt) =>
                        `<button class="variant-btn">${bt.mauSac} - ${bt.kichThuoc}</button>`
                )
                .join("")}
                </div>
            `;
    }

    // Sự kiện thêm vào giỏ
    document.getElementById("addToCartBtn").addEventListener("click", () => {
        alert(`🛒 Đã thêm "${p.tenSp}" vào giỏ hàng!`);
    });


} catch (err) {
    console.error(err);
    document.body.innerHTML = "<h2>Lỗi khi tải sản phẩm!</h2>";
}



// Hàm đổi ảnh
function changeImage(img) {
    document.getElementById("mainImage").src = img.src;
    document.querySelectorAll(".thumbs img").forEach(i => i.classList.remove("active"));
    img.classList.add("active");
}
