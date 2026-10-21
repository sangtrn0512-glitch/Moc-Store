# Mộc Store
Website bán đồ trang trí nội thất, được xây dựng trong đồ án môn Java 5.

## Giới thiệu
Dự án thực hành xây dựng ứng dụng web theo mô hình MVC,
gồm giao diện mua hàng và trang quản trị.

## Công nghệ
- Java 17, Spring Boot, Spring MVC
- Thymeleaf, HTML, CSS, JavaScript
- Spring Data JPA / Hibernate
- SQL Server, Maven

## Chức năng

### Khách hàng
- Đăng nhập, đăng xuất.
- Xem sản phẩm và lọc theo danh mục, khoảng giá.
- Thêm sản phẩm vào giỏ hàng, cập nhật số lượng.
- Đặt hàng và xem lịch sử đơn hàng.

### Quản trị
- Quản lý sản phẩm, danh mục và tài khoản.
- Quản lý đơn hàng và cập nhật trạng thái.
- Thống kê doanh thu theo danh mục.
- Xem 10 khách hàng có tổng chi tiêu cao nhất.

## Một số điểm triển khai
- Tổ chức backend theo Controller → Service → Repository.
- Dùng Thymeleaf để hiển thị dữ liệu từ Spring MVC.
- Lưu dữ liệu bằng Spring Data JPA và SQL Server.
- Lưu giỏ hàng theo session.
- Mã hóa mật khẩu bằng BCrypt.
- Kiểm tra quyền truy cập trang quản trị bằng interceptor.
- Sử dụng transaction khi tạo đơn hàng và chi tiết đơn hàng.

## Hình ảnh
<!-- Thêm ảnh trang chủ, chi tiết sản phẩm, giỏ hàng và trang quản trị. -->

## Hướng dẫn chạy
<!-- Bổ sung các bước cấu hình SQL Server và chạy ứng dụng đã kiểm tra thực tế. -->

## Tác giả
Trần Minh Sang
