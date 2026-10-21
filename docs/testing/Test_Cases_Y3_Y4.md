# KẾT QUẢ TEST CASE Y3 / Y4 - MỘC STORE

Tạo báo cáo: 2026-10-22T05:08:40+07:00. Nguồn: XML JUnit do Maven Surefire tạo, không điền Pass thủ công.

**177 lượt test tự động: 177 Pass, 0 Fail, 0 Skipped.**
Đối chiếu với 122 Test Case Y2: 119 case có script và có kết quả; 3 case chưa chạy tự động.

## Cách đọc và giới hạn

- Pass trong tài liệu này là **Pass của phạm vi assertions trong script**, không xác nhận mọi bước/biến thể thủ công của Test Case Y2 đã được thực hiện. Tên phương thức và XML cho biết chính xác phần đã chạy.
- Một Test Case Y2 có thể được nhiều script kiểm tra; test tham số có thể chạy nhiều lượt. Số lượt JUnit không bằng số Test Case Y2.
- DB test là H2 in-memory, MODE=MSSQLServer; MailService là mock. Không gửi email thật, không xác nhận toàn bộ hành vi SQL Server/schema/collation/gửi SMTP thật.
- Không đánh dấu 3 case cài mới/dữ liệu mẫu/đánh giá giao diện là Pass khi chưa thực hiện. Case có kết quả Fail được giữ nguyên để phản ánh lỗi thật, không đổi expectation chỉ để xanh.
- Đề gốc: Assignment (1).pdf Y3/Y4; business rule: Assignment Lập trình Java 5 và source hiện tại. Mã TC giữ theo catalogue Y2 đã lưu trong Git. Catalogue cũ là bản tham chiếu; ghi chú cập nhật bên dưới thay thế nhận định hiện trạng đã lỗi thời.

## Tổng hợp theo Test Case Y2

| ID | Nội dung | Kết quả phạm vi tự động | Lượt script |
|---|---|---|---:|
| TC-001 | Trang chủ và hàng mới | Pass | 2 |
| TC-002 | Hàng giảm giá | Pass | 1 |
| TC-003 | Hàng bán chạy | Pass | 1 |
| TC-004 | Tìm theo loại | Pass | 1 |
| TC-005 | Lọc giá gồm hai biên | Pass | 1 |
| TC-006 | Giá hai biên bằng nhau | Pass | 1 |
| TC-007 | Khoảng giá sai | Pass | 1 |
| TC-008 | Lọc không có kết quả | Pass | 1 |
| TC-009 | Chi tiết và hàng cùng loại | Pass | 1 |
| TC-010 | Sản phẩm không tồn tại | Pass | 1 |
| TC-011 | Sai kiểu hoặc thiếu tham số giá | Pass | 1 |
| TC-012 | Khách thêm hàng | Pass | 2 |
| TC-013 | Thêm lặp cùng sản phẩm | Pass | 1 |
| TC-014 | Hiển thị và tính tiền | Pass | 1 |
| TC-015 | Cập nhật số lượng tại biên | Pass | 2 |
| TC-016 | Số lượng ngoài miền | Pass | 3 |
| TC-017 | Thêm quá giới hạn | Pass | 1 |
| TC-018 | Thêm hàng không tồn tại hoặc ngừng bán | Pass | 1 |
| TC-019 | Cập nhật hàng không trong giỏ | Pass | 1 |
| TC-020 | Xóa một dòng | Pass | 1 |
| TC-021 | Xóa toàn bộ và xóa giỏ rỗng | Pass | 1 |
| TC-022 | Cách ly phiên | Pass | 1 |
| TC-023 | Làm tròn tiền | Pass | 1 |
| TC-024 | Đăng ký hợp lệ | Pass | 2 |
| TC-025 | Đăng ký thiếu dữ liệu | Pass | 1 |
| TC-026 | Trùng username hoặc email | Pass | 3 |
| TC-027 | Email sai định dạng | Pass | 3 |
| TC-028 | Biên độ dài hồ sơ | Pass | 4 |
| TC-029 | Biên mật khẩu | Pass | 3 |
| TC-030 | Kích hoạt hợp lệ | Pass | 2 |
| TC-031 | Kích hoạt sai/hết hạn/tái sử dụng | Pass | 2 |
| TC-032 | Gửi lại kích hoạt | Pass | 1 |
| TC-033 | SMTP lỗi khi đăng ký | Pass | 1 |
| TC-034 | Cập nhật hồ sơ | Pass | 1 |
| TC-035 | Đổi email cần kích hoạt | Pass | 1 |
| TC-036 | Hồ sơ không hợp lệ | Pass | 1 |
| TC-037 | Đổi mật khẩu thành công | Pass | 1 |
| TC-038 | Đổi mật khẩu thất bại | Pass | 2 |
| TC-039 | Quên mật khẩu | Pass | 1 |
| TC-040 | Email lạ/chưa kích hoạt | Pass | 1 |
| TC-041 | Đặt lại mật khẩu thành công | Pass | 1 |
| TC-042 | Đặt lại token sai/hết hạn/dùng lại | Pass | 2 |
| TC-043 | Reset xác nhận sai hoặc mật khẩu ngắn | Pass | 1 |
| TC-044 | Ảnh hồ sơ hợp lệ | Pass | 2 |
| TC-045 | Ảnh lỗi và quá giới hạn | Pass | 2 |
| TC-046 | Đăng nhập user/admin | Pass | 2 |
| TC-047 | Sai thông tin hoặc bị khóa | Pass | 2 |
| TC-048 | Khách vào chức năng bảo vệ | Pass | 7 |
| TC-049 | Quay lại URL yêu cầu | Pass | 2 |
| TC-050 | Khách truy cập quản trị | Pass | 7 |
| TC-051 | User thường không được quản trị | Pass | 7 |
| TC-052 | CSRF sai hoặc thiếu | Pass | 1 |
| TC-053 | Đăng xuất | Pass | 1 |
| TC-054 | Đổi mật khẩu làm hết phiên khác | Pass | 1 |
| TC-055 | Chống xem đơn người khác | Pass | 1 |
| TC-056 | Không thay đổi dữ liệu bằng GET | Pass | 1 |
| TC-057 | Checkout thành công | Pass | 1 |
| TC-058 | Checkout giỏ rỗng | Pass | 1 |
| TC-059 | Địa chỉ tại biên | Pass | 2 |
| TC-060 | Địa chỉ sai | Pass | 3 |
| TC-061 | Điện thoại hợp lệ theo code | Pass | 3 |
| TC-062 | Điện thoại sai | Pass | 3 |
| TC-063 | Giá thay đổi trước checkout | Pass | 1 |
| TC-064 | Rollback khi hàng ngừng bán | Pass | 1 |
| TC-065 | Chống gửi đơn hai lần | Pass | 1 |
| TC-066 | Checkout key giả | Pass | 1 |
| TC-067 | Danh sách và chi tiết đơn của mình | Pass | 1 |
| TC-068 | My Products | Pass | 1 |
| TC-069 | Giữ giá lịch sử | Pass | 1 |
| TC-070 | Tạo và đọc loại | Pass | 1 |
| TC-071 | Tên loại trắng hoặc trùng | Pass | 2 |
| TC-072 | Sửa loại hợp lệ | Pass | 1 |
| TC-073 | Biên tên khi sửa | Pass | 1 |
| TC-074 | Biên tên khi tạo - kiểm tra khoảng trống validation | Pass | 1 |
| TC-075 | Xóa loại không có hàng | Pass | 1 |
| TC-076 | Chặn xóa loại có hàng | Pass | 2 |
| TC-077 | Sửa loại không tồn tại | Pass | 1 |
| TC-078 | Tạo/đọc sản phẩm | Pass | 1 |
| TC-079 | Sửa sản phẩm | Pass | 1 |
| TC-080 | Tên bắt buộc và độ dài | Pass | 1 |
| TC-081 | Biên giá | Pass | 6 |
| TC-082 | Biên giảm giá | Pass | 5 |
| TC-083 | Thiếu loại hoặc loại giả | Pass | 1 |
| TC-084 | Tên file ảnh không an toàn | Pass | 3 |
| TC-085 | Upload ảnh sản phẩm | Pass | 2 |
| TC-086 | Ngừng bán | Pass | 1 |
| TC-087 | Xóa hàng chưa phát sinh đơn | Pass | 2 |
| TC-088 | Chặn xóa hàng đã có đơn | Pass | 2 |
| TC-089 | Sửa hàng không tồn tại | Pass | 1 |
| TC-090 | Tạo/đọc tài khoản | Pass | 1 |
| TC-091 | Sửa giữ mật khẩu | Pass | 1 |
| TC-092 | Admin đặt mật khẩu mới | Pass | 1 |
| TC-093 | Dữ liệu tài khoản sai | Pass | 1 |
| TC-094 | Không tự khóa/bỏ quyền | Pass | 1 |
| TC-095 | Không tự xóa | Pass | 1 |
| TC-096 | Xóa account chưa có đơn | Pass | 1 |
| TC-097 | Chặn xóa account có đơn | Pass | 1 |
| TC-098 | Khóa user đang có phiên | Pass | 1 |
| TC-099 | Tạo/đọc đơn thủ công | Pass | 1 |
| TC-100 | Tạo đơn dữ liệu sai | Pass | 2 |
| TC-101 | Sửa địa chỉ/điện thoại/trạng thái | Pass | 1 |
| TC-102 | Trạng thái không thuộc tập | Pass | 1 |
| TC-103 | Đơn kết thúc không đổi trạng thái | Pass | 2 |
| TC-104 | Thêm/sửa dòng hàng PENDING | Pass | 1 |
| TC-105 | Chặn sửa dòng không PENDING | Pass | 4 |
| TC-106 | Số lượng dòng sai/hàng ngừng bán | Pass | 1 |
| TC-107 | Xóa một dòng còn lại ít nhất một | Pass | 1 |
| TC-108 | Chặn xóa dòng cuối hoặc dòng đơn khác | Pass | 2 |
| TC-109 | Xóa đơn được phép | Pass | 1 |
| TC-110 | Chặn xóa đơn đang xử lý/hoàn tất | Pass | 3 |
| TC-111 | Doanh thu và đủ 5 chỉ số | Pass | 1 |
| TC-112 | Không cộng đơn chưa hoàn tất | Pass | 1 |
| TC-113 | Tách loại và sắp xếp doanh thu | Pass | 1 |
| TC-114 | Top 10 và thứ tự VIP | Pass | 1 |
| TC-115 | VIP tổng tiền và ngày đầu/cuối | Pass | 1 |
| TC-116 | VIP đồng tổng và ít hơn 10 người | Pass | 1 |
| TC-117 | Báo cáo không có dữ liệu | Pass | 1 |
| TC-118 | Dữ liệu mẫu theo đề | Not Run | 0 |
| TC-119 | Cài mới từ script bàn giao | Not Run | 0 |
| TC-120 | Luồng mua hàng xuyên suốt | Pass | 1 |
| TC-121 | Hiển thị và form trên thiết bị | Not Run | 0 |
| TC-122 | Smoke build source hiện tại | Pass | 1 |

