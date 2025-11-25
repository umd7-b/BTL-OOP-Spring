const params = new URLSearchParams(window.location.search);
const keyword = params.get("q") || "";

document.getElementById("keyword").textContent = keyword;

async function loadSearchResult() {
    const res = await fetch(`http://localhost:8081/api/sanpham/search?keyword=${keyword}`);
    const products = await res.json();

    const box = document.getElementById("searchResultList");

    if (!products.length) {
        box.innerHTML = `<p class="no-products">Không tìm thấy sản phẩm nào.</p>`;
        return;
    }

    box.innerHTML = products.map(p => `
        <div class="product-card" onclick="viewProduct(${p.maSp})">
            <div class="product-img">
                <img src="${p.anhDaiDien ?? '/assets/img/no-image.jpg'}">
            </div>
            <div class="product-info">
                <h3 class="product-name">${p.tenSp}</h3>
                <div class="product-price">
                    <span class="new-price">${(p.giaKm ?? p.giaGoc).toLocaleString()}₫</span>
                    ${p.giaGoc && p.giaKm && p.giaKm < p.giaGoc
            ? `<span class="old-price">${p.giaGoc.toLocaleString()}₫</span>`
            : ""
        }
                </div>
            </div>
        </div>
    `).join("");
}

function viewProduct(id) {
    window.location.href = `/sanpham/${id}`;
}

loadSearchResult();
