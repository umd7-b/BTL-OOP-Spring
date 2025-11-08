// Ngăn chặn lỗi với extension
        (function() {
            'use strict';
            
            // Override để tránh conflict với extension
            const originalAddEventListener = EventTarget.prototype.addEventListener;
            EventTarget.prototype.addEventListener = function(type, listener, options) {
                if (typeof listener === 'function') {
                    originalAddEventListener.call(this, type, listener, options);
                }
            };
        })();

        // API endpoints
        const API = {
            ME: "/api/auth/me",
            CART: "/api/giohang",
            UPDATE_QTY: "/api/giohang/capnhatsl",
            DELETE_ITEM: "/api/giohang/xoa"
        };

        let USER = null;
        let CART = [];

        // Format tiền VND
        function formatVND(amount) {
            return new Intl.NumberFormat('vi-VN').format(amount) + '₫';
        }

        // Lấy thông tin user
        async function fetchUser() {
            try {
                const res = await fetch(API.ME);
                if (!res.ok) return null;
                return await res.json();
            } catch (error) {
                console.error('Lỗi lấy thông tin user:', error);
                return null;
            }
        }

        // Lấy giỏ hàng
        async function fetchCart() {
            if (!USER) {
                window.location.href = '/login';
                return;
            }

            try {
                const res = await fetch(`${API.CART}?maKhachHang=${USER.maKhachHang}`);
                if (!res.ok) {
                    CART = [];
                    renderCart();
                    return;
                }
                
                const data = await res.json();
                CART = Array.isArray(data) ? data : [];
                renderCart();
            } catch (error) {
                console.error('Lỗi lấy giỏ hàng:', error);
                CART = [];
                renderCart();
            }
        }

        // Render giỏ hàng
        function renderCart() {
            const emptyCart = document.getElementById('emptyCart');
            const cartItemsList = document.getElementById('cartItemsList');
            const cartCount = document.getElementById('cartCount');

            if (!emptyCart || !cartItemsList || !cartCount) return;

            cartCount.textContent = CART.length;

            if (CART.length === 0) {
                emptyCart.style.display = 'block';
                cartItemsList.innerHTML = '';
                updateSummary();
                return;
            }

            emptyCart.style.display = 'none';

            cartItemsList.innerHTML = CART.map(item => `
                <div class="cart-item" data-id="${item.maCtGioHang}" data-variant="${item.maBienThe}">
                    <img src="${item.hinhAnh || '/client/images/no-image.png'}" 
                         alt="${item.tenSp || 'Sản phẩm'}" 
                         class="item-image"
                         onerror="this.src='/client/images/no-image.png'">
                    
                    <div class="item-info">
                        <div class="item-name">${item.tenSp || 'Sản phẩm'}</div>
                        <div class="item-variant">${item.phanLoaiText || ''}</div>
                        <div class="item-price">${formatVND(item.gia || 0)}</div>
                    </div>

                    <div class="item-actions">
                        <button class="remove-btn" data-variant="${item.maBienThe}" type="button">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                            </svg>
                        </button>
                        
                        <div class="qty-controls">
                            <button class="qty-btn qty-minus" 
                                    data-id="${item.maCtGioHang}" 
                                    data-qty="${item.soLuong - 1}"
                                    type="button">−</button>
                            <span class="qty-value">${item.soLuong || 1}</span>
                            <button class="qty-btn qty-plus" 
                                    data-id="${item.maCtGioHang}" 
                                    data-qty="${item.soLuong + 1}"
                                    type="button">+</button>
                        </div>
                    </div>
                </div>
            `).join('');

            // Gắn event listeners sau khi render
            attachEventListeners();
            updateSummary();
        }

        // Gắn event listeners
        function attachEventListeners() {
            // Xóa sản phẩm
            document.querySelectorAll('.remove-btn').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    const maBienThe = parseInt(this.getAttribute('data-variant'));
                    if (maBienThe) deleteItem(maBienThe);
                });
            });

            // Tăng/giảm số lượng
            document.querySelectorAll('.qty-btn').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    const maCtGioHang = parseInt(this.getAttribute('data-id'));
                    const newQty = parseInt(this.getAttribute('data-qty'));
                    if (maCtGioHang && newQty > 0) {
                        updateQty(maCtGioHang, newQty);
                    }
                });
            });
        }

        // Cập nhật tóm tắt đơn hàng
        function updateSummary() {
            const subtotal = CART.reduce((sum, item) => {
                const price = parseFloat(item.gia) || 0;
                const qty = parseInt(item.soLuong) || 0;
                return sum + (price * qty);
            }, 0);
            
            const subtotalEl = document.getElementById('subtotalPrice');
            const totalEl = document.getElementById('totalPrice');
            
            if (subtotalEl) subtotalEl.textContent = formatVND(subtotal);
            if (totalEl) totalEl.textContent = formatVND(subtotal);
        }

        // Cập nhật số lượng
        async function updateQty(maCtGioHang, newQty) {
            if (newQty < 1) return;

            try {
                const res = await fetch(`${API.UPDATE_QTY}?maCtGioHang=${maCtGioHang}&soLuong=${newQty}`, {
                    method: 'PUT'
                });

                if (res.ok) {
                    await fetchCart();
                } else {
                    console.error('Lỗi cập nhật số lượng');
                    alert('Không thể cập nhật số lượng!');
                }
            } catch (error) {
                console.error('Lỗi cập nhật số lượng:', error);
                alert('Lỗi kết nối server!');
            }
        }

        // Xóa sản phẩm
        async function deleteItem(maBienThe) {
            if (!confirm('Bạn có chắc muốn xóa sản phẩm này?')) return;

            try {
                const res = await fetch(`${API.DELETE_ITEM}?maKhachHang=${USER.maKhachHang}&maBienThe=${maBienThe}`, {
                    method: 'DELETE'
                });

                if (res.ok) {
                    await fetchCart();
                } else {
                    console.error('Lỗi xóa sản phẩm');
                    alert('Không thể xóa sản phẩm!');
                }
            } catch (error) {
                console.error('Lỗi xóa sản phẩm:', error);
                alert('Lỗi kết nối server!');
            }
        }

        // Khởi tạo khi DOM ready
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', init);
        } else {
            init();
        }

        async function init() {
            USER = await fetchUser();
            
            if (!USER) {
                alert('Vui lòng đăng nhập để xem giỏ hàng!');
                window.location.href = '/login';
                return;
            }

            await fetchCart();

            // Thanh toán
            const checkoutBtn = document.getElementById('checkoutBtn');
            if (checkoutBtn) {
                checkoutBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    if (CART.length === 0) {
                        alert('Giỏ hàng trống!');
                        return;
                    }
                    // TODO: Chuyển đến trang thanh toán
                    alert('Chức năng thanh toán đang được phát triển!');
                });
            }
        }