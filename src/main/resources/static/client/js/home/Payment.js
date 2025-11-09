// ===========================
        // FORMAT TIỀN
        // ===========================
        function fmtVND(n) {
            return new Intl.NumberFormat("vi-VN").format(n) + "₫";
        }


        // Freeship: luôn 0
function getShipFee() { return 0; }

// Tạo đơn + xoá giỏ + điều hướng /order
async function createOrder(CART) {
  const me = await fetch("/api/auth/me");
  if (!me.ok) throw new Error("Không lấy được thông tin người dùng");
  const USER = await me.json();

  const fullname = document.getElementById("fullname")?.value.trim() || "";
  const phone = document.getElementById("phone")?.value.trim() || "";
  const address = document.getElementById("address")?.value.trim() || "";

  // ✅ VALIDATION
  if (!fullname) {
     
      document.getElementById("fullname").focus();
      throw new Error("Vui lòng nhập họ và tên người nhận!");
  }

  if (!phone) {
 
      document.getElementById("phone").focus();
      throw new Error("Vui lòng nhập số điện thoại!");
  }

  const phoneRegex = /^(0|\+84)(\d{9})$/;
  if (!phoneRegex.test(phone)) {

      document.getElementById("phone").focus();
      throw new Error("Số điện thoại không hợp lệ!");
  }

  if (!address) {

      document.getElementById("address").focus();
      throw new Error("Vui lòng nhập địa chỉ giao hàng!");
  }

  // ✅ Fetch tạo đơn hàng
  const res = await fetch("/api/donhang/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
        maKhachHang: USER.maKhachHang,
        tenNguoiNhan: fullname,
        sdtNguoiNhan: phone,
        diaChiNguoiNhan: address,
        phuongThucThanhToan: "Thanh toán khi nhận hàng",
        items: CART.map(i => ({
            maBienThe: i.maBienThe,
            soLuong: i.soLuong,
            donGia: i.gia
        }))
    })
  });

 


  const data = await res.json().catch(() => ({}));
  if (!res.ok || !data.maDonHang) {
  
    throw new Error(data.message || `Tạo đơn thất bại (HTTP ${res.status})`);
  }
   showToast(`🎉 Đặt hàng thành công! Mã đơn hàng #${data.maDonHang}`, "success");
  // Xoá giỏ (nếu server chưa tự clear)
  try {
    await fetch(`/api/giohang/clear?maKhachHang=${USER.maKhachHang}`, { method: "DELETE" });
    // hoặc: await fetch(`/api/giohang?maKhachHang=${USER.maKhachHang}`, { method: "DELETE" });
  } catch {}
 

    setTimeout(() => {
        window.location.href = "/order";
    }, 1200);
}   





        // ===========================

        async function loadCart() {
    try {
        // ✅ 1. BUY NOW → ƯU TIÊN
        const buyNowStr = sessionStorage.getItem("BUY_NOW");
        if (buyNowStr) {
            const item = JSON.parse(buyNowStr);

            // ✅ FETCH thông tin biến thể để lấy giá, ảnh, tên SP...
            const res = await fetch(`/api/bienthe/${item.maBienThe}`);
            const bt = await res.json();

            return {
                type: "BUY_NOW",
                items: [
                    {
                        maBienThe: item.maBienThe,
                        soLuong: item.soLuong,
                        gia: bt.gia,                   // ✅ giá từ BE
                        tenSp: bt.tenSp,               // ✅ tên SP
                        phanLoaiText: bt.phanLoaiText, // ✅ màu + size
                        hinhAnh: `/uploads/products/${bt.anhSp}` // ✅ ảnh
                    }
                ]
            };
        }

        // ✅ 2. Không BUY NOW → lấy giỏ hàng từ DB
        const USER = await fetch("/api/auth/me").then(r => r.json());
        const res = await fetch(`/api/giohang?maKhachHang=${USER.maKhachHang}`);
        const cartItems = await res.json();

        return {
            type: "CART",
            items: cartItems
        };

    } catch (err) {
        console.error("Lỗi loadCart()", err);
        return { type: "CART", items: [] };
    }
}

        // ===========================
        // RENDER UI
        // ===========================
      async function init() {
    const data = await loadCart();   // {type, items}
    const CART = data.items;

    const wrap = document.getElementById("orderItems");

    wrap.innerHTML = CART.map(item => `
        <div class="order-item">
            <img src="${item.hinhAnh || '/client/images/no-image.png'}" alt="">
            <div>
                <div class="order-item-name">${item.tenSp || ""}</div>
                <div class="order-item-variant">${item.phanLoaiText || ""}</div>
                <div class="order-item-qty">Số lượng: ${item.soLuong}</div>
            </div>
            <div class="order-item-price">
                ${fmtVND(item.gia * item.soLuong)}
            </div>
        </div>
    `).join("");

    const subtotal = CART.reduce((s, i) => s + i.gia * i.soLuong, 0);

    document.getElementById("subtotal").textContent = fmtVND(subtotal);
    document.getElementById("shippingFee").textContent = "Miễn phí";
    document.getElementById("total").textContent = fmtVND(subtotal);

    // ✅ Tạo đơn
    const btn = document.getElementById("confirmBtn");
    btn.addEventListener("click", async () => {
        const original = btn.textContent;
        try {
            btn.disabled = true;
            btn.textContent = "⏳ Đang xử lý...";
            await createOrder(CART);

            // ✅ Nếu là BUY NOW → xóa buy now
            if (data.type === "BUY_NOW") {
                sessionStorage.removeItem("BUY_NOW");
            }

        } catch (e) {
            showToast(e.message, "error");
            btn.disabled = false;
            btn.textContent = original;
        }
    });
}

document.addEventListener("DOMContentLoaded", init);


        document.addEventListener("DOMContentLoaded", init);
        function showToast(message, type = 'success', opts = {}) {
    const root = document.getElementById('toast-root');
    if (!root) return;

    const duration = opts.duration ?? 4000; // ms

    const el = document.createElement('div');
    el.className = `toast ${type}`;

    const icon = type === 'success' ? '✅'
               : type === 'error'   ? '⚠️'
               : 'ℹ️';

    el.innerHTML = `
      <div class="toast__icon">${icon}</div>
      <div class="toast__msg">${message}</div>
      <div class="toast__close" aria-label="Đóng">✖</div>
    `;

    // Đóng khi bấm X
    el.querySelector('.toast__close').addEventListener('click', () => removeToast(el));

    root.appendChild(el);

    // Tự ẩn
    const t = setTimeout(() => removeToast(el), duration);

    // Dừng timer khi hover
    el.addEventListener('mouseenter', () => clearTimeout(t));
    el.addEventListener('mouseleave', () => {
      // ẩn nhanh sau khi rời chuột
      setTimeout(() => removeToast(el), 800);
    });

    function removeToast(node){
      if (!node) return;
      node.style.animation = 'toast-out .15s ease-in forwards';
      node.addEventListener('animationend', () => node.remove(), { once:true });
    }
  }
