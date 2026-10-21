# Kiểm thử các chức năng

1. Đăng ký tên/email mới, kiểm tra mật khẩu băm, chưa kích hoạt.
2. Email kích hoạt: hợp lệ, token sai, token hết hạn, dùng lại link.
3. Login sai/đúng; tài khoản chưa kích hoạt; logout; khách truy cập admin bị 403.
4. Khách chưa login vào checkout được chuyển login rồi quay về checkout.
5. Giỏ: session riêng, thêm lặp, cập nhật 0/100 bị chặn, xóa/clear, tính tiền giảm.
6. Checkout: giỏ trống, địa chỉ trống, sản phẩm ngừng bán, giá DB đổi, gửi lặp, rollback khi một dòng không hợp lệ.
7. Khách B không xem được đơn khách A.
8. Admin CRUD loại/sản phẩm/tài khoản; xóa dữ liệu có FK bị chặn. Admin không tự xóa/khóa/bỏ quyền chính mình.
9. Admin tạo đơn, thêm/sửa/xóa dòng khi PENDING, cập nhật COMPLETED; thống kê và sản phẩm đã mua cập nhật.
10. Khôi phục mật khẩu bằng SMTP giả lập; token có hạn và chỉ dùng một lần, phiên cũ bị vô hiệu sau khi đổi mật khẩu.
11. Upload PNG/JPG hợp lệ; file khác hoặc >2 MB bị từ chối. Kiểm tra ảnh và form trên desktop/mobile.
12. Bộ lọc kết hợp tên/loại/giá, sắp xếp và trang tiếp theo giữ điều kiện.

## Smoke test cục bộ

Chạy app với `--server.port=18085 --spring.mail.host=127.0.0.1 --spring.mail.port=10255 --spring.mail.username=test@moc.invalid --spring.mail.password= --spring.mail.properties.mail.smtp.auth=false --spring.mail.properties.mail.smtp.starttls.enable=false --spring.mail.properties.mail.smtp.starttls.required=false --app.base-url=http://localhost:18085`.

Sau đó `python -X utf8 scripts/smoke_test.py`. Script nhận email tại localhost, không gửi thư Gmail. Những thay đổi test được dọn theo tiền tố tài khoản dành riêng cho test. Chỉ chạy ở DB phát triển.

## Kết quả ngày 21/10/2026

- Maven: 4 test, không có failure/error; package JAR thành công.
- HTTP smoke: CRUD loại/sản phẩm/tài khoản/đơn, đăng ký, kích hoạt, login/logout, CSRF, phân quyền, quyền sở hữu đơn, checkout, rollback toàn bộ đơn khi một sản phẩm ngừng bán, lịch sử và thống kê đã qua.
- Khôi phục mật khẩu: token dùng một lần, phiên cũ không còn quyền sau đổi mật khẩu đã qua.
- Token kích hoạt hết hạn và gửi lại với token mới đã qua.
- Upload ảnh PNG hợp lệ được lưu/phục vụ; nội dung không phải ảnh bị từ chối.
- Desktop 1440x1000 và mobile 390x844: catalog/admin orders không tràn ngang.
- SMTP sử dụng hộp thư giả lập localhost, không xác nhận Gmail thật trong lần kiểm thử này.
- Dữ liệu thử được xóa sau test. Một số ảnh dữ liệu mẫu còn để trống.