## Chi tiết đối chiếu và kết quả thực tế

### TC-001 - Trang chủ và hàng mới

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** Có ít nhất 6 sản phẩm đang bán với ngày tạo khác nhau
- **Bước thực hiện Y2:** Mở / và /home/index; đối chiếu mục Hàng mới với CSDL
- **Kỳ vọng Y2:** Hai URL hiển thị trang chủ; tối đa 4 hàng đang bán mới nhất, ngày giảm dần rồi id giảm dần.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#homeSuppliesThreeProductSections` → **Pass** (0.014 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#newProductsUseDateThenId` → **Pass** (0.212 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-002 - Hàng giảm giá

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** 6 hàng đang bán giảm 0, 5, 10, 15, 20, 30%; 1 hàng ngừng bán giảm 90%
- **Bước thực hiện Y2:** Mở /home/index; xem mục giảm giá
- **Kỳ vọng Y2:** Tối đa 4 hàng đang bán có discount > 0 theo mức giảm giảm dần; không có hàng giảm 0% hoặc ngừng bán.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#discountTopFourExcludeUnavailableAndZero` → **Pass** (0.216 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-003 - Hàng bán chạy

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** 5 hàng có lượng bán COMPLETED lần lượt 1,2,3,4,5; hàng khác có 100 món PENDING
- **Bước thực hiện Y2:** Mở trang chủ; đối chiếu các dòng đơn
- **Kỳ vọng Y2:** Top 4 theo tổng quantity của COMPLETED giảm dần; không tính 100 món PENDING; chỉ hàng đang bán.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#bestSellersIgnorePendingAndUnavailable` → **Pass** (0.278 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-004 - Tìm theo loại

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** C1 có P1,P2; C2 có P3
- **Bước thực hiện Y2:** GET URL cũ rồi theo redirect đến /product/list?categoryId={C1}; kiểm tra kết quả
- **Kỳ vọng Y2:** Chỉ có sản phẩm đang bán thuộc C1; tên,ảnh,giá đúng DB.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Source hiện tại chuyển URL cũ sang /product/list?categoryId=...; test đối chiếu chuyển hướng và lọc tại URL mới.
- **Script:** `org.example.asm_java5.controller.ControllerTests#categoryFilterUsesRequestedId` → **Pass** (0.031 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-005 - Lọc giá gồm hai biên

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** P1 giá 100000; P2 giá 200000; P3 giá 300000
- **Bước thực hiện Y2:** GET /product/list-by-price?minPrice=100000&maxPrice=200000
- **Kỳ vọng Y2:** Có P1 và P2; không có P3. Code lọc giá gốc price, không phải giá sau giảm.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#priceFilterIncludesBothBoundaries` → **Pass** (0.213 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-006 - Giá hai biên bằng nhau

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** minPrice=200000; maxPrice=200000
- **Bước thực hiện Y2:** Gửi bộ lọc giá
- **Kỳ vọng Y2:** Chỉ trả sản phẩm giá gốc đúng 200000.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#priceFilterIncludesBothBoundaries` → **Pass** (0.213 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-007 - Khoảng giá sai

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** Lần 1: minPrice=-1,maxPrice=10; lần 2: minPrice=20,maxPrice=10
- **Bước thực hiện Y2:** Mở /product/list-by-price với từng mẫu; theo redirect đến /product/list
- **Kỳ vọng Y2:** Hiển thị Khoảng giá không hợp lệ; bỏ khoảng giá sai và trả danh sách theo nhánh lỗi; không lỗi500.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Validation khoảng giá hiện nằm tại /product/list sau khi URL list-by-price chuyển hướng.
- **Script:** `org.example.asm_java5.controller.ControllerTests#invalidPriceRangeShowsError` → **Pass** (0.019 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-008 - Lọc không có kết quả

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** Loại rỗng; hoặc khoảng giá không chứa sản phẩm
- **Bước thực hiện Y2:** Mở từng bộ lọc
- **Kỳ vọng Y2:** Danh sách rỗng có thể hiểu được; không hiển thị kết quả cũ gây hiểu nhầm.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#emptySearchRendersEmptyResults` → **Pass** (0.225 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-009 - Chi tiết và hàng cùng loại

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** P1,P2 thuộc C1; P3 thuộc C2
- **Bước thực hiện Y2:** Mở /product/detail/{P1}; đối chiếu phần liên quan
- **Kỳ vọng Y2:** Chi tiết đúng P1; liên quan có P2, không có P1 hoặc P3.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#detailLoadsRelatedProductsWithoutSelf` → **Pass** (0.068 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-010 - Sản phẩm không tồn tại

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** id=999999 không có trong DB
- **Bước thực hiện Y2:** Mở /product/detail/999999
- **Kỳ vọng Y2:** Chuyển về /product/list; không lỗi 500.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#missingProductRedirects` → **Pass** (0.015 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-011 - Sai kiểu hoặc thiếu tham số giá

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** DB test đã chuẩn bị; không cần đăng nhập
- **Dữ liệu Y2:** minPrice=abc,maxPrice=10; sau đó bỏ maxPrice
- **Bước thực hiện Y2:** Gửi từng GET riêng
- **Kỳ vọng Y2:** HTTP 400 và trang lỗi dữ liệu; không truy vấn thành công với giá bị diễn giải sai.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#malformedPriceIsBadRequest` → **Pass** (0.051 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-012 - Khách thêm hàng

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Khách; giỏ rỗng
- **Dữ liệu Y2:** P1 đang bán
- **Bước thực hiện Y2:** Mở chi tiết P1; nhấn thêm; mở /cart/index
- **Kỳ vọng Y2:** Có 1 dòng P1 số lượng 1; không yêu cầu đăng nhập.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#anonymousCartPostWithCsrfWorks` → **Pass** (0.026 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.service.CartServiceTests#addAvailableProduct` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-013 - Thêm lặp cùng sản phẩm

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ có P1 quantity=1
- **Dữ liệu Y2:** P1
- **Bước thực hiện Y2:** Thêm P1 lần nữa; mở giỏ
- **Kỳ vọng Y2:** Một dòng P1 quantity=2; không nhân đôi dòng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#mergeRepeatedProduct` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-014 - Hiển thị và tính tiền

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ rỗng
- **Dữ liệu Y2:** P1 giá 100000 giảm 10%, quantity=2; P2 giá 200000 giảm 0%, quantity=1
- **Bước thực hiện Y2:** Thêm P1,P2; cập nhật số lượng; mở giỏ
- **Kỳ vọng Y2:** Đơn giá P1=90000.00; thành tiền=180000.00; P2=200000.00; tổng=380000.00.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#totalUsesDiscountAndQuantity` → **Pass** (0.003 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-015 - Cập nhật số lượng tại biên

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ có P1
- **Dữ liệu Y2:** quantity=1; sau đó 99
- **Bước thực hiện Y2:** POST /cart/update với productId=P1 và từng quantity
- **Kỳ vọng Y2:** Lưu đúng 1 và 99; tổng tiền cập nhật tương ứng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#quantityBoundaries(int)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Script:** `org.example.asm_java5.service.CartServiceTests#quantityBoundaries(int)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-016 - Số lượng ngoài miền

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ P1 quantity=2
- **Dữ liệu Y2:** quantity=0,-1,100 (3 lượt độc lập)
- **Bước thực hiện Y2:** POST /cart/update với từng giá trị
- **Kỳ vọng Y2:** Báo số lượng phải từ 1 đến 99; giỏ vẫn quantity=2.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectInvalidQuantityWithoutMutation(int)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectInvalidQuantityWithoutMutation(int)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectInvalidQuantityWithoutMutation(int)[3]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-017 - Thêm quá giới hạn

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ P1 quantity=99
- **Dữ liệu Y2:** P1
- **Bước thực hiện Y2:** Thêm P1 lần nữa
- **Kỳ vọng Y2:** Báo tối đa 99; số lượng không tăng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectHundredthItem` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-018 - Thêm hàng không tồn tại hoặc ngừng bán

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ rỗng
- **Dữ liệu Y2:** ID không có; P_OFF available=false
- **Bước thực hiện Y2:** POST /cart/add/{id} với từng ID
- **Kỳ vọng Y2:** Báo không tìm thấy hoặc đã ngừng bán; giỏ không đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectMissingAndUnavailableProducts` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-019 - Cập nhật hàng không trong giỏ

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ chỉ có P1
- **Dữ liệu Y2:** productId=P2; quantity=2
- **Bước thực hiện Y2:** POST /cart/update
- **Kỳ vọng Y2:** Báo sản phẩm không có trong giỏ; không tự thêm P2.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#rejectUpdateAbsentProduct` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-020 - Xóa một dòng

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ có P1 và P2
- **Dữ liệu Y2:** P1
- **Bước thực hiện Y2:** Nhấn xóa P1
- **Kỳ vọng Y2:** Chỉ P1 bị xóa; P2 còn nguyên; tổng tính lại.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#removeOnlySelectedLine` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-021 - Xóa toàn bộ và xóa giỏ rỗng

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Giỏ có P1,P2
- **Dữ liệu Y2:** Không
- **Bước thực hiện Y2:** POST /cart/clear; lặp lại khi đã rỗng
- **Kỳ vọng Y2:** Giỏ rỗng, tổng 0; thao tác lặp không gây lỗi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#clearIsIdempotent` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-022 - Cách ly phiên

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** Hai trình duyệt độc lập A,B
- **Dữ liệu Y2:** A thêm P1; B thêm P2
- **Bước thực hiện Y2:** Mở giỏ trên cả hai phiên
- **Kỳ vọng Y2:** A chỉ có P1; B chỉ có P2.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#cartIsActuallySessionScoped` → **Pass** (0.245 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-023 - Làm tròn tiền

- **Yêu cầu:** BR-02
- **Tiền điều kiện Y2:** P có price=10.05,discount=10; giỏ rỗng
- **Dữ liệu Y2:** quantity=3
- **Bước thực hiện Y2:** Thêm P; cập nhật số lượng=3
- **Kỳ vọng Y2:** Đơn giá 9.05 do HALF_UP 2 chữ số; tổng 27.15.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.CartServiceTests#roundUnitPriceBeforeMultiplication` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.CartServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-024 - Đăng ký hợp lệ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Username/email chưa dùng; SMTP test hoạt động
- **Dữ liệu Y2:** username=qa_new; fullName=Nguyễn Kiểm Thử; email=hộp thư test thực; password=Test@1234
- **Bước thực hiện Y2:** GET /account/sign-up; nhập dữ liệu; gửi; xem DB và thư
- **Kỳ vọng Y2:** Tạo 1 account; admin=false,activated=false; mật khẩu BCrypt, không phải bản rõ; có link kích hoạt hạn 24 giờ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#registerHashesAndRequiresActivation` → **Pass** (0.171 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#registrationPersistsInactiveHashedAccount` → **Pass** (1.985 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-025 - Đăng ký thiếu dữ liệu

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Chưa có qa_new
- **Dữ liệu Y2:** Lần lượt bỏ trống username/fullName/email/password; thử toàn khoảng trắng
- **Bước thực hiện Y2:** Gửi từng lượt đăng ký độc lập
- **Kỳ vọng Y2:** Báo dữ liệu bắt buộc; không tạo tài khoản.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#blankRegistrationCannotWrite` → **Pass** (0.718 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-026 - Trùng username hoặc email

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Đã có U1/email U1
- **Dữ liệu Y2:** Username U1 với email mới; username mới với email U1
- **Bước thực hiện Y2:** Đăng ký từng lượt
- **Kỳ vọng Y2:** Từ chối trùng; không tạo thêm bản ghi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#duplicateUsernameCannotWrite` → **Pass** (0.019 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectDuplicateEmail` → **Pass** (0.058 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#duplicateRegistrationRejectedInBrowser` → **Pass** (1.81 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-027 - Email sai định dạng

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Username mới
- **Dữ liệu Y2:** abc; a@b; a b@c.com
- **Bước thực hiện Y2:** Đăng ký từng email
- **Kỳ vọng Y2:** Báo email không hợp lệ; DB không có tài khoản mới.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectInvalidEmail(String)[1]` → **Pass** (0.057 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectInvalidEmail(String)[2]` → **Pass** (0.058 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectInvalidEmail(String)[3]` → **Pass** (0.061 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-028 - Biên độ dài hồ sơ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Dữ liệu còn lại hợp lệ
- **Dữ liệu Y2:** username 50/51; fullName 100/101; email hợp lệ 255/256 ký tự
- **Bước thực hiện Y2:** Thử riêng từng trường và từng độ dài
- **Kỳ vọng Y2:** 50/100/255 được chấp nhận; 51/101/256 bị từ chối; không cắt âm thầm.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#emailLengthBoundaryWithValidSyntax` → **Pass** (1.616 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#profileLengthBoundaries(int, int, boolean)[1]` → **Pass** (0.073 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#profileLengthBoundaries(int, int, boolean)[2]` → **Pass** (0.062 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#profileLengthBoundaries(int, int, boolean)[3]` → **Pass** (0.057 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-029 - Biên mật khẩu

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Username/email mới mỗi lượt
- **Dữ liệu Y2:** 7 và 8 ký tự ASCII; 72 và 73 byte UTF-8; mẫu Unicode 25 chữ á=50 byte và 37 chữ á=74 byte
- **Bước thực hiện Y2:** Đăng ký mỗi dữ liệu độc lập
- **Kỳ vọng Y2:** 7 ký tự hoặc >72 byte bị từ chối; 8 ký tự và 72 byte hợp lệ nếu không trắng; đo byte Unicode đúng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectWeakPassword(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectWeakPassword(String)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#passwordByteBoundaries` → **Pass** (0.003 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-030 - Kích hoạt hợp lệ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Account chưa kích hoạt; token còn hạn
- **Dữ liệu Y2:** Token từ thư
- **Bước thực hiện Y2:** GET /account/activate?token=...; xem DB; đăng nhập
- **Kỳ vọng Y2:** activated=true; token và hạn được xóa; chuyển /auth/login; đăng nhập được.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#activateValidTokenAndConsumeIt` → **Pass** (0.056 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#activationLinkAllowsNewAccountLogin` → **Pass** (2.52 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-031 - Kích hoạt sai/hết hạn/tái sử dụng

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Account chưa kích hoạt riêng cho mỗi lượt
- **Dữ liệu Y2:** Token giả; expiry=now-1 phút; token đã sử dụng
- **Bước thực hiện Y2:** Mở từng link riêng
- **Kỳ vọng Y2:** Báo mã không hợp lệ hoặc hết hạn; không kích hoạt sai tài khoản.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#activationAndResetTokensCannotBeReused` → **Pass** (0.446 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectMissingAndExpiredActivation` → **Pass** (0.06 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-032 - Gửi lại kích hoạt

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Account chưa kích hoạt có token; SMTP test
- **Dữ liệu Y2:** Email account đó
- **Bước thực hiện Y2:** POST /account/resend-activation; mở token cũ rồi token mới
- **Kỳ vọng Y2:** Token mới hạn 24 giờ; token cũ vô hiệu; token mới kích hoạt được.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#resendReplacesActivationToken` → **Pass** (0.061 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-033 - SMTP lỗi khi đăng ký

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** SMTP test cấu hình thất bại
- **Dữ liệu Y2:** Tài khoản mới hợp lệ
- **Bước thực hiện Y2:** Gửi đăng ký; đối chiếu DB
- **Kỳ vọng Y2:** Thông báo đã tạo account nhưng chưa gửi thư; account còn chưa kích hoạt; không báo thành công gửi thư.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#smtpFailureKeepsInactiveAccountAndShowsError` → **Pass** (0.449 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-034 - Cập nhật hồ sơ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập
- **Dữ liệu Y2:** fullName=Nguyễn An; email giữ nguyên
- **Bước thực hiện Y2:** GET /account/edit-profile; đổi tên; lưu; tải lại
- **Kỳ vọng Y2:** Lưu đúng tên; giữ activated và username; không đổi quyền admin.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#updateProfilePreservesRoleAndActivation` → **Pass** (0.305 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-035 - Đổi email cần kích hoạt

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập; email mới chưa dùng
- **Dữ liệu Y2:** Email mới nhận được thư
- **Bước thực hiện Y2:** Đổi email tại hồ sơ; lưu; truy cập trang bảo vệ; kích hoạt email mới
- **Kỳ vọng Y2:** Lưu email mới,activated=false; gửi link; phiên không còn dùng chức năng bảo vệ; kích hoạt rồi đăng nhập lại được.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#newEmailRequiresReactivationAndRevokesSession` → **Pass** (0.303 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-036 - Hồ sơ không hợp lệ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập
- **Dữ liệu Y2:** Tên trắng; email sai; email của U2
- **Bước thực hiện Y2:** Lưu từng lượt; truy vấn DB sau mỗi lượt
- **Kỳ vọng Y2:** Báo lỗi; dữ liệu không hợp lệ không được lưu.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#invalidProfileDoesNotWrite` → **Pass** (0.28 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-037 - Đổi mật khẩu thành công

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập với Test@1234
- **Dữ liệu Y2:** oldPassword=Test@1234; password=New@12345; confirmPassword=New@12345
- **Bước thực hiện Y2:** POST /account/change-password; thử đăng nhập bằng cũ rồi mới
- **Kỳ vọng Y2:** Đăng xuất; mật khẩu cũ thất bại, mới thành công; DB lưu hash mới.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#changePasswordClearsResetToken` → **Pass** (0.233 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-038 - Đổi mật khẩu thất bại

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập
- **Dữ liệu Y2:** Sai oldPassword; xác nhận không khớp; password 7 ký tự (3 lượt)
- **Bước thực hiện Y2:** Gửi từng lượt; thử lại mật khẩu hiện tại
- **Kỳ vọng Y2:** Báo lỗi tương ứng; hash cũ không đổi; không khóa tài khoản.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#passwordConfirmationMismatchDoesNotChangeHash` → **Pass** (0.921 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#wrongOldPasswordDoesNotWrite` → **Pass** (0.111 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-039 - Quên mật khẩu

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 activated=true; SMTP test
- **Dữ liệu Y2:** Email U1
- **Bước thực hiện Y2:** POST /account/forgot-password; xem thư và DB
- **Kỳ vọng Y2:** Có token đặt lại hạn 30 phút; phản hồi chung không tiết lộ tài khoản.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#prepareResetExpiresInThirtyMinutes` → **Pass** (0.056 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-040 - Email lạ/chưa kích hoạt

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Có account chưa kích hoạt
- **Dữ liệu Y2:** Email không tồn tại; email account chưa kích hoạt
- **Bước thực hiện Y2:** Gửi quên mật khẩu từng lượt
- **Kỳ vọng Y2:** Phản hồi chung giống trường hợp hợp lệ; không gửi link/reset token cho hai trường hợp này.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#unknownOrInactiveAccountCannotReset` → **Pass** (0.059 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-041 - Đặt lại mật khẩu thành công

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 có reset token còn hạn
- **Dữ liệu Y2:** password=Reset@123; confirmPassword=Reset@123
- **Bước thực hiện Y2:** Mở /account/reset-password?token=...; gửi mật khẩu mới; đăng nhập
- **Kỳ vọng Y2:** Hash đổi; reset token/hạn được xóa; mật khẩu mới đăng nhập được.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AccountServiceTests#resetConsumesTokenAndChangesHash` → **Pass** (0.35 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-042 - Đặt lại token sai/hết hạn/dùng lại

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Các account test độc lập
- **Dữ liệu Y2:** Token giả; expiry quá hạn; token đã reset thành công
- **Bước thực hiện Y2:** Gửi reset với từng token
- **Kỳ vọng Y2:** Báo link không hợp lệ hoặc hết hạn; mật khẩu không thay đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#activationAndResetTokensCannotBeReused` → **Pass** (0.446 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.AccountServiceTests#rejectExpiredOrInvalidReset` → **Pass** (0.059 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AccountServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-043 - Reset xác nhận sai hoặc mật khẩu ngắn

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** Token còn hạn
- **Dữ liệu Y2:** password=Reset@123,confirmPassword=khac; sau đó password=1234567
- **Bước thực hiện Y2:** Gửi từng dữ liệu
- **Kỳ vọng Y2:** Báo lỗi; không đổi mật khẩu hoặc tiêu thụ token hợp lệ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AccountIntegrationTests#resetConfirmationMismatchPreservesTokenAndPassword` → **Pass** (0.292 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AccountIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-044 - Ảnh hồ sơ hợp lệ

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập
- **Dữ liệu Y2:** PNG 100x100 nhỏ hơn 2MB
- **Bước thực hiện Y2:** POST multipart /account/photo với photo; tải lại hồ sơ
- **Kỳ vọng Y2:** Lưu tên UUID.png trong uploads; DB photo trỏ đúng; ảnh hiển thị.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Controller dùng mock kho file; FileService thật được kiểm tra bằng JVM con trong thư mục tạm riêng, không ghi vào uploads của ứng dụng chính.
- **Script:** `org.example.asm_java5.integration.UploadIntegrationTests#profileUploadSavesReturnedFilename` → **Pass** (0.21 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.UploadIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.FileServiceTests#savesValidImageInIsolatedProcess(Path)` → **Pass** (0.29 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.FileServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-045 - Ảnh lỗi và quá giới hạn

- **Yêu cầu:** BR-03
- **Tiền điều kiện Y2:** U1 đăng nhập
- **Dữ liệu Y2:** File rỗng; văn bản đổi đuôi jpg; ảnh >2MB; ảnh chiều rộng 5001px
- **Bước thực hiện Y2:** Upload mỗi mẫu độc lập
- **Kỳ vọng Y2:** Từ chối, không đổi ảnh DB. File quá giới hạn có thể nhận HTTP 400 từ FormErrors; không lỗi 500.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.FileServiceTests#rejectsEmptyFakeAndOversizedFiles` → **Pass** (0.006 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.FileServiceTests.xml).
- **Script:** `org.example.asm_java5.service.FileServiceTests#rejectsImageWiderThanFiveThousandPixels` → **Pass** (0.017 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.FileServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-046 - Đăng nhập user/admin

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1 và A1 active; mật khẩu test đã biết
- **Dữ liệu Y2:** U1; A1
- **Bước thực hiện Y2:** GET /auth/login; đăng nhập mỗi role trong phiên riêng
- **Kỳ vọng Y2:** U1 về /; A1 về /admin/product/index khi không có returnUrl; session ID được đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AuthServiceTests#loginStoresIdentityAndPasswordStamp` → **Pass** (0.117 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AuthServiceTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#userAndAdminLoginRedirectByRole` → **Pass** (7.319 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-047 - Sai thông tin hoặc bị khóa

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** Có U1 active và U_LOCK inactive
- **Dữ liệu Y2:** Sai password; username lạ; U_LOCK đúng password
- **Bước thực hiện Y2:** Đăng nhập từng lượt
- **Kỳ vọng Y2:** Không xác thực; lỗi sai thông tin hoặc chưa kích hoạt/bị khóa; không vào chức năng bảo vệ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AuthServiceTests#wrongPasswordAndInactiveCannotLogin` → **Pass** (0.177 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AuthServiceTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#badPasswordAndInactiveAccountCannotLogin` → **Pass** (2.13 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-048 - Khách vào chức năng bảo vệ

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** Phiên mới chưa login
- **Dữ liệu Y2:** /account/edit-profile; /account/change-password; /order/checkout; /order/list; /order/detail/{O1}; /order/my-product-list
- **Bước thực hiện Y2:** GET từng URL trong phiên mới riêng
- **Kỳ vọng Y2:** Mỗi URL chuyển /auth/login; không lộ nội dung riêng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#guestProtectedPageRedirects` → **Pass** (0.015 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[1]` → **Pass** (0.211 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[2]` → **Pass** (0.185 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[3]` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[4]` → **Pass** (0.202 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[5]` → **Pass** (0.221 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#protectedPagesRequireLogin(String)[6]` → **Pass** (0.196 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-049 - Quay lại URL yêu cầu

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** Khách chưa login
- **Dữ liệu Y2:** /order/list; /account/edit-profile
- **Bước thực hiện Y2:** Mở từng URL trong phiên riêng; đăng nhập U1
- **Kỳ vọng Y2:** Quay đúng URL đã yêu cầu; không luôn về trang chủ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#loginReturnsToProtectedPage` → **Pass** (0.136 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#loginReturnsToOriginalProtectedPage` → **Pass** (1.659 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-050 - Khách truy cập quản trị

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** Chưa login
- **Dữ liệu Y2:** Các /admin/category/index,product/index,account/index,order/index,report/revenue,report/vip
- **Bước thực hiện Y2:** GET từng URL
- **Kỳ vọng Y2:** Chuyển đăng nhập; không đọc được dữ liệu quản trị.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#guestAdminRedirects` → **Pass** (0.015 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[1]` → **Pass** (0.185 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[2]` → **Pass** (0.179 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[3]` → **Pass** (0.181 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[4]` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[5]` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[6]` → **Pass** (0.202 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-051 - User thường không được quản trị

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1 login; csrf hợp lệ
- **Dữ liệu Y2:** Các GET quản trị và POST create/update/delete của cả 4 module
- **Bước thực hiện Y2:** Gửi từng request; đối chiếu DB
- **Kỳ vọng Y2:** HTTP 403; không có thay đổi DB; không chỉ ẩn nút trên giao diện.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#userCannotCreateAdminCategory` → **Pass** (0.11 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[1]` → **Pass** (0.185 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[2]` → **Pass** (0.179 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[3]` → **Pass** (0.181 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[4]` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[5]` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#allAdminPagesRejectGuestAndUser(String)[6]` → **Pass** (0.202 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-052 - CSRF sai hoặc thiếu

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1/A1 login theo quyền; ghi lại DB trước test
- **Dữ liệu Y2:** Bỏ csrf hoặc dùng csrf=wrong
- **Bước thực hiện Y2:** POST cart/update, account/edit-profile, order/checkout và admin/product/create từng lượt
- **Kỳ vọng Y2:** HTTP 403; không thay đổi dữ liệu.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#missingCsrfPreventsMutation` → **Pass** (0.025 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-053 - Đăng xuất

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1 login, giỏ có P1
- **Dữ liệu Y2:** POST /auth/logout với csrf đúng
- **Bước thực hiện Y2:** Đăng xuất; mở lại /order/list và /cart/index
- **Kỳ vọng Y2:** Phiên cũ vô hiệu; đơn yêu cầu login; giỏ phiên mới rỗng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AuthServiceTests#logoutInvalidatesSession` → **Pass** (0.123 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AuthServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-054 - Đổi mật khẩu làm hết phiên khác

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1 login trong hai trình duyệt A,B
- **Dữ liệu Y2:** A đổi mật khẩu thành công
- **Bước thực hiện Y2:** Tại B truy cập /order/list sau khi A đổi
- **Kỳ vọng Y2:** B phải đăng nhập lại vì authStamp không khớp hash mới.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AuthServiceTests#changedHashRevokesExistingSession` → **Pass** (0.117 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AuthServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-055 - Chống xem đơn người khác

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** U1 login; O2 thuộc U2
- **Dữ liệu Y2:** GET /order/detail/{O2}; GET id không tồn tại
- **Bước thực hiện Y2:** Truy cập hai URL riêng
- **Kỳ vọng Y2:** Đơn U2 trả 403; đơn không tồn tại trả 404; không lộ chi tiết.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#orderOwnershipAndMissingId` → **Pass** (0.211 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-056 - Không thay đổi dữ liệu bằng GET

- **Yêu cầu:** BR-05
- **Tiền điều kiện Y2:** A1 login; có bản ghi test
- **Dữ liệu Y2:** GET /cart/clear và /admin/product/delete/{P1}
- **Bước thực hiện Y2:** Gửi GET; đối chiếu giỏ/DB
- **Kỳ vọng Y2:** Không xóa; route chỉ nhận POST; thông thường HTTP 405.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#getCannotDeleteProductOrClearCart` → **Pass** (0.27 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-057 - Checkout thành công

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1x2,P2x1; key mới
- **Dữ liệu Y2:** P1=100000 giảm10%; P2=200000; address=12 Lê Lợi; phone=0901234567
- **Bước thực hiện Y2:** GET /order/checkout; gửi form; xem DB và giỏ
- **Kỳ vọng Y2:** Tạo đúng 1 orders thuộc U1,status=PENDING và 2 dòng; giá 90000/200000, tổng 380000; giỏ xóa sau thành công; chuyển chi tiết đơn.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#checkoutPersistsHeaderLinesAndTotal` → **Pass** (0.216 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-058 - Checkout giỏ rỗng

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ rỗng; key hợp lệ
- **Dữ liệu Y2:** Địa chỉ/điện thoại hợp lệ
- **Bước thực hiện Y2:** Mở checkout; gửi
- **Kỳ vọng Y2:** Báo Giỏ hàng trống; không tạo orders/order_details.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#emptyCartCannotCreateOrder` → **Pass** (0.059 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-059 - Địa chỉ tại biên

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1; mỗi lượt key hợp lệ
- **Dữ liệu Y2:** Địa chỉ 1 và 500 ký tự không trắng
- **Bước thực hiện Y2:** Gửi mỗi lượt độc lập; khôi phục giỏ
- **Kỳ vọng Y2:** Tạo đơn được; địa chỉ lưu đã trim.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#addressBoundaries(int)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#addressBoundaries(int)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-060 - Địa chỉ sai

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1
- **Dữ liệu Y2:** Rỗng; toàn khoảng trắng; 501 ký tự
- **Bước thực hiện Y2:** Checkout mỗi giá trị riêng
- **Kỳ vọng Y2:** Báo địa chỉ từ 1 đến 500 ký tự; DB không có đơn mới; giỏ giữ nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#blankAddress(String)[1]` → **Pass** (0.229 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#blankAddress(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#tooLongAddress` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-061 - Điện thoại hợp lệ theo code

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1
- **Dữ liệu Y2:** 0901234567; chuỗi 8 và 20 chữ số; +84 (90)-1234567
- **Bước thực hiện Y2:** Gửi từng mẫu; khôi phục giỏ/key
- **Kỳ vọng Y2:** Chấp nhận các mẫu thuộc regex [+0-9 ()-]{8,20}.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#validPhones(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#validPhones(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#validPhones(String)[3]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-062 - Điện thoại sai

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1
- **Dữ liệu Y2:** 7 hoặc 21 chữ số; 090abc1234
- **Bước thực hiện Y2:** Gửi từng mẫu
- **Kỳ vọng Y2:** Từ chối; không tạo đơn; giỏ giữ nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#invalidPhones(String)[1]` → **Pass** (0.0 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#invalidPhones(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#invalidPhones(String)[3]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-063 - Giá thay đổi trước checkout

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 có P1 giá cũ 100000 giảm10% trong giỏ
- **Dữ liệu Y2:** Admin đổi P1 thành 120000 giảm10%
- **Bước thực hiện Y2:** Admin lưu giá mới; U1 mở và gửi checkout; xem chi tiết DB
- **Kỳ vọng Y2:** OrderDetail dùng giá mới 108000.00 từ DB. Kiểm tra chênh lệch giá hiển thị trước xác nhận; cần thống nhất UX khi giá thay đổi.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Script xác nhận giá mới lấy từ DB; chưa đánh giá thủ công trải nghiệm người dùng khi giá thay đổi.
- **Script:** `org.example.asm_java5.service.OrderServiceTests#checkoutRefreshesPriceFromDatabase` → **Pass** (0.066 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-064 - Rollback khi hàng ngừng bán

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** Giỏ P1,P2 đang bán; U1 login
- **Dữ liệu Y2:** Sau khi thêm, admin chuyển P2 available=false
- **Bước thực hiện Y2:** Checkout; kiểm tra cả hai bảng và giỏ
- **Kỳ vọng Y2:** Báo hàng không còn bán; không còn đơn/dòng dở dang; giỏ giữ nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#checkoutRollsBackHeaderAndEarlierLines` → **Pass** (0.204 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-065 - Chống gửi đơn hai lần

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1; key K
- **Dữ liệu Y2:** Cùng csrf và checkoutKey=K
- **Bước thực hiện Y2:** Gửi cùng request hai lần liên tiếp hoặc đồng thời trong một session
- **Kỳ vọng Y2:** Chỉ một đơn mới; lần sau bị báo key đã dùng; không nhân đôi dòng.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Script gửi lặp tuần tự cùng checkoutKey; chưa stress hai request đồng thời.
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#checkoutKeyCanOnlyCreateOneOrder` → **Pass** (0.238 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-066 - Checkout key giả

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 login; giỏ P1; session có key K
- **Dữ liệu Y2:** checkoutKey=wrong
- **Bước thực hiện Y2:** POST /order/checkout với csrf đúng
- **Kỳ vọng Y2:** Báo yêu cầu đã dùng/tải lại giỏ; không tạo đơn; giỏ không mất.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#wrongCheckoutKeyDoesNotLoseCart` → **Pass** (0.218 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-067 - Danh sách và chi tiết đơn của mình

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 có O1,O3; U2 có O2
- **Dữ liệu Y2:** GET /order/list; /order/detail/{O1}
- **Bước thực hiện Y2:** Mở danh sách; mở O1; đối chiếu DB
- **Kỳ vọng Y2:** Danh sách chỉ U1 theo id giảm dần; chi tiết đúng hàng, giá chốt, số lượng, địa chỉ, tổng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#historiesOnlyContainOwnersAndCompletedPurchases` → **Pass** (0.223 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-068 - My Products

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** U1 có đơn COMPLETED và PENDING/CANCELLED; U2 có COMPLETED
- **Dữ liệu Y2:** GET /order/my-product-list
- **Bước thực hiện Y2:** Mở trang; so các dòng order_details
- **Kỳ vọng Y2:** Chỉ dòng thuộc đơn COMPLETED của U1; không có đơn chưa hoàn tất hoặc của U2.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#historiesOnlyContainOwnersAndCompletedPurchases` → **Pass** (0.223 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-069 - Giữ giá lịch sử

- **Yêu cầu:** BR-04
- **Tiền điều kiện Y2:** O1 đã đặt với P1 đơn giá 90000
- **Dữ liệu Y2:** Admin đổi giá/discount P1
- **Bước thực hiện Y2:** Mở lại O1
- **Kỳ vọng Y2:** Đơn vẫn dùng giá OrderDetail 90000; không bị giá danh mục hiện tại thay đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#orderPricesRemainHistorical` → **Pass** (0.195 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-070 - Tạo và đọc loại

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** name=Loại QA mới chưa tồn tại
- **Bước thực hiện Y2:** Mở /admin/category/index; POST create; mở edit/{id}
- **Kỳ vọng Y2:** Tạo 1 loại; có ID; danh sách và form sửa hiển thị đúng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#createAndReadCategoryViaBrowser` → **Pass** (1.975 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-071 - Tên loại trắng hoặc trùng

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** name=   ; sau đó tên C1 đã có
- **Bước thực hiện Y2:** POST create từng lượt
- **Kỳ vọng Y2:** Báo bắt buộc/trùng; không thêm bản ghi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#duplicateCategoryCannotWrite` → **Pass** (0.105 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#duplicateCategoryRejectedViaBrowser` → **Pass** (1.858 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-072 - Sửa loại hợp lệ

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** C1; tên mới chưa dùng
- **Bước thực hiện Y2:** GET edit/{C1}; POST update với id/name
- **Kỳ vọng Y2:** Cập nhật cùng ID; không tạo loại mới.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#updateCategoryViaBrowser` → **Pass** (2.404 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-073 - Biên tên khi sửa

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** name 100 và 101 ký tự; tên loại khác đã dùng
- **Bước thực hiện Y2:** POST update từng lượt độc lập
- **Kỳ vọng Y2:** 100 hợp lệ; 101 hoặc trùng loại khác bị từ chối; DB giữ bản trước khi lỗi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.controller.ControllerTests#categoryUpdateRejectsLongName` → **Pass** (0.134 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.controller.ControllerTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-074 - Biên tên khi tạo - kiểm tra khoảng trống validation

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** name 101 ký tự
- **Bước thực hiện Y2:** POST /admin/category/create với CSRF/admin hợp lệ và name101 ký tự; đối chiếu DB
- **Kỳ vọng Y2:** HTTP400 do CategoryService.save từ chối tên>100; không thêm loại mới.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Source hiện tại chặn tên quá 100 tại CategoryService.save; test xác nhận HTTP400 và không thêm bản ghi. Khoảng trống validation của snapshot cũ không tái hiện.
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#categoryCreateRejectsNameOverOneHundred` → **Pass** (0.184 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-075 - Xóa loại không có hàng

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** C_EMPTY không có sản phẩm
- **Bước thực hiện Y2:** POST /admin/category/delete/{C_EMPTY}
- **Kỳ vọng Y2:** Xóa thành công; không còn trong DB/danh sách.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#deleteEmptyCategoryViaBrowser` → **Pass** (1.747 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-076 - Chặn xóa loại có hàng

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** C1 có P1
- **Bước thực hiện Y2:** POST /admin/category/delete/{C1}
- **Kỳ vọng Y2:** Báo không thể xóa loại đang có sản phẩm; C1/P1 còn nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#relatedRecordsCannotBeDeleted` → **Pass** (0.221 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#cannotDeleteCategoryContainingProductsViaBrowser` → **Pass** (2.02 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-077 - Sửa loại không tồn tại

- **Yêu cầu:** BR-06
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; dữ liệu test riêng
- **Dữ liệu Y2:** id=999999
- **Bước thực hiện Y2:** GET /admin/category/edit/999999
- **Kỳ vọng Y2:** Chuyển /admin/category/index; không lỗi 500.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#missingEditIdsRedirect` → **Pass** (0.191 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-078 - Tạo/đọc sản phẩm

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** name=Bình QA; price=100000; discount=10; categoryId=C1; image=vase.svg; available=true
- **Bước thực hiện Y2:** Mở index; POST create; mở edit/{id}
- **Kỳ vọng Y2:** Tạo 1 P; ngày tạo hôm nay; tất cả trường đúng DB và form.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#productCreateAndRead` → **Pass** (0.277 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-079 - Sửa sản phẩm

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** P1; name=Bình QA sửa; price=120000; discount=5; categoryId=C2
- **Bước thực hiện Y2:** POST update với id=P1
- **Kỳ vọng Y2:** Cùng ID cập nhật đúng; ngày tạo không bị thay mới; storefront phản ánh dữ liệu.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#productUpdateKeepsIdAndCreationDate` → **Pass** (0.257 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-080 - Tên bắt buộc và độ dài

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** name trắng; 200 và 201 ký tự
- **Bước thực hiện Y2:** Create/update từng lượt, các trường khác hợp lệ
- **Kỳ vọng Y2:** Trắng/201 bị từ chối; 200 chấp nhận; không lưu lỗi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.ProductValidationTests#nameBoundaries` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-081 - Biên giá

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** price=-0.01;0;99999999999999999.99;100000000000000000.00;1.001
- **Bước thực hiện Y2:** POST create từng giá trị
- **Kỳ vọng Y2:** 0 và max hợp lệ; âm, vượt max hoặc quá 2 số thập phân bị từ chối.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.ProductServiceTests#rejectsNegativePriceBeforeDatabaseWrite` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductServiceTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidPrice(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidPrice(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidPrice(String)[3]` → **Pass** (0.003 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#acceptPriceBoundaries(String)[1]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#acceptPriceBoundaries(String)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-082 - Biên giảm giá

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** discount=-0.01;0;100;100.01;1.001
- **Bước thực hiện Y2:** POST create từng giá trị
- **Kỳ vọng Y2:** 0/100 chấp nhận; âm, >100 hoặc >2 chữ số thập phân bị từ chối.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidDiscount(String)[1]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidDiscount(String)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectInvalidDiscount(String)[3]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#acceptDiscountBoundaries(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#acceptDiscountBoundaries(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-083 - Thiếu loại hoặc loại giả

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** categoryId trống; categoryId=999999
- **Bước thực hiện Y2:** POST create/update mỗi lượt
- **Kỳ vọng Y2:** Báo chọn loại hàng; không lưu FK sai.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.ProductValidationTests#categoryRequired` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-084 - Tên file ảnh không an toàn

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** image=../x.png; a/b.png; chuỗi có dấu gạch chéo ngược; 256 ký tự
- **Bước thực hiện Y2:** POST create từng mẫu
- **Kỳ vọng Y2:** Từ chối đường dẫn hoặc tên >255; không truy cập/ghi file ngoài uploads.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectImagePaths(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectImagePaths(String)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductValidationTests#rejectImagePaths(String)[3]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductValidationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-085 - Upload ảnh sản phẩm

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** imageFile=ảnh PNG nhỏ hơn 2MB
- **Bước thực hiện Y2:** Chọn ảnh PNG hợp lệ tại form create/update; gửi multipart
- **Kỳ vọng Y2:** Ảnh hợp lệ được xử lý và tên file được lưu cùng sản phẩm; không ghi đè file ngoài vùng lưu trữ.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Controller dùng mock kho file; FileService thật có test độc lập trong thư mục tạm. Không còn lỗi imageFile ở source hiện tại.
- **Script:** `org.example.asm_java5.integration.UploadIntegrationTests#productUploadSavesReturnedFilename` → **Pass** (0.27 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.UploadIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.FileServiceTests#savesValidImageInIsolatedProcess(Path)` → **Pass** (0.29 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.FileServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-086 - Ngừng bán

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** P1 đang bán
- **Bước thực hiện Y2:** Update available=false; thử POST /cart/add/{P1}
- **Kỳ vọng Y2:** Lưu ngừng bán; không cho thêm mới vào giỏ; không xuất hiện top 4 trang chủ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#unavailableProductCannotBePurchasedOrListed` → **Pass** (0.335 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-087 - Xóa hàng chưa phát sinh đơn

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** P_NEW không có order_details
- **Bước thực hiện Y2:** POST delete/{P_NEW}
- **Kỳ vọng Y2:** Bản ghi bị xóa; danh sách không còn.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#deleteUnreferencedProduct` → **Pass** (0.183 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductServiceTests#deletesProductWithoutOrders` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-088 - Chặn xóa hàng đã có đơn

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** P1 có order_details
- **Bước thực hiện Y2:** POST delete/{P1}
- **Kỳ vọng Y2:** Báo chuyển sang Ngừng bán; hàng và lịch sử đơn còn nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#relatedRecordsCannotBeDeleted` → **Pass** (0.221 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.ProductServiceTests#cannotDeleteProductWithOrderHistory` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.ProductServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-089 - Sửa hàng không tồn tại

- **Yêu cầu:** BR-07
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt
- **Dữ liệu Y2:** id=999999
- **Bước thực hiện Y2:** GET edit/999999; POST update với id đó
- **Kỳ vọng Y2:** Chuyển index; không tạo mới sản phẩm hoặc lỗi 500.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#missingEditIdsRedirect` → **Pass** (0.191 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-090 - Tạo/đọc tài khoản

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** qa_staff; họ tên/email hợp lệ; password=Test@1234; admin=false; activated=true
- **Bước thực hiện Y2:** Mở index; POST create; mở edit/qa_staff
- **Kỳ vọng Y2:** Có account đúng vai trò/trạng thái; BCrypt; account active đăng nhập được.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminCreatesActiveUserWithHashedPassword` → **Pass** (0.347 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-091 - Sửa giữ mật khẩu

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** U2; tên mới; password rỗng
- **Bước thực hiện Y2:** POST update; so hash trước/sau
- **Kỳ vọng Y2:** Tên/email/vai trò/trạng thái cập nhật theo form; hash giữ nguyên khi password rỗng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminUpdateWithoutPasswordKeepsHash` → **Pass** (0.187 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-092 - Admin đặt mật khẩu mới

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** U2; password=New@12345
- **Bước thực hiện Y2:** POST update; thử login mật khẩu cũ/mới
- **Kỳ vọng Y2:** Hash đổi; cũ không dùng được; mới dùng được nếu account active.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminChangesPassword` → **Pass** (0.37 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-093 - Dữ liệu tài khoản sai

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** Username trùng lúc create; email U1; tên trắng; mật khẩu mới 7 ký tự
- **Bước thực hiện Y2:** Gửi từng lượt độc lập
- **Kỳ vọng Y2:** Báo không lưu; không tạo hoặc cập nhật một phần dữ liệu không hợp lệ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#badAdminAccountCreationDoesNotPersist` → **Pass** (0.226 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-094 - Không tự khóa/bỏ quyền

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** username=A1 hiện đang login; admin=false hoặc activated=false
- **Bước thực hiện Y2:** POST update từng lượt
- **Kỳ vọng Y2:** Từ chối; A1 vẫn admin và active trong DB.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminCannotDemoteSelf` → **Pass** (0.189 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-095 - Không tự xóa

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** id=A1
- **Bước thực hiện Y2:** POST delete/A1
- **Kỳ vọng Y2:** Báo không thể tự xóa; account còn nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminCannotDeleteSelf` → **Pass** (0.201 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-096 - Xóa account chưa có đơn

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** U_EMPTY chưa có đơn
- **Bước thực hiện Y2:** POST delete/U_EMPTY
- **Kỳ vọng Y2:** Xóa account; không còn trong danh sách.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#deleteUserWithoutOrders` → **Pass** (0.196 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-097 - Chặn xóa account có đơn

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** U1 có O1
- **Bước thực hiện Y2:** POST delete/U1
- **Kỳ vọng Y2:** Báo khóa thay vì xóa; account/đơn còn nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#relatedRecordsCannotBeDeleted` → **Pass** (0.221 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-098 - Khóa user đang có phiên

- **Yêu cầu:** BR-08
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; account test riêng
- **Dữ liệu Y2:** U2 đang login ở trình duyệt khác; A1 update U2 activated=false
- **Bước thực hiện Y2:** Lưu; U2 mở /order/list
- **Kỳ vọng Y2:** U2 bị yêu cầu login lại; không dùng phiên cũ để đọc dữ liệu bảo vệ.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.AuthServiceTests#inactiveAccountRevokesExistingSession` → **Pass** (0.113 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.AuthServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-099 - Tạo/đọc đơn thủ công

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** username=U1; productId=P1; quantity=2; address=12 Lê Lợi; phone=0901234567
- **Bước thực hiện Y2:** POST create; mở index rồi edit/{id}
- **Kỳ vọng Y2:** Tạo đơn PENDING thuộc U1, đúng 1 dòng quantity=2 và giá sau giảm; đọc được chi tiết/tổng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminCreatesOrderAndReadsDetails` → **Pass** (0.312 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-100 - Tạo đơn dữ liệu sai

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** Username lạ; productId lạ; quantity=0/100; P_OFF
- **Bước thực hiện Y2:** POST create mỗi mẫu riêng với các trường khác hợp lệ
- **Kỳ vọng Y2:** Báo lỗi; không có orders hoặc detail dở dang.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminInvalidOrderRollsBack(int)[1]` → **Pass** (0.196 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminInvalidOrderRollsBack(int)[2]` → **Pass** (0.227 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-101 - Sửa địa chỉ/điện thoại/trạng thái

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_PENDING; địa chỉ mới; phone hợp lệ; status=CONFIRMED
- **Bước thực hiện Y2:** POST update; mở lại edit
- **Kỳ vọng Y2:** Lưu đúng địa chỉ/phone/status; dòng hàng và giá không đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#adminUpdatesOrderAddressAndStatus` → **Pass** (0.211 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-102 - Trạng thái không thuộc tập

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** status=UNKNOWN
- **Bước thực hiện Y2:** POST update
- **Kỳ vọng Y2:** Từ chối; status DB không đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#rejectUnknownStatus` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-103 - Đơn kết thúc không đổi trạng thái

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_COMPLETED và O_CANCELLED
- **Bước thực hiện Y2:** Đổi từng đơn sang PENDING
- **Kỳ vọng Y2:** Từ chối; trạng thái kết thúc giữ nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#terminalStatusCannotBeReopened(String)[1]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#terminalStatusCannotBeReopened(String)[2]` → **Pass** (0.002 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-104 - Thêm/sửa dòng hàng PENDING

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_PENDING; P2 chưa có,quantity=2; sau đó P2 quantity=3
- **Bước thực hiện Y2:** POST /admin/order/item hai lượt
- **Kỳ vọng Y2:** Thêm dòng P2 rồi cập nhật cùng dòng thành 3; không trùng dòng; tổng tính lại.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#saveItemMergesProductLineAndRecalculatesTotal` → **Pass** (0.235 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-105 - Chặn sửa dòng không PENDING

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_CONFIRMED, O_SHIPPING, O_COMPLETED, O_CANCELLED
- **Bước thực hiện Y2:** POST item rồi item/delete ở từng trạng thái
- **Kỳ vọng Y2:** Từ chối; các dòng không đổi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#nonPendingOrderLinesAreLocked(String)[1]` → **Pass** (0.196 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#nonPendingOrderLinesAreLocked(String)[2]` → **Pass** (0.208 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#nonPendingOrderLinesAreLocked(String)[3]` → **Pass** (0.195 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#nonPendingOrderLinesAreLocked(String)[4]` → **Pass** (0.206 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-106 - Số lượng dòng sai/hàng ngừng bán

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_PENDING; quantity=0/100; P_OFF
- **Bước thực hiện Y2:** POST item từng mẫu
- **Kỳ vọng Y2:** Từ chối; không thêm/sửa dòng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#orderLinesRejectInvalidQuantityAndUnavailableProduct` → **Pass** (0.215 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-107 - Xóa một dòng còn lại ít nhất một

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_PENDING có 2 dòng
- **Bước thực hiện Y2:** POST /admin/order/item/delete đúng orderId/detailId
- **Kỳ vọng Y2:** Xóa đúng dòng; còn 1 dòng; tổng giảm đúng.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#removeLineRejectsOtherOrderThenRemovesOwnLine` → **Pass** (0.243 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-108 - Chặn xóa dòng cuối hoặc dòng đơn khác

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** O_PENDING có 1 dòng; lượt khác có 2 dòng và detailId thuộc O2
- **Bước thực hiện Y2:** POST item/delete từng lượt độc lập
- **Kỳ vọng Y2:** Từ chối; không xóa dòng cuối; không xóa dòng của đơn khác.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.AdminIntegrationTests#removeLineRejectsOtherOrderThenRemovesOwnLine` → **Pass** (0.243 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.AdminIntegrationTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#cannotRemoveFinalLine` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-109 - Xóa đơn được phép

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** Đơn PENDING và CANCELLED riêng
- **Bước thực hiện Y2:** POST delete/{id} từng đơn
- **Kỳ vọng Y2:** Xóa cả header và details; không để dòng mồ côi.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#deletePendingAndCancelledRemovesDetails` → **Pass** (0.213 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-110 - Chặn xóa đơn đang xử lý/hoàn tất

- **Yêu cầu:** BR-09
- **Tiền điều kiện Y2:** A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt
- **Dữ liệu Y2:** Đơn CONFIRMED,SHIPPING,COMPLETED riêng
- **Bước thực hiện Y2:** POST delete/{id} từng trạng thái
- **Kỳ vọng Y2:** Báo chỉ xóa đơn chờ xác nhận/đã hủy; dữ liệu giữ nguyên.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.service.OrderServiceTests#cannotDeleteProcessingOrCompletedOrder(String)[1]` → **Pass** (0.003 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#cannotDeleteProcessingOrCompletedOrder(String)[2]` → **Pass** (0.001 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Script:** `org.example.asm_java5.service.OrderServiceTests#cannotDeleteProcessingOrCompletedOrder(String)[3]` → **Pass** (0.0 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.service.OrderServiceTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-111 - Doanh thu và đủ 5 chỉ số

- **Yêu cầu:** BR-10
- **Tiền điều kiện Y2:** A1 login; DB báo cáo riêng chỉ có fixture R
- **Dữ liệu Y2:** C1: hai dòng COMPLETED, (price=90000,quantity=2),(price=200000,quantity=1)
- **Bước thực hiện Y2:** Mở /admin/report/revenue; đối chiếu phép tính
- **Kỳ vọng Y2:** C1 revenue=380000; quantity=3; highest=200000; lowest=90000; average=126666.666... trước định dạng. Trung bình có trọng số theo code; đối chiếu làm tròn UI.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#revenueAggregatesOnlyCompletedByCategory` → **Pass** (0.21 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-112 - Không cộng đơn chưa hoàn tất

- **Yêu cầu:** BR-10
- **Tiền điều kiện Y2:** A1 login; Fixture R thêm dòng C1 PENDING 999999x9 và CANCELLED 888888x8
- **Dữ liệu Y2:** Các dòng ngoài COMPLETED
- **Bước thực hiện Y2:** Mở revenue
- **Kỳ vọng Y2:** Chỉ số C1 giữ như fixture R; không cộng đơn PENDING/CANCELLED.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#revenueAggregatesOnlyCompletedByCategory` → **Pass** (0.21 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-113 - Tách loại và sắp xếp doanh thu

- **Yêu cầu:** BR-10
- **Tiền điều kiện Y2:** A1 login; Fixture R; C2 có COMPLETED price=500000 quantity=1
- **Dữ liệu Y2:** C1 revenue380000; C2 revenue500000
- **Bước thực hiện Y2:** Mở revenue
- **Kỳ vọng Y2:** Mỗi loại một dòng; C2 trước C1; các chỉ số không trộn hai loại.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#revenueAggregatesOnlyCompletedByCategory` → **Pass** (0.21 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-114 - Top 10 và thứ tự VIP

- **Yêu cầu:** BR-11
- **Tiền điều kiện Y2:** A1 login; DB riêng có 11 user có đơn COMPLETED
- **Dữ liệu Y2:** U01..U11 mua lần lượt 10000..110000
- **Bước thực hiện Y2:** Mở /admin/report/vip
- **Kỳ vọng Y2:** Đúng 10 user tổng cao nhất U11..U02 giảm dần; loại U01; không có user chỉ có đơn PENDING.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#vipReturnsExactlyTopTen` → **Pass** (0.861 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-115 - VIP tổng tiền và ngày đầu/cuối

- **Yêu cầu:** BR-11
- **Tiền điều kiện Y2:** A1 login; U1 có 2 đơn COMPLETED ngày 01/10/2026 và 20/10/2026
- **Dữ liệu Y2:** Đơn đầu có nhiều dòng tổng180000; đơn sau200000; PENDING ngày21/10 giá999999
- **Bước thực hiện Y2:** Mở vip; đối chiếu DB
- **Kỳ vọng Y2:** U1 total380000; firstDate=01/10; lastDate=20/10; không nhân tổng do join và không lấy ngày PENDING.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#vipTotalAndDatesIgnorePending` → **Pass** (0.214 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-116 - VIP đồng tổng và ít hơn 10 người

- **Yêu cầu:** BR-11
- **Tiền điều kiện Y2:** A1 login; DB riêng có 3 user; U1,U2 cùng tổng
- **Dữ liệu Y2:** U1 và U2 cùng 100000; U3=50000
- **Bước thực hiện Y2:** Mở vip
- **Kỳ vọng Y2:** Có 3 dòng; U1 trước U2 theo username khi đồng tổng; không bổ sung user chưa mua.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#vipTieBreaksByUsernameAndDoesNotPadRows` → **Pass** (0.228 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-117 - Báo cáo không có dữ liệu

- **Yêu cầu:** BR-10,BR-11
- **Tiền điều kiện Y2:** A1 login; DB test không có đơn COMPLETED
- **Dữ liệu Y2:** Chỉ PENDING hoặc không có đơn
- **Bước thực hiện Y2:** Mở revenue và vip
- **Kỳ vọng Y2:** Bảng rỗng rõ ràng; không lỗi chia 0/500 hoặc hiển thị doanh thu giả.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.integration.BusinessIntegrationTests#reportsEmptyWithoutCompletedOrders` → **Pass** (0.232 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.integration.BusinessIntegrationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-118 - Dữ liệu mẫu theo đề

- **Yêu cầu:** BR-12
- **Tiền điều kiện Y2:** Có bản DB test dùng để nộp
- **Dữ liệu Y2:** 10 loại,200 sản phẩm,5 user thường,1 admin
- **Bước thực hiện Y2:** Đếm bản ghi categories/products/accounts và vai trò; kiểm tra FK
- **Kỳ vọng Y2:** Có ít nhất lượng dữ liệu mẫu theo hướng dẫn; 5 user thường và 1 admin được phân biệt; các FK hợp lệ.
- **Kết quả phạm vi tự động:** Not Run
- **Thực tế:** Not Run: không kết nối SQL Server thật để đếm dữ liệu mẫu; H2 chỉ chứa fixture kiểm thử.
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-119 - Cài mới từ script bàn giao

- **Yêu cầu:** BR-12
- **Tiền điều kiện Y2:** SQL Server test riêng; bản sao source
- **Dữ liệu Y2:** Database/database.sql và cấu hình môi trường
- **Bước thực hiện Y2:** Tạo DB SQL Server test mới bằng Database/database.sql; cấu hình và chạy ứng dụng
- **Kỳ vọng Y2:** Script tái tạo schema/data; Hibernate validate thành công; ứng dụng khởi động. File database.sql hiện có; chưa thực thi cài mới trong đợt này.
- **Kết quả phạm vi tự động:** Not Run
- **Thực tế:** Not Run: script Database/database.sql hiện có; chưa thực thi cài mới trên SQL Server. H2 tự sinh schema không chứng minh script SQL Server chạy được.
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-120 - Luồng mua hàng xuyên suốt

- **Yêu cầu:** BR-01,BR-02,BR-04
- **Tiền điều kiện Y2:** DB/SMTP test; U1 active; A1 admin
- **Dữ liệu Y2:** P1 đang bán; địa chỉ và phone hợp lệ
- **Bước thực hiện Y2:** Khách xem hàng và thêm giỏ; vào checkout; login U1; đặt; A1 đổi COMPLETED; U1 mở My Products; A1 mở báo cáo
- **Kỳ vọng Y2:** Đơn đúng chủ, đúng giá; lịch sử/My Products/báo cáo cùng phản ánh đơn hoàn tất; không nhân bản.
- **Kết quả phạm vi tự động:** Pass
- **Script:** `org.example.asm_java5.ui.StoreSeleniumTests#purchaseThroughUiAppearsInHistoryAndReports` → **Pass** (4.145 giây). [XML](evidence/selenium-reports/TEST-org.example.asm_java5.ui.StoreSeleniumTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-121 - Hiển thị và form trên thiết bị

- **Yêu cầu:** BR-01
- **Tiền điều kiện Y2:** App chạy
- **Dữ liệu Y2:** Viewport 1366x768 và 390x844; tên tiếng Việt
- **Bước thực hiện Y2:** Mở trang chủ, chi tiết, giỏ, checkout và admin; thao tác nút/form
- **Kỳ vọng Y2:** Chữ Việt đúng; nội dung quan trọng đọc được; nút không che nhau; form dùng được. Đây là kiểm thử chất lượng bổ sung.
- **Kết quả phạm vi tự động:** Not Run
- **Thực tế:** Not Run: chưa kiểm tra thủ công toàn bộ giao diện ở cả desktop và mobile; Selenium hiện dùng 1440x1000.
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.

### TC-122 - Smoke build source hiện tại

- **Yêu cầu:** BR-12
- **Tiền điều kiện Y2:** JDK17+ và Maven Wrapper khả dụng
- **Dữ liệu Y2:** ./mvnw.cmd -q -DskipTests compile
- **Bước thực hiện Y2:** Chạy Maven compile/test với profile test
- **Kỳ vọng Y2:** Source compile thành công; Spring test context chạy với DB kiểm thử, không dùng SQL Server thật.
- **Kết quả phạm vi tự động:** Pass
- **Phạm vi/ghi chú:** Compile đã được Maven thực thi trước test; contextLoads kiểm tra profile test kết nối H2. Lỗi build imageFile của snapshot cũ không còn là hiện trạng.
- **Script:** `org.example.asm_java5.AsmJava5ApplicationTests#contextLoads` → **Pass** (0.578 giây). [XML](evidence/surefire-reports/TEST-org.example.asm_java5.AsmJava5ApplicationTests.xml).
- **Người thực thi:** Codex tự động; sinh viên chưa xác nhận review. Thời điểm từng suite có trong XML.
