console.log("✅ ProductDetail JS loaded");

document.addEventListener("DOMContentLoaded", () => {
    // =======================
    // ẢNH CHÍNH + THUMBNAIL
    // =======================
    const mainImage = document.getElementById("mainProductImage");
    const thumbnails = document.querySelectorAll(".thumbs img");

    console.log("Main image:", mainImage);
    console.log("Thumbnails found:", thumbnails.length);

    if (mainImage && thumbnails.length > 0) {
        // Active mặc định ảnh đầu tiên
        thumbnails[0].classList.add("active");

        thumbnails.forEach(thumb => {
            thumb.addEventListener("click", () => {
                mainImage.src = thumb.src;

                thumbnails.forEach(t => t.classList.remove("active"));
                thumb.classList.add("active");
            });
        });
    } else {
        console.warn("Không tìm thấy ảnh chính hoặc ảnh thumbnail! (nhưng vẫn chạy phần biến thể bình thường)");
    }

    // =======================
    // BIẾN THỂ + TỒN KHO
    // =======================

    const variantButtons = document.querySelectorAll(".variant-btn");
    const stockInfo = document.getElementById("stockInfo");   // span hiển thị tồn kho
    const qtyInput = document.getElementById("qty");          // ô số lượng (nếu muốn set max)
    const addToCartBtn = document.getElementById("btnAddToCart"); // nếu có nút thêm giỏ

    console.log("Variant buttons:", variantButtons.length);

    variantButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            // Active button
            variantButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            console.log("Đã chọn biến thể:", btn.textContent);

            // LẤY MÃ BIẾN THỂ TỪ data-bienthe-id
            const maBienThe = btn.dataset.bientheId; // vì HTML dùng data-bienthe-id
            if (!maBienThe) {
                console.warn("Nút biến thể không có data-bienthe-id");
                return;
            }

            try {
                const res = await fetch(`/api/bienthe/${maBienThe}`);
                if (!res.ok) throw new Error("Không lấy được dữ liệu biến thể");

                const data = await res.json();
                console.log("Biến thể nhận được:", data);

                // Cập nhật tồn kho
                if (stockInfo) {
                    if (data.soLuongTon > 0) {
                        stockInfo.textContent = `Còn ${data.soLuongTon} sản phẩm`;
                        stockInfo.style.color = "#22c55e"; // xanh
                    } else {
                        stockInfo.textContent = "Hết hàng";
                        stockInfo.style.color = "#ef4444"; // đỏ
                    }
                }

                // Set max cho input số lượng (nếu muốn)
                if (qtyInput && typeof data.soLuongTon === "number") {
                    qtyInput.max = data.soLuongTon;
                    const current = parseInt(qtyInput.value, 10) || 1;
                    if (current > data.soLuongTon) {
                        qtyInput.value = data.soLuongTon > 0 ? data.soLuongTon : 1;
                    }
                }

                // Disable nút thêm giỏ nếu hết hàng
                if (addToCartBtn && typeof data.soLuongTon === "number") {
                    if (data.soLuongTon <= 0) {
                        addToCartBtn.disabled = true;
                        addToCartBtn.textContent = "Hết hàng";
                    } else {
                        addToCartBtn.disabled = false;
                        addToCartBtn.textContent = "Thêm vào giỏ";
                    }
                }

            } catch (err) {
                console.error("Lỗi khi load biến thể:", err);
                if (stockInfo) {
                    stockInfo.textContent = "Lỗi tải số lượng tồn";
                    stockInfo.style.color = "#ef4444";
                }
            }
        });
    });
});
