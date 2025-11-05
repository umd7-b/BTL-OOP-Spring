// Toggle dropdown menu
function toggleDropdown(event) {
    event.stopPropagation();
    const dropdown = document.getElementById('dropdownMenu');
    dropdown.classList.toggle('show');
}

// Close dropdown when clicking outside
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById('dropdownMenu');
    const userInfo = document.querySelector('.user-info');
    
    if (!userInfo.contains(event.target)) {
        dropdown.classList.remove('show');
    }
});

// Handle logout
function handleLogout(event) {
    event.stopPropagation();
    const spinner = document.getElementById('logoutSpinner');
    const logoutBtn = event.currentTarget;
    
    spinner.classList.add('show');
    logoutBtn.style.pointerEvents = 'none';
    
    fetch('/admin/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/admin/login';
        }
    })
    .catch(error => {
        console.error('Logout error:', error);
        window.location.href = '/admin/login';
    });
}

// Modal management
let currentModalType = '';

function openAddModal(type) {
    currentModalType = type;
    const modal = document.getElementById('addModal');
    const modalTitle = document.getElementById('modalTitle');
    const formFields = document.getElementById('formFields');
    const form = document.getElementById('addForm');
    
    // Set modal title and form action based on type
    const config = {
        'product': {
            title: 'Thêm Sản phẩm mới',
            action: '/admin/products/add'
        },
        'brand': {
            title: 'Thêm Thương hiệu mới',
            action: '/admin/brands/add'
        },
        'category': {
            title: 'Thêm Danh mục mới',
            action: '/admin/categories/add'
        },
        'sport': {
            title: 'Thêm Môn thể thao mới',
            action: '/admin/sports/add'
        }
    };
    
    modalTitle.textContent = config[type].title;
    form.action = config[type].action;
    
    // Generate form fields based on type
    formFields.innerHTML = generateFormFields(type);
    
    modal.style.display = 'block';
}

function closeAddModal() {
    const modal = document.getElementById('addModal');
    modal.style.display = 'none';
    document.getElementById('addForm').reset();
}

function generateFormFields(type) {
    if (type === 'product') {
        return `
            <div class="form-group">
                <label for="tenSp">Tên sản phẩm *</label>
                <input type="text" id="tenSp" name="tenSp" required>
            </div>
            <div class="form-group">
                <label for="maThuongHieu">Thương hiệu *</label>
                <select id="maThuongHieu" name="maThuongHieu" required>
                    <option value="">Chọn thương hiệu</option>
                </select>
            </div>
            <div class="form-group">
                <label for="maDanhMuc">Danh mục *</label>
                <select id="maDanhMuc" name="maDanhMuc" required>
                    <option value="">Chọn danh mục</option>
                </select>
            </div>
            <div class="form-group">
                <label for="maMonTheThao">Môn thể thao *</label>
                <select id="maMonTheThao" name="maMonTheThao" required>
                    <option value="">Chọn môn thể thao</option>
                </select>
            </div>
            <div class="form-group">
                <label for="giaGoc">Giá gốc (đ) *</label>
                <input type="number" id="giaGoc" name="giaGoc" min="0" step="1000" required>
            </div>
            <div class="form-group">
                <label for="giaKm">Giá khuyến mãi (đ)</label>
                <input type="number" id="giaKm" name="giaKm" min="0" step="1000">
            </div>
            <div class="form-group">
                <label for="moTa">Mô tả</label>
                <textarea id="moTa" name="moTa" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="anhSanPham">Hình ảnh sản phẩm</label>
                <input type="file" id="anhSanPham" name="anhSanPham" multiple accept="image/*">
            </div>
            <div class="form-group">
            <label>Biến thể sản phẩm</label>
            <button type="button" class="btn btn-primary" onclick="addVariantRow()">+ Thêm biến thể</button>

            <table id="variantsTable" style="width:100%; margin-top:10px;">
                <thead>
                    <tr>
                        <th>Màu sắc</th>
                        <th>Kích thước</th>
                        <th>Số lượng</th>
                        <th>Xoá</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
        `;
    } else if (type === 'brand') {
        return `
            <div class="form-group">
                <label for="tenThuongHieu">Tên thương hiệu *</label>
                <input type="text" id="tenThuongHieu" name="tenThuongHieu" required>
            </div>
            <div class="form-group">
                <label for="moTa">Mô tả</label>
                <textarea id="moTa" name="moTa" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="logoUrl">Logo URL</label>
                <input type="url" id="logoUrl" name="logoUrl" placeholder="https://example.com/logo.png">
            </div>
            <div class="form-group">
                <label for="quocGia">Quốc gia</label>
                <input type="text" id="quocGia" name="quocGia">
            </div>
        `;
    } else if (type === 'category') {
        return `
            <div class="form-group">
                <label for="tenDanhMuc">Tên danh mục *</label>
                <input type="text" id="tenDanhMuc" name="tenDanhMuc" required>
            </div>
            <div class="form-group">
                <label for="moTa">Mô tả</label>
                <textarea id="moTa" name="moTa" rows="3"></textarea>
            </div>
        `;
    } else if (type === 'sport') {
        return `
            <div class="form-group">
                <label for="tenMonTheThao">Tên môn thể thao *</label>
                <input type="text" id="tenMonTheThao" name="tenMonTheThao" required>
            </div>
            <div class="form-group">
                <label for="moTa">Mô tả</label>
                <textarea id="moTa" name="moTa" rows="3"></textarea>
            </div>
        `;
    }
    
    return '';
}

