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
                        .map((item) => `
        <li>
            <a href="#"
               class="dropdown-item"
               data-id="${item.maThuongHieu || item.maMonTheThao || item.maDanhMuc}"
               data-type="${labelKey}">
               ${item[labelKey]}
            </a>
        </li>
    `)
                        .join("");
                    list.querySelectorAll(".dropdown-item").forEach(a => {
                        a.addEventListener("click", (e) => {
                            e.preventDefault();
                            list.classList.remove("show");

                            const id = a.dataset.id;
                            const type = a.dataset.type;

                            // Gán vào filter tương ứng
                            if (type === "tenThuongHieu") {
                                document.getElementById("filterBrand").value = id;
                            }
                            else if (type === "tenMonTheThao") {
                                document.getElementById("filterSport").value = id;
                            }
                            else if (type === "tenDanhMuc") {
                                // Nếu có filter danh mục thì set, nếu không thì bỏ qua
                            }

                            // Gọi API lọc sản phẩm
                            filterProducts();
                        });
                    });


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

                // 🟢 Thêm onclick để chuyển sang trang chi tiết sản phẩm
                return `
                <div class="product-card" onclick="viewProduct(${p.maSp})">
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
    // ========== FILTER SẢN PHẨM ==========
    const filterBrand = document.getElementById("filterBrand");
    const filterSport = document.getElementById("filterSport");
    const filterPrice = document.getElementById("filterPrice");
    const allBtn = document.getElementById("allBtn");

    // load danh sách filter (thương hiệu + môn thể thao)
    async function loadFilterOptions() {
        try {
            const [brands, sports] = await Promise.all([
                fetch("http://localhost:8081/api/thuonghieu/all").then((res) => res.json()),
                fetch("http://localhost:8081/api/monthethao/all").then((res) => res.json()),
            ]);

            if (filterBrand) {
                filterBrand.innerHTML += brands
                    .map((b) => `<option value="${b.maThuongHieu}">${b.tenThuongHieu}</option>`)
                    .join("");
            }

            if (filterSport) {
                filterSport.innerHTML += sports
                    .map((s) => `<option value="${s.maMonTheThao}">${s.tenMonTheThao}</option>`)
                    .join("");
            }
        } catch (e) {
            console.error("Lỗi tải danh sách filter:", e);
        }
    }

    loadFilterOptions();

    // gọi API filter sản phẩm
    async function filterProducts() {
        const brand = filterBrand?.value || "";
        const sport = filterSport?.value || "";
        const price = filterPrice?.value || "";
        let giaMin = "",
            giaMax = "";

        if (price) [giaMin, giaMax] = price.split("-");

        const params = new URLSearchParams({
            thuongHieu: brand,
            monTheThao: sport,
            giaMin: giaMin,
            giaMax: giaMax,
        });

        const apiUrl = `http://localhost:8081/api/sanpham/filter?${params.toString()}`;
        console.log("Đang gọi API:", apiUrl);

        try {
            const res = await fetch(apiUrl);
            const products = await res.json();
            if (products.length === 0) {
                grid.innerHTML = `<p class="no-products">Không có sản phẩm phù hợp.</p>`;
            } else {
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
            <div class="product-card" onclick="viewProduct(${p.maSp})">
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
            </div>`;
                    })
                    .join("");
            }
            grid.scrollIntoView({ behavior: "smooth" });

        } catch (e) {
            console.error("Lỗi khi lọc sản phẩm:", e);
        }
    }

    // gắn sự kiện
    if (filterBrand) filterBrand.addEventListener("change", filterProducts);
    if (filterSport) filterSport.addEventListener("change", filterProducts);
    if (filterPrice) filterPrice.addEventListener("change", filterProducts);
    if (allBtn)
        allBtn.addEventListener("click", async () => {
            const res = await fetch("http://localhost:8081/api/sanpham/all");
            const products = await res.json();
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
          <div class="product-card" onclick="viewProduct(${p.maSp})">
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
          </div>`;
                })
                .join("");
            if (filterBrand) filterBrand.value = "";
            if (filterSport) filterSport.value = "";
            if (filterPrice) filterPrice.value = "";
        });



});

// 🟢 Hàm chuyển sang trang chi tiết sản phẩm
function viewProduct(id) {
    window.location.href = `/sanpham/${id}`;
}
let currentSlide = 0;
const slides = document.querySelectorAll(".slide");
const dots = document.querySelectorAll(".dot");

// ============ REALTIME SEARCH DROPDOWN SHOPEE STYLE ============
function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

const searchInputEl = document.getElementById("searchInput");
const resultBox = document.getElementById("searchResult");

async function realtimeSearch() {
    const keyword = searchInputEl.value.trim();

    if (keyword.length === 0) {
        resultBox.style.display = "none";
        return;
    }

    try {
        const res = await fetch(`http://localhost:8081/api/sanpham/search?keyword=${keyword}`);
        const products = await res.json();

        if (!products || products.length === 0) {
            resultBox.innerHTML = `<div class="search-item">Không tìm thấy sản phẩm</div>`;
            resultBox.style.display = "block";
            return;
        }
        resultBox.innerHTML =
            products
                .map((p) => {
                    const img = p.anhDaiDien ? p.anhDaiDien : "/assets/img/no-image.jpg";
                    const name = p.tenSp;
                    const price = p.giaKm ?? p.giaGoc ?? 0;
                    const oldPrice = (p.giaGoc && p.giaKm && p.giaKm < p.giaGoc) ? p.giaGoc : null;

                    return `
                <div class="search-item" onclick="viewProduct(${p.maSp})">
                    <img src="${img}" alt="${name}">
                    <div class="search-info">
                        <span class="search-name">${name}</span>
                        <div>
                            <span class="search-price">${price.toLocaleString()}₫</span>
                            ${oldPrice
                            ? `<span class="search-price-old">${oldPrice.toLocaleString()}₫</span>`
                            : ""
                        }
                        </div>
                    </div>
                </div>
            `;
                })
                .join("")
            +
            `
    <div class="search-show-all"
         onclick="window.location.href='/search?q=${keyword}'">
         Xem tất cả sản phẩm
    </div>
    `;



        resultBox.style.display = "block";
    } catch (err) {
        console.error("Search error:", err);
    }
}

searchInputEl.addEventListener("input", debounce(realtimeSearch, 250));

document.addEventListener("click", (e) => {
    if (!searchInputEl.contains(e.target) && !resultBox.contains(e.target)) {
        resultBox.style.display = "none";
    }
});


