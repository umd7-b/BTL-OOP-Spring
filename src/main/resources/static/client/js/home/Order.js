    // Order.js - ĐÃ SỬA LỖI ĐƯỜNG DẪN API

    class OrderManager {
        constructor() {
    this.loadingEl = document.getElementById('loading');
    this.emptyStateEl = document.getElementById('emptyState');
    this.ordersListEl = document.getElementById('ordersList');
    this.toastRoot = document.getElementById('toast-root');

    this.currentTab = "ALL";     // ✅ THÊM
    this.cachedOrders = [];      // ✅ THÊM
    
    this.init();
    this.addTabEvents();         // ✅ THÊM
}

        async init() {
            await this.loadOrders();
        }
        addTabEvents() {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {

            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            this.currentTab = tab.dataset.status;

            this.renderOrders(this.cachedOrders);
        });
    });
}

        // Load danh sách đơn hàng (Hàm này đã đúng)
        async loadOrders() {
            try {
                this.showLoading();
                
                // ✅ ĐÚNG: /api/order/customer
                const response = await fetch('/api/order/customer', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include' 
                });

                if (!response.ok) {
                    if (response.status === 401) {
                        this.showToast('Vui lòng đăng nhập để xem đơn hàng', 'warning');
                        setTimeout(() => {
                            window.location.href = '/login';
                        }, 1500);
                        return;
                    }
                    throw new Error('Không thể tải đơn hàng');
                }

                const orders = await response.json();
                this.cachedOrders = orders; 
                
                this.hideLoading();
                
                if (!orders || orders.length === 0) {
                    this.showEmptyState();
                } else {
                    this.renderOrders(orders);
                }
                
            } catch (error) {
                console.error('Lỗi khi tải đơn hàng:', error);
                this.hideLoading();
                this.showToast('Không thể tải đơn hàng. Vui lòng thử lại sau.', 'error');
            }
        }

        // Render danh sách đơn hàng
        renderOrders(orders) {

    this.ordersListEl.style.display = 'block';
    this.ordersListEl.innerHTML = '';

    let filtered = [...orders];

    if (this.currentTab === "ALL") {
        filtered = filtered.filter(o => o.trangThai !== "DA_HUY");
    }
    else if (this.currentTab === "CHO_XAC_NHAN") {
        filtered = filtered.filter(o => o.trangThai === "CHO_XAC_NHAN");
    }
    else if (this.currentTab === "DA_XAC_NHAN") {
        filtered = filtered.filter(o => o.trangThai === "DA_XAC_NHAN");
    }
    else if (this.currentTab === "DANG_GIAO") {
        filtered = filtered.filter(o =>
            o.trangThai === "DA_XAC_NHAN" || o.trangThai === "DANG_GIAO"
        );
    }
    else if (this.currentTab === "DA_GIAO") {
        filtered = filtered.filter(o =>
            o.trangThai === "DA_GIAO" || o.trangThai === "HOAN_THANH"
        );
    }
    else if (this.currentTab === "DA_HUY") {
        filtered = filtered.filter(o => o.trangThai === "DA_HUY");
    }

    if (filtered.length === 0) {
        this.showEmptyState();
        return;
    }

    filtered.sort((a, b) => new Date(b.ngayDat) - new Date(a.ngayDat));

    filtered.forEach(order => {
        const orderCard = this.createOrderCard(order);
        this.ordersListEl.appendChild(orderCard);
    });
}

        // Tạo card đơn hàng
        createOrderCard(order) {
            const card = document.createElement('div');
            card.className = 'order-card';
            
            const statusInfo = this.getStatusInfo(order.trangThai);
            const formattedDate = this.formatDate(order.ngayDat);
            const formattedTotal = this.formatCurrency(order.tongTien);
            
            card.innerHTML = `
                <div class="order-header">
                    <div>
                        <div class="order-id">Đơn hàng #${order.maDonHang}</div>
                        <div class="order-date">${formattedDate}</div>
                    </div>
                    <div class="order-status ${statusInfo.class}">${statusInfo.text}</div>
                </div>
                
                <div class="order-body">
                    <div class="order-items" id="items-${order.maDonHang}">
                        <div style="text-align: center; padding: 20px; color: #6b7280;">
                            Đang tải chi tiết...
                        </div>
                    </div>
                    
                    <div class="order-info">
                        <div><strong>Người nhận:</strong> ${order.tenNguoiNhan || 'N/A'}</div>
                        <div><strong>Số điện thoại:</strong> ${order.sdtNguoiNhan || 'N/A'}</div>
                        <div><strong>Địa chỉ:</strong> ${order.diaChiNguoiNhan || order.diaChiGiao || 'N/A'}</div>
                        <div><strong>Thanh toán:</strong> ${this.formatPaymentMethod(order.phuongThucThanhToan)}</div>
                    </div>
                </div>
                
                <div class="order-footer">
                    <div>
                        <div class="order-total">
                            Tổng tiền: <strong>${formattedTotal}</strong>
                        </div>
                    </div>
                    <div class="order-actions">
                        <button class="btn btn-detail" onclick="orderManager.viewDetail(${order.maDonHang})">
                            Chi tiết
                        </button>
                        ${order.trangThai === 'CHO_XAC_NHAN' ? 
                            `<button class="btn btn-cancel" onclick="orderManager.cancelOrder(${order.maDonHang})">
                                Hủy đơn
                            </button>` : ''}
                    </div>
                </div>
            `;
            
            // Load chi tiết đơn hàng
            this.loadOrderItems(order.maDonHang);
            
            return card;
        }

        // Load chi tiết đơn hàng (items)
        async loadOrderItems(maDonHang) {
            try {
                // ⚠️ SỬA LỖI 1: Sửa đường dẫn API cho đúng với Controller Java
                const response = await fetch(`/api/order/${maDonHang}/detail`, {
                    method: 'GET',
                    credentials: 'include'
                });

                if (!response.ok) {
                    throw new Error('Không thể tải chi tiết đơn hàng');
                }

                const items = await response.json();
                this.renderOrderItems(maDonHang, items);
                
            } catch (error) {
                console.error('Lỗi khi tải chi tiết đơn hàng:', error);
                const itemsContainer = document.getElementById(`items-${maDonHang}`);
                if (itemsContainer) {
                    itemsContainer.innerHTML = `
                        <div style="text-align: center; padding: 20px; color: #dc2626;">
                            Không thể tải chi tiết đơn hàng
                        </div>
                    `;
                }
            }
        }

        // Render chi tiết sản phẩm trong đơn hàng
        renderOrderItems(maDonHang, items) {
            const itemsContainer = document.getElementById(`items-${maDonHang}`);
            if (!itemsContainer) return;
            
            if (!items || items.length === 0) {
                itemsContainer.innerHTML = `
                    <div style="text-align: center; padding: 20px; color: #6b7280;">
                        Không có sản phẩm nào
                    </div>
                `;
                return;
            }
            
            itemsContainer.innerHTML = items.map(item => `
                <div class="order-item">
                    <img src="${item.hinhAnh || '/client/images/placeholder.png'}" 
                        alt="${item.tenSanPham}" 
                        class="item-image"
                        onerror="this.src='/client/images/placeholder.png'">
                    <div class="item-info">
                        <div class="item-name">${item.tenSanPham || 'Sản phẩm'}</div>
                        ${item.tenBienThe ? `<div class="item-variant">Phân loại: ${item.tenBienThe}</div>` : ''}
                        <div class="item-qty">Số lượng: ${item.soLuong}</div>
                    </div>
                    <div class="item-price">${this.formatCurrency(item.gia)}</div>
                </div>
            `).join('');
        }

        // Xem chi tiết đơn hàng (Hàm này đã đúng, gọi View)
        viewDetail(maDonHang) {
            window.location.href = `/order/detail/${maDonHang}`;
        }

        // Hủy đơn hàng
        async cancelOrder(maDonHang) {
            if (!confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
                return;
            }
            
            try {
                // ⚠️ SỬA LỖI 2: Sửa đường dẫn API cho đúng với Controller Java
                const response = await fetch(`/api/order/${maDonHang}/delete-order`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });

                if (!response.ok) {
                    throw new Error('Không thể hủy đơn hàng');
                }

                this.showToast('Đã hủy đơn hàng thành công', 'success');
                
                // Reload lại danh sách đơn hàng
                setTimeout(() => {
                    this.loadOrders();
                }, 1000);
                
            } catch (error) {
                console.error('Lỗi khi hủy đơn hàng:', error);
                this.showToast('Không thể hủy đơn hàng. Vui lòng thử lại.', 'error');
            }
        }

        // (Các hàm helper bên dưới giữ nguyên)
        // ...

        // Lấy thông tin trạng thái
        getStatusInfo(status) {
            const statusMap = {
                'CHO_XAC_NHAN': { text: 'Chờ xác nhận', class: 'status-pending' },
                'DA_XAC_NHAN': { text: 'Đã xác nhận', class: 'status-confirmed' },
                'DANG_GIAO': { text: 'Đang giao hàng', class: 'status-shipping' },
                'DA_GIAO': { text: 'Đã giao hàng', class: 'status-completed' },
                'DA_HUY': { text: 'Đã hủy', class: 'status-cancelled' }
            };
            
            return statusMap[status] || { text: status, class: 'status-pending' };
        }

        // Format phương thức thanh toán
        formatPaymentMethod(method) {
            const methodMap = {
                'TIEN_MAT': 'Tiền mặt',
                'CHUYEN_KHOAN': 'Chuyển khoản',
                'THE': 'Thẻ ngân hàng',
                'VI_DIEN_TU': 'Ví điện tử'
            };
            
            return methodMap[method] || method || 'N/A';
        }

        // Format ngày tháng
        formatDate(dateString) {
            if (!dateString) return 'N/A';
            
            const date = new Date(dateString);
            const day = String(date.getDate()).padStart(2, '0');
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const year = date.getFullYear();
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            
            return `${day}/${month}/${year} ${hours}:${minutes}`;
        }

        // Format tiền tệ
        formatCurrency(amount) {
            if (amount === null || amount === undefined) return '0 ₫';
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(amount);
        }

        // Hiển thị loading
        showLoading() {
            this.loadingEl.style.display = 'block';
            this.emptyStateEl.style.display = 'none';
            this.ordersListEl.style.display = 'none';
        }

        // Ẩn loading
        hideLoading() {
            this.loadingEl.style.display = 'none';
        }

        // Hiển thị empty state
        showEmptyState() {
            this.emptyStateEl.style.display = 'block';
            this.ordersListEl.style.display = 'none';
        }

        // Hiển thị toast notification
        showToast(message, type = 'info') {
            const toast = document.createElement('div');
            toast.className = `toast toast-${type}`;
            toast.style.cssText = `
                background: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#3b82f6'};
                color: white;
                padding: 16px 24px;
                border-radius: 8px;
                margin-bottom: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                animation: slideIn 0.3s ease;
            `;
            toast.textContent = message;
            
            this.toastRoot.appendChild(toast);
            
            setTimeout(() => {
                toast.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => toast.remove(), 300);
            }, 3000);
        }
    }

    // Khởi tạo khi DOM đã load
    let orderManager;
    document.addEventListener('DOMContentLoaded', () => {
        orderManager = new OrderManager();
    });

    // CSS animations
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from {
                transform: translateX(400px);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        
        @keyframes slideOut {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(400px);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);