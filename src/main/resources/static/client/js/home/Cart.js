// =====================================================
//  ✅ API Chuẩn theo backend của bạn
// =====================================================
const API = {
    ME: "/api/auth/me",
    CART: "/api/giohang",
    ADD: "/api/giohang/add",
    UPDATE_QTY: "/api/giohang/capnhatsl",
    DELETE_ITEM: "/api/giohang/xoa",
    TOGGLE_SELECT: "/api/giohang/chon",
    CHECK_STOCK: "/api/bienthe"
};

// ====================== UTIL ==========================
function $(sel, root = document) { return root.querySelector(sel); }
function $$(sel, root = document) { return Array.from(root.querySelectorAll(sel)); }
function fmtVND(n) { return new Intl.NumberFormat("vi-VN").format(n) + "₫"; }

let USER = null;
let CART = [];


// =====================================================
//  ✅ Toast notification
// =====================================================
function showToast(msg = "Thành công!") {
    const toast = document.getElementById("toast");
    if (!toast) {
        console.log("Toast:", msg);
        return;
    }

    toast.textContent = msg;
    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 2500);
}


// =====================================================
//  ✅ Kiểm tra tồn kho của biến thể
// =====================================================
async function checkStock(maBienThe) {
    try {
        const res = await fetch(`${API.CHECK_STOCK}/${maBienThe}`);
        if (!res.ok) return null;
        
        return await res.json();
    } catch (error) {
        console.error("Lỗi kiểm tra tồn kho:", error);
        return null;
    }
}


// =====================================================
//  ✅ Lấy thông tin user
// =====================================================
async function fetchUser() {
    try {
        const res = await fetch(API.ME);
        if (!res.ok) {
            console.log("👤 Người dùng chưa đăng nhập");
            return null;
        }
        return await res.json();
    } catch (error) {
        console.log("👤 Lỗi lấy thông tin user:", error.message);
        return null;
    }
}


// =====================================================
//  ✅ Lấy giỏ hàng theo user
// =====================================================
async function fetchCart() {
    if (!USER) {
        CART = [];
        renderCartUI();
        return;
    }

    try {
        const res = await fetch(`${API.CART}?maKhachHang=${USER.maKhachHang}`);
        if (!res.ok) {
            CART = [];
            renderCartUI();
            return;
        }
        CART = await res.json();
        renderCartUI();
    } catch (error) {
        console.error("Lỗi lấy giỏ hàng:", error);
        CART = [];
        renderCartUI();
    }
}