// Load dropdown data when modal opens
document.getElementById('addModal').addEventListener('show', function() {
    if (currentModalType === 'product') {
        loadDropdownData();
    }
});

function loadDropdownData() {
    // Load brands
    fetch('/admin/brands/all')
        .then(response => response.json())
        .then(brands => {
            const select = document.getElementById('maThuongHieu');
            brands.forEach(brand => {
                const option = document.createElement('option');
                option.value = brand.maThuongHieu;
                option.textContent = brand.tenThuongHieu;
                select.appendChild(option);
            });
        });
    
    // Load categories
    fetch('/admin/api/categories')
        .then(response => response.json())
        .then(categories => {
            const select = document.getElementById('maDanhMuc');
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.maDanhMuc;
                option.textContent = category.tenDanhMuc;
                select.appendChild(option);
            });
        });
    
    // Load sports
    fetch('/admin/api/sports')
        .then(response => response.json())
        .then(sports => {
            const select = document.getElementById('maMonTheThao');
            sports.forEach(sport => {
                const option = document.createElement('option');
                option.value = sport.maMonTheThao;
                option.textContent = sport.tenMonTheThao;
                select.appendChild(option);
            });
        });
}

// Handle form submission
// XÓA HÀM CŨ VÀ THAY BẰNG HÀM NÀY
document.getElementById('addForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn form tự submit
    
    const form = event.target;
    const action = form.action; 
    
    // 1. Tạo FormData từ form (để lấy các trường cơ bản: tenSp, giaGoc,...)
    // Hàm này cũng tự động lấy file ảnh (anhSanPham)
    const formData = new FormData(form);
    
    // 2. Xử lý dữ liệu Biến thể
    const variants = [];
    // Tìm tất cả các hàng 'tr' trong tbody của bảng variantsTable
    const variantRows = document.querySelectorAll('#variantsTable tbody tr'); 
    
    variantRows.forEach(row => {
        // Tìm các input bằng class chúng ta đã thêm ở Bước 1
        const mauSac = row.querySelector('.input-mau-sac').value;
        const kichThuoc = row.querySelector('.input-kich-thuoc').value;
        const soLuongTon = row.querySelector('.input-ton-kho').value;

        // Chỉ thêm nếu có dữ liệu
        if (mauSac || kichThuoc || soLuongTon) {
            variants.push({
                mauSac: mauSac,
                kichThuoc: kichThuoc,
                soLuongTon: parseInt(soLuongTon) || 0
            });
        }
    });

    // 3. Thêm mảng variants vào FormData dưới dạng chuỗi JSON
    // Tên 'variants' phải khớp với @RequestParam("variants") trong Controller
    if (variants.length > 0) {
        formData.append('variants', JSON.stringify(variants));
    }
    
    // (DEBUG) Bỏ ẩn dòng này để xem bạn đang gửi gì
    // for (let [key, value] of formData.entries()) {
    //   console.log(key, value);
    // }

    // 4. Gửi request
    fetch(action, {
        method: 'POST',
        body: formData // Bây giờ formData đã chứa: data form + JSON biến thể + files
    })
    .then(response => {
        // Vì Controller (Java) trả về JSON, chúng ta cần xử lý nó
        return response.json().then(data => {
            if (response.ok) {
                alert(data.message || 'Thêm mới thành công!'); // Đọc thông báo từ server
                window.location.reload();
            } else {
                // Hiển thị lỗi từ server
                alert('Lỗi: ' + (data.message || data.body || 'Không thể thêm mới'));
            }
        });
    })
    .catch(error => {
        console.error('Lỗi khi gửi form:', error);
        alert('Có lỗi nghiêm trọng xảy ra. Vui lòng thử lại.');
    });
});

