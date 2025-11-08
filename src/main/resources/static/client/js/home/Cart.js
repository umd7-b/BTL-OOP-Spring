// =====================================================
//  ✅ API Chuẩn theo backend của bạn
// =====================================================
const API = {
    ME: "/api/auth/me",
    CART: "/api/giohang",                         // GET ?maKhachHang=
    ADD: "/api/giohang/add",                      // POST
    UPDATE_QTY: "/api/giohang/capnhatsl",         // PUT
    DELETE_ITEM: "/api/giohang/xoa",              // DELETE
    TOGGLE_SELECT: "/api/giohang/chon"            // PUT
};

// ====================== UTIL ==========================
function $(sel, root = document) { return root.querySelector(sel); }
function $$(sel, root = document) { return Array.from(root.querySelectorAll(sel)); }
function fmtVND(n) { return new Intl.NumberFormat("vi-VN").format(n) + "₫"; }

let USER = null;
let CART = [];


// =====================================================
//  ✅ Lấy thông tin user
// =====================================================
async function fetchUser() {
    try {
        const res = await fetch(API.ME);
        if (!res.ok) return null;
        return await res.json();
    } catch {
        return null;
    }
}

// =====================================================
//  ✅ Lấy giỏ hàng theo user
// =====================================================
async function fetchCart() {
    if (!USER) return;

    try {
        const res = await fetch(`${API.CART}?maKhachHang=${USER.maKhachHang}`);
        if (!res.ok) {
            CART = [];
            return;
        }
        CART = await res.json();
        renderCartUI();
    } catch (error) {
        console.error("Lỗi lấy giỏ hàng:", error);
        CART = [];
    }
}


// =====================================================
//  ✅ Thêm vào giỏ hàng (query params - khớp backend)
// =====================================================
async function addToCartDB(variantId, qty, price) {
    if (!USER) return redirectToLogin();

    try {
        // ⚠️ QUAN TRỌNG: Backend nhận query params, không phải JSON body
        // Backend signature: addToCart(@RequestParam Long maKhachHang, @RequestParam Integer maBienThe, ...)
        const url = `${API.ADD}?maKhachHang=${USER.maKhachHang}&maBienThe=${variantId}&soLuong=${qty}&gia=${price}`;
        
        console.log("🛒 Gọi API:", url); // Debug

        const res = await fetch(url, { 
            method: "POST",
            headers: {
                "Content-Type": "application/json" // Giữ header này cho chuẩn
            }
        });

        if (res.ok) {
            const result = await res.json();
            console.log("✅ Thêm thành công:", result);
              showToast("✅ Đã thêm sản phẩm vào giỏ!");
            await fetchCart();
            openCart();
        } else {
            const errorText = await res.text();
            console.error("❌ Backend error:", errorText);
            alert(`Lỗi: ${errorText || "Không thể thêm vào giỏ hàng"}`);
        }

    } catch (error) {
        console.error("❌ Network error:", error);
        alert("Không thể kết nối đến server!");
    }
}


// =====================================================
//  ✅ Cập nhật số lượng
// =====================================================
async function updateQtyDB(maCtGioHang, newQty) {
    try {
        const url = `${API.UPDATE_QTY}?maCtGioHang=${maCtGioHang}&soLuong=${newQty}`;
        const res = await fetch(url, { method: "PUT" });
        
        if (res.ok) {
            await fetchCart();
        } else {
            console.error("Lỗi cập nhật số lượng");
        }
    } catch (error) {
        console.error("Network error:", error);
    }
}


// =====================================================
//  ✅ Xóa item
// =====================================================
async function deleteItemDB(maBienThe) {
    try {
        const url = `${API.DELETE_ITEM}?maKhachHang=${USER.maKhachHang}&maBienThe=${maBienThe}`;
        const res = await fetch(url, { method: "DELETE" });
        
        if (res.ok) {
            await fetchCart();
        } else {
            console.error("Lỗi xóa sản phẩm");
        }
    } catch (error) {
        console.error("Network error:", error);
    }
}


// =====================================================
//  ✅ RENDER UI
// =====================================================
function renderCartUI() {
    const badge = $("#cartCount");
    if (badge) badge.textContent = CART.length;

    const wrap = $("#cartItems");
    const subtotalEl = $("#subtotal");

    let subtotal = CART.reduce((s, item) => s + item.gia * item.soLuong, 0);
    if (subtotalEl) subtotalEl.textContent = fmtVND(subtotal);

    if (!wrap) return;

    if (CART.length === 0) {
        wrap.innerHTML = `<div class="empty">Giỏ hàng trống</div>`;
        return;
    }

    wrap.innerHTML = CART.map(item => `
        <div class="cart-item" data-id="${item.maCtGioHang}" data-variant="${item.maBienThe}">
            <img src="${item.hinhAnh || '/client/images/no-image.png'}" alt="">
            <div class="ci-info">
                <div class="ci-title">${item.tenSp || 'Sản phẩm'}</div>
                <div class="ci-variant">${item.phanLoaiText || ''}</div>
                <div class="ci-price">${fmtVND(item.gia)}</div>
            </div>

            <div class="ci-qty">
                <button class="qty-dec">−</button>
                <input type="number" class="qty-input" min="1" value="${item.soLuong}">
                <button class="qty-inc">+</button>
            </div>

            <button class="ci-remove">✕</button>
        </div>
    `).join("");

    bindCartEvents();
}