// =====================================================
//  ✅ Thêm vào giỏ hàng (có kiểm tra tồn kho)
// =====================================================
async function addToCartDB(variantId, qty, price) {
    try {
        // ⭐ KIỂM TRA TỒN KHO TRƯỚC
        const stockInfo = await checkStock(variantId);
        if (!stockInfo) {
            showToast("❌ Không thể kiểm tra tồn kho sản phẩm!");
            return;
        }

        if (!stockInfo.soLuongTon || stockInfo.soLuongTon <= 0) {
            showToast("❌ Sản phẩm này đã hết hàng!");
            return;
        }

        if (stockInfo.soLuongTon < qty) {
            showToast(`❌ Chỉ còn ${stockInfo.soLuongTon} sản phẩm trong kho!`);
            return;
        }

        // ✅ Gọi API thêm vào giỏ
        const url = `${API.ADD}?maKhachHang=${USER.maKhachHang}&maBienThe=${variantId}&soLuong=${qty}&gia=${price}`;
        
        console.log("🛒 Gọi API:", url);

        const res = await fetch(url, { 
            method: "POST",
            headers: {
                "Content-Type": "application/json"
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
            showToast(`Lỗi: ${errorText || "Không thể thêm vào giỏ hàng"}`);
        }

    } catch (error) {
        console.error("❌ Network error:", error);
        showToast("❌ Không thể kết nối đến server!");
    }
}


// =====================================================
//  ✅ Cập nhật số lượng (có kiểm tra tồn kho)
// =====================================================
async function updateQtyDB(maCtGioHang, newQty) {
    try {
        const item = CART.find(i => i.maCtGioHang === maCtGioHang);
        if (!item) return;

        // ⭐ Kiểm tra tồn kho
        const stockInfo = await checkStock(item.maBienThe);
        if (!stockInfo) {
            showToast("❌ Không thể kiểm tra tồn kho!");
            await fetchCart();
            return;
        }

        if (stockInfo.soLuongTon < newQty) {
            showToast(`❌ Chỉ còn ${stockInfo.soLuongTon} sản phẩm trong kho!`);
            await fetchCart();
            return;
        }

        // ✅ Cập nhật
        const url = `${API.UPDATE_QTY}?maCtGioHang=${maCtGioHang}&soLuong=${newQty}`;
        const res = await fetch(url, { method: "PUT" });
        
        if (res.ok) {
            await fetchCart();
            showToast("✅ Đã cập nhật số lượng!");
        } else {
            console.error("Lỗi cập nhật số lượng");
            showToast("❌ Không thể cập nhật số lượng!");
        }
    } catch (error) {
        console.error("Network error:", error);
        showToast("❌ Lỗi kết nối!");
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
            showToast("✅ Đã xóa sản phẩm!");
        } else {
            console.error("Lỗi xóa sản phẩm");
            showToast("❌ Không thể xóa sản phẩm!");
        }
    } catch (error) {
        console.error("Network error:", error);
        showToast("❌ Lỗi kết nối!");
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
    showToast("⚠️ Bạn cần đăng nhập để thêm vào giỏ hàng!");
    setTimeout(() => {
        window.location.href = "/login";
    }, 1500);
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
    const addBtns = $$(".btn-primary");
    const addBtn = addBtns.find(btn => /Thêm vào giỏ/i.test(btn.textContent));
    if (addBtn) {
        addBtn.addEventListener("click", async () => {

            // ⭐ KIỂM TRA ĐĂNG NHẬP NGAY TỪ ĐẦU
            if (!USER) {
                return redirectToLogin();
            }

            // Kiểm tra đã chọn phân loại chưa
            const variantBtn = $(".variant-btn.active");
            if (!variantBtn) {
                showToast("⚠️ Bạn chưa chọn phân loại sản phẩm!");
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

        
            console.log("Bạn đã chọn sản phẩm vào giỏ ", {
                maKhachHang: USER.maKhachHang,
                maBienThe: variantId,
                soLuong: qty,
                gia: price
            });

            // Validate
            if (!variantId || isNaN(variantId)) {
                showToast("❌ Mã biến thể không hợp lệ!");
                return;
            }
            if (!qty || qty < 1) {
                showToast("❌ Số lượng phải >= 1!");
                return;
            }
            if (!price || isNaN(price) || price <= 0) {
                showToast("❌ Giá sản phẩm không hợp lệ!");
                return;
            }

            // Gọi API
            await addToCartDB(variantId, qty, price);
        });
    }

    // 👉 Nút "Mua ngay"
    const buyNowBtn = $("#buyNowBtn");
    if (buyNowBtn) {
        buyNowBtn.addEventListener("click", async () => {

            if (!USER) return redirectToLogin();

            // ✅ Kiểm tra biến thể
            const variantBtn = $(".variant-btn.active");
            if (!variantBtn) {
                showToast("⚠️ Bạn chưa chọn phân loại sản phẩm!");
                return;
            }

            const variantId = Number(variantBtn.getAttribute("data-bienthe-id"));
            const qty = Number($("#qty").value || 1);

            const priceText = $(".price .new").textContent.replace(/[^\d]/g, "");
            const price = Number(priceText);

            // ⭐ KIỂM TRA TỒN KHO
            const stockInfo = await checkStock(variantId);
            if (!stockInfo) {
                showToast("❌ Không thể kiểm tra tồn kho!");
                return;
            }

            if (!stockInfo.soLuongTon || stockInfo.soLuongTon <= 0) {
                showToast("❌ Sản phẩm này đã hết hàng!");
                return;
            }

            if (stockInfo.soLuongTon < qty) {
                showToast(`❌ Chỉ còn ${stockInfo.soLuongTon} sản phẩm trong kho!`);
                return;
            }

            // ✅ Lưu vào SESSION — không ảnh hưởng giỏ
            const buyNowItem = {
                maBienThe: variantId,
                soLuong: qty,
            };

            sessionStorage.setItem("BUY_NOW", JSON.stringify(buyNowItem));

            // ✅ Điều hướng payment
            window.location.href = "/payment";
        });
    }

    // 👉 Nút thanh toán trong giỏ hàng
    const checkoutBtn = $("#checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", () => {
            sessionStorage.removeItem("BUY_NOW");
            if (CART.length === 0) {
                showToast("⚠️ Giỏ hàng trống!");
                return;
            }
            window.location.href = "/checkout";
        });
    }
});