// Filter products
function filterProducts() {
    const searchValue = document.getElementById('searchInput').value.toLowerCase();
    const brandFilter = document.getElementById('brandFilter').value;
    const categoryFilter = document.getElementById('categoryFilter').value;
    const sportFilter = document.getElementById('sportFilter').value;
    
    const rows = document.querySelectorAll('#productsTableBody tr');
    
    rows.forEach(row => {
        if (row.cells.length < 10) return; // Skip empty row
        
        const productName = row.cells[2].textContent.toLowerCase();
        const brand = row.cells[3].textContent;
        const category = row.cells[4].textContent;
        const sport = row.cells[5].textContent;
        
        const matchesSearch = productName.includes(searchValue);
        const matchesBrand = !brandFilter || brand === brandFilter;
        const matchesCategory = !categoryFilter || category === categoryFilter;
        const matchesSport = !sportFilter || sport === sportFilter;
        
        if (matchesSearch && matchesBrand && matchesCategory && matchesSport) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Edit product
function editProduct(maSp) {
    window.location.href = '/admin/products/edit/' + maSp;
}

// Delete product
function deleteProduct(maSp) {
    if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        fetch('/admin/products/delete/' + maSp, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Xóa sản phẩm thành công!');
                window.location.reload();
            } else {
                return response.json().then(error => {
                    alert('Lỗi: ' + (error.message || 'Không thể xóa sản phẩm'));
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra. Vui lòng thử lại.');
        });
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('addModal');
    if (event.target == modal) {
        closeAddModal();
    }
}

// Initialize: Load dropdown data when page loads
document.addEventListener('DOMContentLoaded', function() {
    // Add event listener for when modal is about to be shown
    const originalOpenModal = openAddModal;
    window.openAddModal = function(type) {
        originalOpenModal(type);
        if (type === 'product') {
            loadDropdownData();
        }
    };
});function addVariantRow() {
    const table = document.querySelector("#variantsTable tbody");
    const row = document.createElement("tr");

    // Thêm các class để dễ dàng query
    row.innerHTML = `
        <td>
            <input type="text" class="form-control input-mau-sac" required>
        </td>
        <td>
            <input type="text" class="form-control input-kich-thuoc" required>
        </td>
        <td>
            <input type="number" min="0" value="0" class="form-control input-ton-kho" required>
        </td>
        <td>
            <button type="button" class="btn btn-danger" onclick="this.parentElement.parentElement.remove()">X</button>
        </td>
    `;
    // Bỏ thuộc tính 'name' đi, chúng ta không cần chúng nữa
    // vì chúng ta sẽ đọc bằng 'class'

    table.appendChild(row);
}