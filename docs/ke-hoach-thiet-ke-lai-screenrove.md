# Kế hoạch thiết kế lại Mộc Store từ khảo sát Screenrove

Ngày lập: 21/10/2026. Phạm vi: khảo sát và lập kế hoạch, chưa sửa giao diện ứng dụng.

## 1. Cơ sở khảo sát

Đã dùng Screenrove list_products, search_screens, get_product, get_design, get_screen_image và compare_screens; trực tiếp xem bốn ảnh: Airbnb home, Airbnb listing detail, Stripe full-page demo, Linear workspace issues. Đối chiếu với templates, CSS, JavaScript, Product entity và các controller hiện có trong dự án. Chưa kiểm tra giao diện Mộc Store trên trình duyệt trong lượt này; các nhận xét hiện trạng dưới đây dựa trên mã nguồn.

Screenrove là thư viện ảnh chụp theo thời điểm, không phải kết quả kiểm tra trực tiếp các website hôm nay. Thư viện hiện được liệt kê không có thương hiệu nội thất chuyên biệt; các mẫu dưới đây được chọn theo bài toán giao diện tương đồng.

| Nguồn | Bằng chứng quan sát | Áp dụng cho Mộc Store |
| --- | --- | --- |
| [Airbnb](https://screenrove.com/airbnb), chụp 21/09/2026 | Ảnh chiếm phần lớn thẻ; tên và giá đặt sát ảnh; tìm kiếm nổi bật. Trang chi tiết có ảnh lớn và cụm đặt chỗ riêng. | Ảnh sản phẩm rõ, thẻ ít chữ, thanh tìm kiếm dễ thấy; chi tiết chia vùng ảnh và thông tin mua. |
| [Stripe](https://screenrove.com/stripe), chụp 24/09/2026 | Trong bản demo công khai, phần preview phân tách dòng hàng, tạm tính, khuyến mãi, thuế và tổng phải trả. | Tóm tắt đơn có thứ bậc rõ; tổng tiền dễ đọc, một hành động đặt hàng chính. |
| [Linear](https://screenrove.com/linear), chụp 20/09/2026 cho luồng workspace | Sidebar ổn định, thanh công cụ gọn, nội dung dạng hàng với ít trang trí. | Quản trị có sidebar, bảng rõ ràng, trạng thái đang chọn và thao tác nhất quán. |

Ảnh đã khảo sát: [Airbnb home](https://screenrove.com/captures/airbnb-product-home.jpg), [Airbnb detail](https://screenrove.com/captures/airbnb-product-listing.jpg), [Stripe demo](https://screenrove.com/captures/stripe-full-page-demo.jpg), [Linear issues](https://screenrove.com/captures/linear-inbox.jpg).

Các đề xuất là diễn giải dành riêng cho Mộc Store. Không sao chép tìm kiếm ngày/khách, bản đồ Airbnb, sidebar cấu hình của Stripe hoặc quy trình công việc của Linear. Khảo sát Stripe không bao gồm Dashboard đăng nhập hay giao dịch thanh toán hoàn tất. Ảnh detail Airbnb chỉ bao phủ viewport đầu. get_design của Stripe chủ yếu mô tả docs và công cụ demo, không dùng như bộ thông số đo riêng của form checkout.

## 2. Hiện trạng và ưu tiên

| Mức | Hiện trạng trong mã nguồn | Hướng xử lý |
| --- | --- | --- |
| P0 | Trang chủ dùng các liên kết `?category=nature/light/wall`; controller danh mục dùng `/product/list-by-category/{id}`. JavaScript cũ chờ `data-filter` trong khi template hiện tại dùng liên kết Thymeleaf. | Đồng bộ nguồn danh mục và URL; hiển thị danh mục đang chọn theo dữ liệu server. Không hardcode ID khi chưa xác minh dữ liệu. |
| P0 | Thẻ gợi ý trang chủ là dữ liệu cố định; không có liên kết chi tiết ở tên/ảnh. | Lấy sản phẩm từ model hoặc cấu hình nội dung có liên kết hợp lệ, làm tên và ảnh truy cập được bằng bàn phím. |
| P0 | Chi tiết chỉ có ảnh và các thuộc tính cơ bản, thiếu khu vực mua. | Tổ chức lại vùng ảnh, tên, giá, giảm giá, trạng thái. Phần số lượng/thêm giỏ cần xử lý Java trước khi hoạt động. |
| P0 | Cart là bảng trống cố định; checkout có hai trường và nút disabled. | Thiết kế trạng thái rỗng tốt trước; thiết kế đầy đủ giỏ/checkout đi cùng dữ liệu và submit thực. |
| P1 | Danh sách có danh mục, khoảng giá, số sản phẩm; chưa có tìm kiếm/sắp xếp. Hai bộ lọc hiện đi qua endpoint riêng. | Giữ chức năng hiện có; giữ giá trị lọc sau submit, báo lỗi rõ, bổ sung trạng thái không có kết quả. Kết hợp nhiều điều kiện cần sửa controller/service. |
| P1 | Giá ở danh sách/chi tiết nối trực tiếp số với ký hiệu tiền; quản trị đã có định dạng riêng. | Dùng cùng định dạng tiền Việt ở tất cả trang; thống nhất quy tắc giá giảm trước khi hiển thị. |
| P2 | Quản trị đã có sidebar và form/bảng CRUD sản phẩm. | Tối ưu thứ bậc, độ rộng, trạng thái, thông báo và xử lý màn hình nhỏ. |

Giữ nguyên Spring MVC + Thymeleaf, tên view, model binding, URL và form action đang hoạt động. Dự án đang có thay đổi chưa commit; khi triển khai cần giữ nguyên công việc hiện có.

## 3. Hướng mỹ thuật đề xuất: Mộc — ấm, thoáng, lấy sản phẩm làm trung tâm

- Giữ nền kem `#FAF8F3`, bề mặt sáng `#FFFDF8`, xanh olive `#334C37`, chữ `#263C30`; đất nung `#9B593D` dùng tiết chế. Đây là token hiện có được đề xuất giữ lại, chưa phải xác nhận mọi tổ hợp màu đạt tương phản.
- Tiêu đề serif có dấu tiếng Việt tốt; nội dung Segoe UI/system sans, 16px. Tiêu đề trang mua sắm khoảng 32–40px; hero có thể 48–64px desktop, 32–40px mobile.
- Nội dung rộng tối đa khoảng 1200–1280px; lề 20px mobile, 32–48px desktop; khoảng cách theo 4/8px. Góc bo 12–16px cho ảnh, 8px cho điều khiển.
- Ảnh sản phẩm cùng tỉ lệ 1:1 hoặc 4:5 và ánh sáng; dùng ảnh thật đúng sản phẩm khi có. Hero dùng ảnh không gian sống có quyền sử dụng; SVG hiện tại làm phương án tạm hoặc hình trạng thái rỗng.
- Thẻ sản phẩm không cần khung đậm: ảnh → danh mục nhỏ → tên tối đa hai dòng → giá. Nhãn giảm giá chỉ xuất hiện khi có dữ liệu hợp lệ; không thêm đánh giá hay lượng bán giả.
- SVG icon đồng bộ; chuyển động nhẹ khoảng 150–250ms cho hover/focus. Nội dung luôn đọc được khi tắt hiệu ứng.

Đã chạy gợi ý design-system của ui-ux-pro-max. Công cụ trả về cả scroll storytelling và phong cách Swiss; chỉ chọn các nguyên tắc khoảng trắng, chữ rõ, ảnh nhanh, tương tác nhẹ. Bố cục mua sắm và bản sắc hiện tại là cơ sở quyết định, không áp dụng hiệu ứng cuộn chỉ vì công cụ gợi ý.

## 4. Bố cục từng nhóm trang

### Trang chủ

Thứ tự đề xuất: header gọn → hero ảnh không gian + một CTA “Khám phá sản phẩm” → nhóm danh mục → sản phẩm nổi bật → câu chuyện ngắn của Mộc → footer.

Hero khoảng 40% chữ / 60% ảnh trên desktop; mobile xếp chữ trên ảnh. Rút gọn các dải khẩu hiệu lặp lại để khách sớm nhìn thấy hàng. Header giữ logo, Sản phẩm, Tài khoản, Giỏ hàng; đơn hàng đặt thuận tiện trong khu vực tài khoản. Khi có backend tìm kiếm, thêm ô tìm theo tên; trước đó không trình bày ô tìm như một chức năng đã hoạt động.

### Danh sách sản phẩm

Tiêu đề gọn → số kết quả → bộ lọc danh mục và khoảng giá → các điều kiện đang áp dụng → lưới sản phẩm → phân trang khi backend hỗ trợ.

Desktop bốn cột, tablet ba hoặc hai cột, mobile hai cột nếu tên/giá đủ chỗ; xuống một cột khi không bảo đảm đọc rõ. Bộ lọc mobile đặt trong phần thu gọn có nút mở/đóng rõ; ưu tiên cấu trúc đơn giản trước modal. Không có kết quả: thông báo nguyên nhân theo điều kiện đã chọn và nút “Bỏ lọc”.

### Chi tiết sản phẩm

Breadcrumb → hai cột ảnh/thông tin → sản phẩm cùng loại. Thông tin gồm tên, danh mục, giá, giảm giá hợp lệ và trạng thái đang bán. Trên mobile xếp ảnh trước thông tin.

Product hiện chỉ có một trường image, chưa có mô tả, kích thước/chất liệu hoặc danh sách ảnh. Bản đầu dùng một ảnh tốt; gallery, thông số và nội dung mô tả là hạng mục cần dữ liệu bổ sung. `available` thể hiện đang bán/ngừng bán, không suy diễn thành số tồn kho. Khu vực mua có số lượng và “Thêm vào giỏ” sau khi có nghiệp vụ giỏ hàng; thông báo thành công/lỗi gắn với kết quả server.

### Giỏ hàng và đặt hàng

Giỏ trống: hình nhỏ, câu giải thích và CTA mua sắm. Giỏ có hàng: ảnh/tên/giá/số lượng/xóa bên trái, tóm tắt tiền bên phải; mobile chuyển từng hàng thành thẻ.

Checkout: thông tin người nhận bên trái, tóm tắt đơn bên phải; mobile một cột. Tách tạm tính, giảm giá nếu có, phí vận chuyển và tổng. Chỉ hiển thị phương thức thanh toán được hỗ trợ thực tế. Không tự đặt phí hay chính sách giao hàng; lỗi đặt cạnh trường, giữ dữ liệu khi gửi lỗi, chống gửi lặp và có màn hình xác nhận từ kết quả đơn hàng thật.

### Tài khoản, đơn hàng, quản trị

Form tài khoản có độ rộng vừa đọc, label rõ, một hành động chính và thông báo cụ thể. Đơn hàng có trạng thái bằng chữ, tổng tiền, liên kết chi tiết và trạng thái rỗng.

Quản trị giữ sidebar hiện tại, bổ sung trạng thái active chính xác theo nhóm URL; tiêu đề + thao tác chính → thông báo → form/bảng. Bảng dùng ảnh nhỏ, giá căn phải, trạng thái bằng nhãn chữ; xử lý bảng rộng bằng vùng cuộn riêng. Giữ form trên trang trong giai đoạn đầu để tận dụng binding và lỗi hiện có.

## 5. Trình tự triển khai và đầu ra

| Đợt | Công việc | File chính | Điều kiện hoàn thành |
| --- | --- | --- | --- |
| 1 | Chốt token, chuẩn bị ảnh, dựng khung desktop/mobile trang chủ và danh sách; xử lý liên kết danh mục lệch | `static/css/style.css`, `templates/shared/layout.html`, `home.html`, `product-list.html`, `static/js/store.js`, ProductController khi cần model trạng thái | Luồng trang chủ → danh mục → chi tiết dùng đúng sản phẩm/danh mục; bộ lọc hiện có không mất chức năng. |
| 2 | Làm chi tiết sản phẩm, giá tiền, trạng thái rỗng/thiếu ảnh; nối dữ liệu trang chủ | `product-detail.html`, `home.html`, controller tương ứng | Giá hiển thị thống nhất; sản phẩm không tồn tại và thiếu ảnh có cách xử lý rõ; không giả lập thông số chưa có. |
| 3 | Giỏ hàng, checkout, xác nhận và đơn hàng | `cart.html`, `check-out.html`, các template đơn hàng, Cart/Order controller và service | Cần nghiệp vụ backend: thêm/sửa/xóa giỏ, tính tiền phía server, validate và lưu đơn. Hoàn tất kiểm thử chức năng trước khi bật nút đặt hàng. |
| 4 | Đồng bộ tài khoản và quản trị | `shared/admin-layout.html`, `admin/*.html`, template tài khoản; tách CSS quản trị nếu cần | Form CRUD cũ còn hoạt động, lỗi/thành công rõ, bảng dùng được trên màn nhỏ. |
| 5 | Kiểm tra trình duyệt và hoàn thiện ảnh/nội dung | Các trang đã sửa | Đạt checklist dưới đây và lưu ảnh đối chiếu trước/sau. |

Ưu tiên thực hiện đợt 1–2 trước để có phần mua sắm xem được và đánh giá được. Đợt 3 có phạm vi nghiệp vụ lớn hơn việc đổi CSS; không coi giao diện đẹp là bằng chứng mua hàng đã hoạt động.

## 6. Tiêu chí nghiệm thu khi triển khai

- Kiểm tra 375, 768, 1024, 1440px: không tràn ngang toàn trang, nút/giá/tên không che nhau, header không che nội dung hoặc focus.
- Menu dùng được bằng bàn phím, Escape đóng khi mở; điều khiển có tên, focus rõ; chữ thường đạt tương phản ít nhất 4.5:1. Mục tiêu vùng bấm 44px cho thao tác chính trên mobile.
- Danh mục active và giá lọc còn đúng sau tải lại; thử không có kết quả, khoảng giá sai, danh mục rỗng, ảnh thiếu và sản phẩm ngừng bán.
- Xác minh liên kết trang chủ thực sự mở đúng nhóm và chi tiết; không giữ script lọc cũ không còn khớp DOM.
- Ảnh có kích thước cố định, alt phù hợp; lazy-load phần dưới màn hình, ưu tiên ảnh hero; tôn trọng reduced-motion.
- Kiểm tra CRUD quản trị sau thay đổi template. Chạy Maven test và các kiểm tra hành trình phù hợp với phần được triển khai; không dùng HTTP 200 làm bằng chứng nghiệp vụ đúng.
- Khi triển khai giỏ/checkout: tính lại giá phía server, xử lý hàng ngừng bán, dữ liệu người nhận sai, lỗi lưu đơn và gửi đơn lặp. Không hiển thị thành công khi server thất bại.

## 7. Các quyết định có thể chốt trong lúc làm

Mặc định giữ nhận diện kem/olive và stack hiện tại. Ảnh thật, tìm kiếm/sắp xếp, bộ lọc kết hợp, gallery và nghiệp vụ giỏ hàng được triển khai theo dữ liệu/chức năng đã sẵn sàng. Với thông tin giao hàng, chất liệu hoặc chính sách chưa được cung cấp, cần nội dung thật trước khi đưa lên trang khách hàng.
