// ===========================
        // FORMAT TIỀN
        // ===========================
        function fmtVND(n) {
            return new Intl.NumberFormat("vi-VN").format(n) + "₫";
        }

        // ===========================
        // LẤY GIỎ HÀNG TỪ BACKEND
        // ===========================
        async function loadCart() {
            try {
                const USER = await fetch("/api/auth/me").then(r => r.json());

                const res = await fetch(`/api/giohang?maKhachHang=${USER.maKhachHang}`);
                return await res.json();
            } catch {
                return [];
            }
        }

        // ===========================
        // RENDER UI
        // ===========================
        async function init() {
            const CART = await loadCart();

            const wrap = document.getElementById("orderItems");
            wrap.innerHTML = CART.map(item => `
                <div class="order-item">
                    <img src="${item.hinhAnh || '/client/images/no-image.png'}" alt="">
                    
                    <div>
                        <div class="order-item-name">${item.tenSp}</div>
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

            // Shipping change
            document.querySelectorAll("input[name='ship']").forEach(r => {
                r.addEventListener("change", () => {
                    const ship = parseInt(r.value);
                    document.getElementById("shippingFee").textContent = fmtVND(ship);
                    document.getElementById("total").textContent = fmtVND(subtotal + ship);
                });
            });

            // Default
            document.getElementById("total").textContent =
                fmtVND(subtotal + parseInt(document.querySelector("input[name='ship']:checked").value));

            // Confirm
            document.getElementById("confirmBtn").addEventListener("click", () => {
                    showToast("Đặt hàng thành công! Cảm ơn bạn đã mua sắm tại Lowtech Store.", "success");
            });
        }

        document.addEventListener("DOMContentLoaded", init);
        function showToast(message, type = 'success', opts = {}) {
    const root = document.getElementById('toast-root');
    if (!root) return;

    const duration = opts.duration ?? 2500; // ms

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
