async function loadProductDetail() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    if (!id) {
        document.getElementById("productDetail").innerHTML = "<p>Không tìm thấy sản phẩm.</p>";
        return;
    }
    
    try {
        const response = await fetch(`http://localhost:8081/api/sanpham/${id}`);
        if (!response.ok) throw new Error("Không thể tải sản phẩm");

        const p = await response.json();

        const imgSrc = p.anhDaiDien ? `http://localhost:8081${p.anhDaiDien}` : "/assets/img/no-image.jpg";
        const gia = p.giaKm ?? p.giaGoc ?? 0;
        const giaGoc =
            p.giaGoc && p.giaKm && p.giaKm < p.giaGoc
                ? `<span class="old-price">${p.giaGoc.toLocaleString()}₫</span>`
                : "";

        document.getElementById("productDetail").innerHTML = `
      <div class="detail-container">
        <div class="detail-image">
          <img src="${imgSrc}" alt="${p.tenSp}" />
        </div>
        <div class="detail-info">
          <h2>${p.tenSp}</h2>
          <p class="price">
            <span class="new-price">${gia.toLocaleString()}₫</span>
            ${giaGoc}
          </p>
          <p class="brand"><b>Thương hiệu:</b> ${p.thuongHieu?.tenThuongHieu ?? "Không rõ"}</p>
          <p class="desc">${p.moTa ?? "Chưa có mô tả"}</p>
          <button class="add-to-cart">Thêm vào giỏ hàng</button>
        </div>
      </div>
    `;
    } catch (err) {
        console.error(err);
        document.getElementById("productDetail").innerHTML = "<p>Lỗi khi tải chi tiết sản phẩm.</p>";
    }
}

loadProductDetail();
