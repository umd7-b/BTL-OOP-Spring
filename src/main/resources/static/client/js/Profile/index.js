
        // Load thông tin người dùng
        async function loadProfile() {
            try {
                const response = await fetch('/api/profile');
                const data = await response.json();
                
                if (data.success && data.user) {
                    const user = data.user;
                    
                    // Hiển thị thông tin trong sidebar
                    document.getElementById('userName').textContent = user.hoTen;
                    document.getElementById('userEmail').textContent = user.email;
                    document.getElementById('userAvatar').textContent = user.hoTen.charAt(0).toUpperCase();
                    
                    // Điền vào form
                    document.getElementById('username').value = user.username;
                    document.getElementById('hoTen').value = user.hoTen;
                    document.getElementById('email').value = user.email;
                    document.getElementById('soDienThoai').value = user.soDienThoai || '';
                    document.getElementById('diaChi').value = user.diaChi || '';
                    document.getElementById('gioiTinh').value = user.gioiTinh || '';
                    
                    if (user.ngayDangKy) {
                        const date = new Date(user.ngayDangKy);
                        document.getElementById('ngayDangKy').value = date.toLocaleDateString('vi-VN');
                    }
                } else {
                    window.location.href = '/login';
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Không thể tải thông tin người dùng!');
            }
        }
        
        // Cập nhật thông tin
        document.getElementById('profileForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const updateData = {
                hoTen: document.getElementById('hoTen').value,
                email: document.getElementById('email').value,
                soDienThoai: document.getElementById('soDienThoai').value,
                diaChi: document.getElementById('diaChi').value,
                gioiTinh: document.getElementById('gioiTinh').value
            };
            
            try {
                const response = await fetch('/api/profile/update', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(updateData)
                });
                
                const data = await response.json();
                
                if (response.ok) {
                    document.getElementById('updateSuccess').classList.add('show');
                    document.getElementById('updateError').classList.remove('show');
                    
                    // Cập nhật sessionStorage
                    sessionStorage.setItem('ho_ten', updateData.hoTen);
                    
                    // Reload profile
                    loadProfile();
                    
                    setTimeout(() => {
                        document.getElementById('updateSuccess').classList.remove('show');
                    }, 3000);
                } else {
                    document.getElementById('updateError').textContent = data.message;
                    document.getElementById('updateError').classList.add('show');
                    document.getElementById('updateSuccess').classList.remove('show');
                }
            } catch (error) {
                console.error('Error:', error);
                document.getElementById('updateError').textContent = 'Có lỗi xảy ra!';
                document.getElementById('updateError').classList.add('show');
            }
        });
        
        // Đổi mật khẩu
        document.getElementById('passwordForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                document.getElementById('passwordError').textContent = 'Mật khẩu xác nhận không khớp!';
                document.getElementById('passwordError').classList.add('show');
                return;
            }
            
            try {
                const response = await fetch('/api/profile/change-password', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({oldPassword, newPassword})
                });
                
                const data = await response.json();
                
                if (response.ok) {
                    document.getElementById('passwordSuccess').classList.add('show');
                    document.getElementById('passwordError').classList.remove('show');
                    document.getElementById('passwordForm').reset();
                    
                    setTimeout(() => {
                        document.getElementById('passwordSuccess').classList.remove('show');
                    }, 3000);
                } else {
                    document.getElementById('passwordError').textContent = data.message;
                    document.getElementById('passwordError').classList.add('show');
                    document.getElementById('passwordSuccess').classList.remove('show');
                }
            } catch (error) {
                console.error('Error:', error);
                document.getElementById('passwordError').textContent = 'Có lỗi xảy ra!';
                document.getElementById('passwordError').classList.add('show');
            }
        });
        
        // Xử lý tab menu
        document.querySelectorAll('.menu-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                
                // Remove active class
                document.querySelectorAll('.menu-link').forEach(l => l.classList.remove('active'));
                document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
                
                // Add active class
                link.classList.add('active');
                const tabId = link.getAttribute('data-tab');
                document.getElementById(tabId).classList.add('active');
            });
        });
        
     
        loadProfile();
