console.log("✅ ProductDetail JS loaded");

document.addEventListener("DOMContentLoaded", () => {

    // =======================
    // ẢNH CHÍNH + THUMBNAIL
    // =======================

    const mainImage = document.getElementById("mainProductImage"); 
    // ✅ KHỚP VỚI HTML CỦA BẠN

    const thumbnails = document.querySelectorAll(".thumbs img");
    // ✅ KHỚP VỚI HTML CỦA BẠN

    console.log("Main image:", mainImage);
    console.log("Thumbnails found:", thumbnails.length);

    if (!mainImage || thumbnails.length === 0) {
        console.warn("Không tìm thấy ảnh chính hoặc ảnh thumbnail!");
        return;
    }

    // Active mặc định
    thumbnails[0].classList.add("active");

    thumbnails.forEach(thumb => {
        thumb.addEventListener("click", () => {
            mainImage.src = thumb.src;

            thumbnails.forEach(t => t.classList.remove("active"));
            thumb.classList.add("active");
        });
    });

    // =======================
    // ACTIVE BIẾN THỂ
    // =======================

    const variantButtons = document.querySelectorAll(".variant-btn");

    console.log("Variant buttons:", variantButtons.length);

    variantButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            variantButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            console.log("Đã chọn:", btn.textContent);
        });
    });

});