// =====================================================
//  ✅ Event trên từng dòng cart
// =====================================================
function bindCartEvents() {
    $$(".cart-item").forEach(row => {
        const id = parseInt(row.dataset.id);
        const variant = parseInt(row.dataset.variant);

        const input = row.querySelector(".qty-input");
        const dec = row.querySelector(".qty-dec");
        const inc = row.querySelector(".qty-inc");
        const rm = row.querySelector(".ci-remove");

        dec.addEventListener("click", () => {
            const v = Math.max(1, parseInt(input.value) - 1);
            input.value = v;
            updateQtyDB(id, v);
        });

        inc.addEventListener("click", () => {
            const v = parseInt(input.value) + 1;
            input.value = v;
            updateQtyDB(id, v);
        });

        input.addEventListener("change", () => {
            const v = Math.max(1, parseInt(input.value) || 1);
            input.value = v;
            updateQtyDB(id, v);
        });

        rm.addEventListener("click", () => {
            if (confirm("Xóa sản phẩm này khỏi giỏ hàng?")) {
                deleteItemDB(variant);
            }
        });
    });
}


// =====================================================
//  ✅ Drawer UI
// =====================================================
function openCart() { 
    const drawer = $("#cartDrawer");
    if (drawer) drawer.classList.add("show"); 
}

function closeCart() { 
    const drawer = $("#cartDrawer");
    if (drawer) drawer.classList.remove("show"); 
}

function redirectToLogin() {
    alert("Bạn cần đăng nhập để thêm vào giỏ hàng!");
    window.location.href = "/login";
}


// =====================================================
//  ✅ KHỞI CHẠY TRANG
// =====================================================
document.addEventListener("DOMContentLoaded", async () => {

    // Lấy thông tin user và giỏ hàng
    USER = await fetchUser();
    await fetchCart();

    // Mở/đóng giỏ hàng
    const openCartBtn = $("#openCart");
    if (openCartBtn) {
        openCartBtn.addEventListener("click", () => {
            if (!USER) return redirectToLogin();
            openCart();
        });
    }

    const closeCartBtn = $("#closeCart");
    if (closeCartBtn) {
        closeCartBtn.addEventListener("click", closeCart);
    }

    // 👉 Chọn phân loại sản phẩm (variant)
    $$(".variant-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            $$(".variant-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
        });
    });

    // 👉 Nút "Thêm vào giỏ hàng"
    const addBtn = $$(".btn-primary").find(btn => /Thêm vào giỏ/i.test(btn.textContent));
    if (addBtn) {
        addBtn.addEventListener("click", async () => {

            // Kiểm tra đã chọn phân loại chưa
            const variantBtn = $(".variant-btn.active");
            if (!variantBtn) {
                alert("⚠️ Bạn chưa chọn phân loại sản phẩm!");
                return;
            }

            // Lấy dữ liệu
            const variantIdStr = variantBtn.getAttribute("data-bienthe-id");
            const variantId = parseInt(variantIdStr);
            
            const qtyInput = $("#qty");
            const qty = parseInt(qtyInput ? qtyInput.value : 1);
            
            const priceEl = $(".price .new");
            const priceText = priceEl ? priceEl.textContent.replace(/[^\d]/g, "") : "0";
            const price = parseFloat(priceText);

            // Debug log
            console.log("📦 Dữ liệu thêm vào giỏ:", {
                maKhachHang: USER.maKhachHang,
                maBienThe: variantId,
                soLuong: qty,
                gia: price
            });

            // Validate
            if (!variantId || isNaN(variantId)) {
                alert("❌ Mã biến thể không hợp lệ!");
                return;
            }
            if (!qty || qty < 1) {
                alert("❌ Số lượng phải >= 1!");
                return;
            }
            if (!price || isNaN(price) || price <= 0) {
                alert("❌ Giá sản phẩm không hợp lệ!");
                return;
            }

            // Gọi API
            await addToCartDB(variantId, qty, price);
        });
    }

    // 👉 Nút "Mua ngay"
    const buyNowBtn = $("#buyNowBtn");
    if (buyNowBtn) {
        buyNowBtn.addEventListener("click", () => {
            if (!USER) return redirectToLogin();
            
            const variantBtn = $(".variant-btn.active");
            if (!variantBtn) {
                alert("⚠️ Bạn chưa chọn phân loại sản phẩm!");
                return;
            }
            
            // TODO: Chuyển đến trang thanh toán
            alert("Chức năng mua ngay đang được phát triển!");
        });
    }

    // 👉 Nút thanh toán trong giỏ hàng
    const checkoutBtn = $("#checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", () => {
            if (CART.length === 0) {
                alert("Giỏ hàng trống!");
                return;
            }
            // TODO: Chuyển đến trang thanh toán
            window.location.href = "/checkout";
        });
    }
});
function showToast(msg = "Thêm vào giỏ hàng thành công!") {
    const toast = document.getElementById("toast");
    if (!toast) return;

    toast.textContent = msg;
    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 2500);
}
