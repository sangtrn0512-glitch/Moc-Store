# Khung Assignment Java 5

Ứng dụng bán hàng theo đề: Spring Boot MVC, Thymeleaf, JpaRepository và SQL Server.
Chủ đề: Mộc Store bán đồ trang trí nhà cửa. Sản phẩm, giá và hình SVG hiện dùng để minh họa giao diện, chưa lấy từ
database.

## Chạy bản khung

Chạy `AsmJava5Application` trong IntelliJ hoặc `./mvnw.cmd spring-boot:run`.
Mở http://localhost:8080/home/index. Cấu hình trong `application.properties` tạm tắt kết nối database để xem giao diện.
Các trang admin đang mở để xem bố cục, chưa có bảo mật. Không dùng bản khung này để triển khai công khai.

## Cấu trúc và luồng xử lý dự kiến

`Trình duyệt → Controller → Service → Repository → SQL Server`.
Controller trả tên template để Thymeleaf dựng HTML.

- `entity`: Category, Product, Account, Order, OrderDetail; quan hệ nhiều-một.
- `repository`: 5 interface kế thừa JpaRepository.
- `service`: hợp đồng CRUD và các dịch vụ tiện ích; chưa có implementation.
- `controller`: điều hướng GET cho các trang khách hàng.
- `controller/admin`: điều hướng GET cho quản trị và báo cáo.
- `templates/shared/layout.html`: layout chung; `templates/admin`: các trang quản trị.
- `static/css/style.css`: CSS dùng chung.
- `Database/database.sql`: script khung SQL Server, chưa chạy và chưa có dữ liệu mẫu.

## Kết nối database sau

1. Chạy script SQL bằng SSMS, một lần trên database mới.
2. Đặt biến môi trường `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
3. Trong `application.properties`, xóa dòng `spring.autoconfigure.exclude`, rồi bỏ dấu `#` ở 5 dòng cấu hình database.
4. Hoàn thiện và kiểm tra mapping Entity với database. Cấu hình database dùng `ddl-auto=validate`.

## Thứ tự làm tiếp

1. Chọn chủ đề, tạo dữ liệu mẫu: 10 loại, 200 sản phẩm, 5 người dùng, 1 admin.
2. Viết implementation CategoryService và ProductService; hiển thị, lọc loại/giá, chi tiết và sản phẩm cùng loại.
3. Giỏ hàng: thêm, xóa, cập nhật số lượng, xóa tất cả; dùng POST cho thao tác thay đổi dữ liệu.
4. Đăng nhập/đăng xuất; AuthInterceptor và AuthConfig; chuyển về URL trước đăng nhập; phân quyền admin.
5. Đăng ký, kích hoạt email, cập nhật hồ sơ, đổi và khôi phục mật khẩu; lưu mật khẩu dạng băm.
6. Đặt hàng, lịch sử, chi tiết, sản phẩm đã mua; lưu đơn và chi tiết trong cùng transaction.
7. CRUD quản trị, tìm kiếm, sắp xếp và phân trang phù hợp.
8. Doanh thu theo loại (doanh thu, số lượng, giá cao/thấp/trung bình), top 10 VIP (tổng tiền, ngày mua đầu/cuối).
9. Kiểm tra bảo mật, validation và hoàn thiện hồ sơ nộp.

Hiện chỉ có đường dẫn GET để xem trang. Các đường dẫn thêm/xóa/sửa trong đề chưa được triển khai; form và nút nghiệp vụ
đang vô hiệu hóa.
Entity và repository có sẵn; chưa có xử lý giỏ hàng, xác thực, gửi email, CRUD hoặc thống kê.

## Hồ sơ nộp theo đề

- `BaoCao/BaoCao.doc`: báo cáo hoàn chỉnh (chưa tạo).
- `Source`: toàn bộ ứng dụng; khi nộp sao chép dự án vào thư mục này, không lồng bản sao ngay trong dự án đang phát
  triển.
- `Database/database.sql`: script tạo database và dữ liệu mẫu hoàn chỉnh.
