from pathlib import Path
from collections import Counter
import html

OUT = Path(__file__).resolve().parent / 'testing'
OUT.mkdir(exist_ok=True)
cases = []
def add(group, req, source, title, pre, data, steps, expected, priority='P1'):
    cases.append(dict(id=f'TC-{len(cases)+1:03}', group=group, req=req, source=source, title=title, pre=pre, data=data, steps=steps, expected=expected, priority=priority))

# Each data variation is an independent execution, with reset to the stated precondition.
g='Trưng bày hàng hóa'; r='BR-01'; s='Đề tr.2; HomeController, ProductController, ProductRepository'
for title,data,steps,expected in [
('Trang chủ và hàng mới','Có ít nhất 6 sản phẩm đang bán với ngày tạo khác nhau','Mở / và /home/index; đối chiếu mục Hàng mới với CSDL','Hai URL hiển thị trang chủ; tối đa 4 hàng đang bán mới nhất, ngày giảm dần rồi id giảm dần.'),
('Hàng giảm giá','6 hàng đang bán giảm 0, 5, 10, 15, 20, 30%; 1 hàng ngừng bán giảm 90%','Mở /home/index; xem mục giảm giá','Tối đa 4 hàng đang bán có discount > 0 theo mức giảm giảm dần; không có hàng giảm 0% hoặc ngừng bán.'),
('Hàng bán chạy','5 hàng có lượng bán COMPLETED lần lượt 1,2,3,4,5; hàng khác có 100 món PENDING','Mở trang chủ; đối chiếu các dòng đơn','Top 4 theo tổng quantity của COMPLETED giảm dần; không tính 100 món PENDING; chỉ hàng đang bán.'),
('Tìm theo loại','C1 có P1,P2; C2 có P3','Mở /product/list-by-category/{C1}','Chỉ có sản phẩm thuộc C1; tên, ảnh, giá đúng CSDL.'),
('Lọc giá gồm hai biên','P1 giá 100000; P2 giá 200000; P3 giá 300000','GET /product/list-by-price?minPrice=100000&maxPrice=200000','Có P1 và P2; không có P3. Code lọc giá gốc price, không phải giá sau giảm.'),
('Giá hai biên bằng nhau','minPrice=200000; maxPrice=200000','Gửi bộ lọc giá','Chỉ trả sản phẩm giá gốc đúng 200000.'),
('Khoảng giá sai','Lần 1: minPrice=-1,maxPrice=10; lần 2: minPrice=20,maxPrice=10','Gửi từng bộ lọc riêng','Hiển thị Khoảng giá không hợp lệ và danh sách theo nhánh lỗi; không lỗi 500.'),
('Lọc không có kết quả','Loại rỗng; hoặc khoảng giá không chứa sản phẩm','Mở từng bộ lọc','Danh sách rỗng có thể hiểu được; không hiển thị kết quả cũ gây hiểu nhầm.'),
('Chi tiết và hàng cùng loại','P1,P2 thuộc C1; P3 thuộc C2','Mở /product/detail/{P1}; đối chiếu phần liên quan','Chi tiết đúng P1; liên quan có P2, không có P1 hoặc P3.'),
('Sản phẩm không tồn tại','id=999999 không có trong DB','Mở /product/detail/999999','Chuyển về /product/list; không lỗi 500.'),
('Sai kiểu hoặc thiếu tham số giá','minPrice=abc,maxPrice=10; sau đó bỏ maxPrice','Gửi từng GET riêng','HTTP 400 và trang lỗi dữ liệu; không truy vấn thành công với giá bị diễn giải sai.')]:
    add(g,r,s,title,'DB test đã chuẩn bị; không cần đăng nhập',data,steps,expected)

g='Giỏ hàng';r='BR-02';s='Đề tr.2-3; CartController, CartService, CartItem'
for title,pre,data,steps,expected in [
('Khách thêm hàng','Khách; giỏ rỗng','P1 đang bán','Mở chi tiết P1; nhấn thêm; mở /cart/index','Có 1 dòng P1 số lượng 1; không yêu cầu đăng nhập.'),
('Thêm lặp cùng sản phẩm','Giỏ có P1 quantity=1','P1','Thêm P1 lần nữa; mở giỏ','Một dòng P1 quantity=2; không nhân đôi dòng.'),
('Hiển thị và tính tiền','Giỏ rỗng','P1 giá 100000 giảm 10%, quantity=2; P2 giá 200000 giảm 0%, quantity=1','Thêm P1,P2; cập nhật số lượng; mở giỏ','Đơn giá P1=90000.00; thành tiền=180000.00; P2=200000.00; tổng=380000.00.'),
('Cập nhật số lượng tại biên','Giỏ có P1','quantity=1; sau đó 99','POST /cart/update với productId=P1 và từng quantity','Lưu đúng 1 và 99; tổng tiền cập nhật tương ứng.'),
('Số lượng ngoài miền','Giỏ P1 quantity=2','quantity=0,-1,100 (3 lượt độc lập)','POST /cart/update với từng giá trị','Báo số lượng phải từ 1 đến 99; giỏ vẫn quantity=2.'),
('Thêm quá giới hạn','Giỏ P1 quantity=99','P1','Thêm P1 lần nữa','Báo tối đa 99; số lượng không tăng.'),
('Thêm hàng không tồn tại hoặc ngừng bán','Giỏ rỗng','ID không có; P_OFF available=false','POST /cart/add/{id} với từng ID','Báo không tìm thấy hoặc đã ngừng bán; giỏ không đổi.'),
('Cập nhật hàng không trong giỏ','Giỏ chỉ có P1','productId=P2; quantity=2','POST /cart/update','Báo sản phẩm không có trong giỏ; không tự thêm P2.'),
('Xóa một dòng','Giỏ có P1 và P2','P1','Nhấn xóa P1','Chỉ P1 bị xóa; P2 còn nguyên; tổng tính lại.'),
('Xóa toàn bộ và xóa giỏ rỗng','Giỏ có P1,P2','Không','POST /cart/clear; lặp lại khi đã rỗng','Giỏ rỗng, tổng 0; thao tác lặp không gây lỗi.'),
('Cách ly phiên','Hai trình duyệt độc lập A,B','A thêm P1; B thêm P2','Mở giỏ trên cả hai phiên','A chỉ có P1; B chỉ có P2.'),
('Làm tròn tiền','P có price=10.05,discount=10; giỏ rỗng','quantity=3','Thêm P; cập nhật số lượng=3','Đơn giá 9.05 do HALF_UP 2 chữ số; tổng 27.15.')]:
    add(g,r,s,title,pre,data,steps,expected)

g='Tài khoản';r='BR-03';s='Đề tr.2,4; AccountController, AccountService, AuthService'
for title,pre,data,steps,expected in [
('Đăng ký hợp lệ','Username/email chưa dùng; SMTP test hoạt động','username=qa_new; fullName=Nguyễn Kiểm Thử; email=hộp thư test thực; password=Test@1234','GET /account/sign-up; nhập dữ liệu; gửi; xem DB và thư','Tạo 1 account; admin=false,activated=false; mật khẩu BCrypt, không phải bản rõ; có link kích hoạt hạn 24 giờ.'),
('Đăng ký thiếu dữ liệu','Chưa có qa_new','Lần lượt bỏ trống username/fullName/email/password; thử toàn khoảng trắng','Gửi từng lượt đăng ký độc lập','Báo dữ liệu bắt buộc; không tạo tài khoản.'),
('Trùng username hoặc email','Đã có U1/email U1','Username U1 với email mới; username mới với email U1','Đăng ký từng lượt','Từ chối trùng; không tạo thêm bản ghi.'),
('Email sai định dạng','Username mới','abc; a@b; a b@c.com','Đăng ký từng email','Báo email không hợp lệ; DB không có tài khoản mới.'),
('Biên độ dài hồ sơ','Dữ liệu còn lại hợp lệ','username 50/51; fullName 100/101; email hợp lệ 255/256 ký tự','Thử riêng từng trường và từng độ dài','50/100/255 được chấp nhận; 51/101/256 bị từ chối; không cắt âm thầm.'),
('Biên mật khẩu','Username/email mới mỗi lượt','7 và 8 ký tự ASCII; 72 và 73 byte UTF-8; mẫu Unicode 25 chữ á=50 byte và 37 chữ á=74 byte','Đăng ký mỗi dữ liệu độc lập','7 ký tự hoặc >72 byte bị từ chối; 8 ký tự và 72 byte hợp lệ nếu không trắng; đo byte Unicode đúng.'),
('Kích hoạt hợp lệ','Account chưa kích hoạt; token còn hạn','Token từ thư','GET /account/activate?token=...; xem DB; đăng nhập','activated=true; token và hạn được xóa; chuyển /auth/login; đăng nhập được.'),
('Kích hoạt sai/hết hạn/tái sử dụng','Account chưa kích hoạt riêng cho mỗi lượt','Token giả; expiry=now-1 phút; token đã sử dụng','Mở từng link riêng','Báo mã không hợp lệ hoặc hết hạn; không kích hoạt sai tài khoản.'),
('Gửi lại kích hoạt','Account chưa kích hoạt có token; SMTP test','Email account đó','POST /account/resend-activation; mở token cũ rồi token mới','Token mới hạn 24 giờ; token cũ vô hiệu; token mới kích hoạt được.'),
('SMTP lỗi khi đăng ký','SMTP test cấu hình thất bại','Tài khoản mới hợp lệ','Gửi đăng ký; đối chiếu DB','Thông báo đã tạo account nhưng chưa gửi thư; account còn chưa kích hoạt; không báo thành công gửi thư.'),
('Cập nhật hồ sơ','U1 đăng nhập','fullName=Nguyễn An; email giữ nguyên','GET /account/edit-profile; đổi tên; lưu; tải lại','Lưu đúng tên; giữ activated và username; không đổi quyền admin.'),
('Đổi email cần kích hoạt','U1 đăng nhập; email mới chưa dùng','Email mới nhận được thư','Đổi email tại hồ sơ; lưu; truy cập trang bảo vệ; kích hoạt email mới','Lưu email mới,activated=false; gửi link; phiên không còn dùng chức năng bảo vệ; kích hoạt rồi đăng nhập lại được.'),
('Hồ sơ không hợp lệ','U1 đăng nhập','Tên trắng; email sai; email của U2','Lưu từng lượt; truy vấn DB sau mỗi lượt','Báo lỗi; dữ liệu không hợp lệ không được lưu.'),
('Đổi mật khẩu thành công','U1 đăng nhập với Test@1234','oldPassword=Test@1234; password=New@12345; confirmPassword=New@12345','POST /account/change-password; thử đăng nhập bằng cũ rồi mới','Đăng xuất; mật khẩu cũ thất bại, mới thành công; DB lưu hash mới.'),
('Đổi mật khẩu thất bại','U1 đăng nhập','Sai oldPassword; xác nhận không khớp; password 7 ký tự (3 lượt)','Gửi từng lượt; thử lại mật khẩu hiện tại','Báo lỗi tương ứng; hash cũ không đổi; không khóa tài khoản.'),
('Quên mật khẩu','U1 activated=true; SMTP test','Email U1','POST /account/forgot-password; xem thư và DB','Có token đặt lại hạn 30 phút; phản hồi chung không tiết lộ tài khoản.'),
('Email lạ/chưa kích hoạt','Có account chưa kích hoạt','Email không tồn tại; email account chưa kích hoạt','Gửi quên mật khẩu từng lượt','Phản hồi chung giống trường hợp hợp lệ; không gửi link/reset token cho hai trường hợp này.'),
('Đặt lại mật khẩu thành công','U1 có reset token còn hạn','password=Reset@123; confirmPassword=Reset@123','Mở /account/reset-password?token=...; gửi mật khẩu mới; đăng nhập','Hash đổi; reset token/hạn được xóa; mật khẩu mới đăng nhập được.'),
('Đặt lại token sai/hết hạn/dùng lại','Các account test độc lập','Token giả; expiry quá hạn; token đã reset thành công','Gửi reset với từng token','Báo link không hợp lệ hoặc hết hạn; mật khẩu không thay đổi.'),
('Reset xác nhận sai hoặc mật khẩu ngắn','Token còn hạn','password=Reset@123,confirmPassword=khac; sau đó password=1234567','Gửi từng dữ liệu','Báo lỗi; không đổi mật khẩu hoặc tiêu thụ token hợp lệ.'),
('Ảnh hồ sơ hợp lệ','U1 đăng nhập','PNG 100x100 nhỏ hơn 2MB','POST multipart /account/photo với photo; tải lại hồ sơ','Lưu tên UUID.png trong uploads; DB photo trỏ đúng; ảnh hiển thị.'),
('Ảnh lỗi và quá giới hạn','U1 đăng nhập','File rỗng; văn bản đổi đuôi jpg; ảnh >2MB; ảnh chiều rộng 5001px','Upload mỗi mẫu độc lập','Từ chối, không đổi ảnh DB. File quá giới hạn có thể nhận HTTP 400 từ FormErrors; không lỗi 500.')]:
    add(g,r,s,title,pre,data,steps,expected)

g='Đăng nhập và phân quyền';r='BR-05';s='Đề tr.2-4; AuthController, AuthInterceptor, AuthService'
for title,pre,data,steps,expected in [
('Đăng nhập user/admin','U1 và A1 active; mật khẩu test đã biết','U1; A1','GET /auth/login; đăng nhập mỗi role trong phiên riêng','U1 về /; A1 về /admin/product/index khi không có returnUrl; session ID được đổi.'),
('Sai thông tin hoặc bị khóa','Có U1 active và U_LOCK inactive','Sai password; username lạ; U_LOCK đúng password','Đăng nhập từng lượt','Không xác thực; lỗi sai thông tin hoặc chưa kích hoạt/bị khóa; không vào chức năng bảo vệ.'),
('Khách vào chức năng bảo vệ','Phiên mới chưa login','/account/edit-profile; /account/change-password; /order/checkout; /order/list; /order/detail/{O1}; /order/my-product-list','GET từng URL trong phiên mới riêng','Mỗi URL chuyển /auth/login; không lộ nội dung riêng.'),
('Quay lại URL yêu cầu','Khách chưa login','/order/list; /account/edit-profile','Mở từng URL trong phiên riêng; đăng nhập U1','Quay đúng URL đã yêu cầu; không luôn về trang chủ.'),
('Khách truy cập quản trị','Chưa login','Các /admin/category/index,product/index,account/index,order/index,report/revenue,report/vip','GET từng URL','Chuyển đăng nhập; không đọc được dữ liệu quản trị.'),
('User thường không được quản trị','U1 login; csrf hợp lệ','Các GET quản trị và POST create/update/delete của cả 4 module','Gửi từng request; đối chiếu DB','HTTP 403; không có thay đổi DB; không chỉ ẩn nút trên giao diện.'),
('CSRF sai hoặc thiếu','U1/A1 login theo quyền; ghi lại DB trước test','Bỏ csrf hoặc dùng csrf=wrong','POST cart/update, account/edit-profile, order/checkout và admin/product/create từng lượt','HTTP 403; không thay đổi dữ liệu.'),
('Đăng xuất','U1 login, giỏ có P1','POST /auth/logout với csrf đúng','Đăng xuất; mở lại /order/list và /cart/index','Phiên cũ vô hiệu; đơn yêu cầu login; giỏ phiên mới rỗng.'),
('Đổi mật khẩu làm hết phiên khác','U1 login trong hai trình duyệt A,B','A đổi mật khẩu thành công','Tại B truy cập /order/list sau khi A đổi','B phải đăng nhập lại vì authStamp không khớp hash mới.'),
('Chống xem đơn người khác','U1 login; O2 thuộc U2','GET /order/detail/{O2}; GET id không tồn tại','Truy cập hai URL riêng','Đơn U2 trả 403; đơn không tồn tại trả 404; không lộ chi tiết.'),
('Không thay đổi dữ liệu bằng GET','A1 login; có bản ghi test','GET /cart/clear và /admin/product/delete/{P1}','Gửi GET; đối chiếu giỏ/DB','Không xóa; route chỉ nhận POST; thông thường HTTP 405.')]:
    add(g,r,s,title,pre,data,steps,expected,'P0')

g='Đặt hàng và lịch sử';r='BR-04';s='Đề tr.2,4; OrderController, OrderService'
for title,pre,data,steps,expected in [
('Checkout thành công','U1 login; giỏ P1x2,P2x1; key mới','P1=100000 giảm10%; P2=200000; address=12 Lê Lợi; phone=0901234567','GET /order/checkout; gửi form; xem DB và giỏ','Tạo đúng 1 orders thuộc U1,status=PENDING và 2 dòng; giá 90000/200000, tổng 380000; giỏ xóa sau thành công; chuyển chi tiết đơn.'),
('Checkout giỏ rỗng','U1 login; giỏ rỗng; key hợp lệ','Địa chỉ/điện thoại hợp lệ','Mở checkout; gửi','Báo Giỏ hàng trống; không tạo orders/order_details.'),
('Địa chỉ tại biên','U1 login; giỏ P1; mỗi lượt key hợp lệ','Địa chỉ 1 và 500 ký tự không trắng','Gửi mỗi lượt độc lập; khôi phục giỏ','Tạo đơn được; địa chỉ lưu đã trim.'),
('Địa chỉ sai','U1 login; giỏ P1','Rỗng; toàn khoảng trắng; 501 ký tự','Checkout mỗi giá trị riêng','Báo địa chỉ từ 1 đến 500 ký tự; DB không có đơn mới; giỏ giữ nguyên.'),
('Điện thoại hợp lệ theo code','U1 login; giỏ P1','0901234567; chuỗi 8 và 20 chữ số; +84 (90)-1234567','Gửi từng mẫu; khôi phục giỏ/key','Chấp nhận các mẫu thuộc regex [+0-9 ()-]{8,20}.'),
('Điện thoại sai','U1 login; giỏ P1','7 hoặc 21 chữ số; 090abc1234','Gửi từng mẫu','Từ chối; không tạo đơn; giỏ giữ nguyên.'),
('Giá thay đổi trước checkout','U1 có P1 giá cũ 100000 giảm10% trong giỏ','Admin đổi P1 thành 120000 giảm10%','Admin lưu giá mới; U1 mở và gửi checkout; xem chi tiết DB','OrderDetail dùng giá mới 108000.00 từ DB. Kiểm tra chênh lệch giá hiển thị trước xác nhận; cần thống nhất UX khi giá thay đổi.'),
('Rollback khi hàng ngừng bán','Giỏ P1,P2 đang bán; U1 login','Sau khi thêm, admin chuyển P2 available=false','Checkout; kiểm tra cả hai bảng và giỏ','Báo hàng không còn bán; không còn đơn/dòng dở dang; giỏ giữ nguyên.'),
('Chống gửi đơn hai lần','U1 login; giỏ P1; key K','Cùng csrf và checkoutKey=K','Gửi cùng request hai lần liên tiếp hoặc đồng thời trong một session','Chỉ một đơn mới; lần sau bị báo key đã dùng; không nhân đôi dòng.'),
('Checkout key giả','U1 login; giỏ P1; session có key K','checkoutKey=wrong','POST /order/checkout với csrf đúng','Báo yêu cầu đã dùng/tải lại giỏ; không tạo đơn; giỏ không mất.'),
('Danh sách và chi tiết đơn của mình','U1 có O1,O3; U2 có O2','GET /order/list; /order/detail/{O1}','Mở danh sách; mở O1; đối chiếu DB','Danh sách chỉ U1 theo id giảm dần; chi tiết đúng hàng, giá chốt, số lượng, địa chỉ, tổng.'),
('My Products','U1 có đơn COMPLETED và PENDING/CANCELLED; U2 có COMPLETED','GET /order/my-product-list','Mở trang; so các dòng order_details','Chỉ dòng thuộc đơn COMPLETED của U1; không có đơn chưa hoàn tất hoặc của U2.'),
('Giữ giá lịch sử','O1 đã đặt với P1 đơn giá 90000','Admin đổi giá/discount P1','Mở lại O1','Đơn vẫn dùng giá OrderDetail 90000; không bị giá danh mục hiện tại thay đổi.')]:
    add(g,r,s,title,pre,data,steps,expected,'P0' if title in ['Checkout thành công','Rollback khi hàng ngừng bán','Chống gửi đơn hai lần'] else 'P1')

g='Quản trị loại hàng';r='BR-06';s='Đề tr.2,4; CategoryAController, CategoryService'
for title,data,steps,expected in [
('Tạo và đọc loại','name=Loại QA mới chưa tồn tại','Mở /admin/category/index; POST create; mở edit/{id}','Tạo 1 loại; có ID; danh sách và form sửa hiển thị đúng.'),
('Tên loại trắng hoặc trùng','name=   ; sau đó tên C1 đã có','POST create từng lượt','Báo bắt buộc/trùng; không thêm bản ghi.'),
('Sửa loại hợp lệ','C1; tên mới chưa dùng','GET edit/{C1}; POST update với id/name','Cập nhật cùng ID; không tạo loại mới.'),
('Biên tên khi sửa','name 100 và 101 ký tự; tên loại khác đã dùng','POST update từng lượt độc lập','100 hợp lệ; 101 hoặc trùng loại khác bị từ chối; DB giữ bản trước khi lỗi.'),
('Biên tên khi tạo - kiểm tra khoảng trống validation','name 101 ký tự','POST create; đối chiếu DB và HTTP','Kỳ vọng kiểm thử bổ sung: từ chối thân thiện, không lưu quá 100, không lỗi 500. Code create chưa có kiểm tra length như update; cần xác minh runtime.'),
('Xóa loại không có hàng','C_EMPTY không có sản phẩm','POST /admin/category/delete/{C_EMPTY}','Xóa thành công; không còn trong DB/danh sách.'),
('Chặn xóa loại có hàng','C1 có P1','POST /admin/category/delete/{C1}','Báo không thể xóa loại đang có sản phẩm; C1/P1 còn nguyên.'),
('Sửa loại không tồn tại','id=999999','GET /admin/category/edit/999999','Chuyển /admin/category/index; không lỗi 500.')]:
    add(g,r,s,title,'A1 login; csrf hợp lệ; dữ liệu test riêng',data,steps,expected)

g='Quản trị hàng hóa';r='BR-07';s='Đề tr.2,4; ProductAController, ProductService, FileService'
for title,data,steps,expected in [
('Tạo/đọc sản phẩm','name=Bình QA; price=100000; discount=10; categoryId=C1; image=vase.svg; available=true','Mở index; POST create; mở edit/{id}','Tạo 1 P; ngày tạo hôm nay; tất cả trường đúng DB và form.'),
('Sửa sản phẩm','P1; name=Bình QA sửa; price=120000; discount=5; categoryId=C2','POST update với id=P1','Cùng ID cập nhật đúng; ngày tạo không bị thay mới; storefront phản ánh dữ liệu.'),
('Tên bắt buộc và độ dài','name trắng; 200 và 201 ký tự','Create/update từng lượt, các trường khác hợp lệ','Trắng/201 bị từ chối; 200 chấp nhận; không lưu lỗi.'),
('Biên giá','price=-0.01;0;99999999999999999.99;100000000000000000.00;1.001','POST create từng giá trị','0 và max hợp lệ; âm, vượt max hoặc quá 2 số thập phân bị từ chối.'),
('Biên giảm giá','discount=-0.01;0;100;100.01;1.001','POST create từng giá trị','0/100 chấp nhận; âm, >100 hoặc >2 chữ số thập phân bị từ chối.'),
('Thiếu loại hoặc loại giả','categoryId trống; categoryId=999999','POST create/update mỗi lượt','Báo chọn loại hàng; không lưu FK sai.'),
('Tên file ảnh không an toàn','image=../x.png; a/b.png; chuỗi có dấu gạch chéo ngược; 256 ký tự','POST create từng mẫu','Từ chối đường dẫn hoặc tên >255; không truy cập/ghi file ngoài uploads.'),
('Upload ảnh sản phẩm','imageFile=ảnh PNG nhỏ hơn 2MB','Chọn ảnh tại form create/update; gửi multipart','Kỳ vọng chức năng bổ sung: ảnh được lưu UUID.png và hiển thị. Hiện bị chặn compile vì imageFile chưa khai báo trong controller.'),
('Ngừng bán','P1 đang bán','Update available=false; thử POST /cart/add/{P1}','Lưu ngừng bán; không cho thêm mới vào giỏ; không xuất hiện top 4 trang chủ.'),
('Xóa hàng chưa phát sinh đơn','P_NEW không có order_details','POST delete/{P_NEW}','Bản ghi bị xóa; danh sách không còn.'),
('Chặn xóa hàng đã có đơn','P1 có order_details','POST delete/{P1}','Báo chuyển sang Ngừng bán; hàng và lịch sử đơn còn nguyên.'),
('Sửa hàng không tồn tại','id=999999','GET edit/999999; POST update với id đó','Chuyển index; không tạo mới sản phẩm hoặc lỗi 500.')]:
    add(g,r,s,title,'A1 login; csrf hợp lệ; khôi phục dữ liệu sau mỗi lượt',data,steps,expected)

g='Quản trị người dùng';r='BR-08';s='Đề tr.2,4; AccountAController, AccountService'
for title,data,steps,expected in [
('Tạo/đọc tài khoản','qa_staff; họ tên/email hợp lệ; password=Test@1234; admin=false; activated=true','Mở index; POST create; mở edit/qa_staff','Có account đúng vai trò/trạng thái; BCrypt; account active đăng nhập được.'),
('Sửa giữ mật khẩu','U2; tên mới; password rỗng','POST update; so hash trước/sau','Tên/email/vai trò/trạng thái cập nhật theo form; hash giữ nguyên khi password rỗng.'),
('Admin đặt mật khẩu mới','U2; password=New@12345','POST update; thử login mật khẩu cũ/mới','Hash đổi; cũ không dùng được; mới dùng được nếu account active.'),
('Dữ liệu tài khoản sai','Username trùng lúc create; email U1; tên trắng; mật khẩu mới 7 ký tự','Gửi từng lượt độc lập','Báo không lưu; không tạo hoặc cập nhật một phần dữ liệu không hợp lệ.'),
('Không tự khóa/bỏ quyền','username=A1 hiện đang login; admin=false hoặc activated=false','POST update từng lượt','Từ chối; A1 vẫn admin và active trong DB.'),
('Không tự xóa','id=A1','POST delete/A1','Báo không thể tự xóa; account còn nguyên.'),
('Xóa account chưa có đơn','U_EMPTY chưa có đơn','POST delete/U_EMPTY','Xóa account; không còn trong danh sách.'),
('Chặn xóa account có đơn','U1 có O1','POST delete/U1','Báo khóa thay vì xóa; account/đơn còn nguyên.'),
('Khóa user đang có phiên','U2 đang login ở trình duyệt khác; A1 update U2 activated=false','Lưu; U2 mở /order/list','U2 bị yêu cầu login lại; không dùng phiên cũ để đọc dữ liệu bảo vệ.')]:
    add(g,r,s,title,'A1 login; csrf hợp lệ; account test riêng',data,steps,expected)

g='Quản trị đơn hàng';r='BR-09';s='Đề tr.2,4; OrderAController, OrderService'
for title,data,steps,expected in [
('Tạo/đọc đơn thủ công','username=U1; productId=P1; quantity=2; address=12 Lê Lợi; phone=0901234567','POST create; mở index rồi edit/{id}','Tạo đơn PENDING thuộc U1, đúng 1 dòng quantity=2 và giá sau giảm; đọc được chi tiết/tổng.'),
('Tạo đơn dữ liệu sai','Username lạ; productId lạ; quantity=0/100; P_OFF','POST create mỗi mẫu riêng với các trường khác hợp lệ','Báo lỗi; không có orders hoặc detail dở dang.'),
('Sửa địa chỉ/điện thoại/trạng thái','O_PENDING; địa chỉ mới; phone hợp lệ; status=CONFIRMED','POST update; mở lại edit','Lưu đúng địa chỉ/phone/status; dòng hàng và giá không đổi.'),
('Trạng thái không thuộc tập','status=UNKNOWN','POST update','Từ chối; status DB không đổi.'),
('Đơn kết thúc không đổi trạng thái','O_COMPLETED và O_CANCELLED','Đổi từng đơn sang PENDING','Từ chối; trạng thái kết thúc giữ nguyên.'),
('Thêm/sửa dòng hàng PENDING','O_PENDING; P2 chưa có,quantity=2; sau đó P2 quantity=3','POST /admin/order/item hai lượt','Thêm dòng P2 rồi cập nhật cùng dòng thành 3; không trùng dòng; tổng tính lại.'),
('Chặn sửa dòng không PENDING','O_CONFIRMED, O_SHIPPING, O_COMPLETED, O_CANCELLED','POST item rồi item/delete ở từng trạng thái','Từ chối; các dòng không đổi.'),
('Số lượng dòng sai/hàng ngừng bán','O_PENDING; quantity=0/100; P_OFF','POST item từng mẫu','Từ chối; không thêm/sửa dòng.'),
('Xóa một dòng còn lại ít nhất một','O_PENDING có 2 dòng','POST /admin/order/item/delete đúng orderId/detailId','Xóa đúng dòng; còn 1 dòng; tổng giảm đúng.'),
('Chặn xóa dòng cuối hoặc dòng đơn khác','O_PENDING có 1 dòng; lượt khác có 2 dòng và detailId thuộc O2','POST item/delete từng lượt độc lập','Từ chối; không xóa dòng cuối; không xóa dòng của đơn khác.'),
('Xóa đơn được phép','Đơn PENDING và CANCELLED riêng','POST delete/{id} từng đơn','Xóa cả header và details; không để dòng mồ côi.'),
('Chặn xóa đơn đang xử lý/hoàn tất','Đơn CONFIRMED,SHIPPING,COMPLETED riêng','POST delete/{id} từng trạng thái','Báo chỉ xóa đơn chờ xác nhận/đã hủy; dữ liệu giữ nguyên.')]:
    add(g,r,s,title,'A1 login; csrf hợp lệ; reset trạng thái/dòng sau mỗi lượt',data,steps,expected)

g='Thống kê';s='Đề tr.2-3,5; ReportService, ReportAController'
for r,title,pre,data,steps,expected in [
('BR-10','Doanh thu và đủ 5 chỉ số','DB báo cáo riêng chỉ có fixture R','C1: hai dòng COMPLETED, (price=90000,quantity=2),(price=200000,quantity=1)','Mở /admin/report/revenue; đối chiếu phép tính','C1 revenue=380000; quantity=3; highest=200000; lowest=90000; average=126666.666... trước định dạng. Trung bình có trọng số theo code; đối chiếu làm tròn UI.'),
('BR-10','Không cộng đơn chưa hoàn tất','Fixture R thêm dòng C1 PENDING 999999x9 và CANCELLED 888888x8','Các dòng ngoài COMPLETED','Mở revenue','Chỉ số C1 giữ như fixture R; không cộng đơn PENDING/CANCELLED.'),
('BR-10','Tách loại và sắp xếp doanh thu','Fixture R; C2 có COMPLETED price=500000 quantity=1','C1 revenue380000; C2 revenue500000','Mở revenue','Mỗi loại một dòng; C2 trước C1; các chỉ số không trộn hai loại.'),
('BR-11','Top 10 và thứ tự VIP','DB riêng có 11 user có đơn COMPLETED','U01..U11 mua lần lượt 10000..110000','Mở /admin/report/vip','Đúng 10 user tổng cao nhất U11..U02 giảm dần; loại U01; không có user chỉ có đơn PENDING.'),
('BR-11','VIP tổng tiền và ngày đầu/cuối','U1 có 2 đơn COMPLETED ngày 01/10/2026 và 20/10/2026','Đơn đầu có nhiều dòng tổng180000; đơn sau200000; PENDING ngày21/10 giá999999','Mở vip; đối chiếu DB','U1 total380000; firstDate=01/10; lastDate=20/10; không nhân tổng do join và không lấy ngày PENDING.'),
('BR-11','VIP đồng tổng và ít hơn 10 người','DB riêng có 3 user; U1,U2 cùng tổng','U1 và U2 cùng 100000; U3=50000','Mở vip','Có 3 dòng; U1 trước U2 theo username khi đồng tổng; không bổ sung user chưa mua.'),
('BR-10,BR-11','Báo cáo không có dữ liệu','DB test không có đơn COMPLETED','Chỉ PENDING hoặc không có đơn','Mở revenue và vip','Bảng rỗng rõ ràng; không lỗi chia 0/500 hoặc hiển thị doanh thu giả.')]:
    add(g,r,s,title,'A1 login; '+pre,data,steps,expected)

g='Tích hợp và bàn giao';s='Đề tr.1,3,5; pom.xml, application.properties, templates'
for r,title,pre,data,steps,expected in [
('BR-12','Dữ liệu mẫu theo đề','Có bản DB test dùng để nộp','10 loại,200 sản phẩm,5 user thường,1 admin','Đếm bản ghi categories/products/accounts và vai trò; kiểm tra FK','Có ít nhất lượng dữ liệu mẫu theo hướng dẫn; 5 user thường và 1 admin được phân biệt; các FK hợp lệ.'),
('BR-12','Cài mới từ script bàn giao','SQL Server test riêng; bản sao source','Database/database.sql và cấu hình môi trường','Khởi tạo DB mới từ script; cấu hình DB/MAIL; build và khởi động','Script tái tạo schema/data; Hibernate validate qua; ứng dụng khởi động. Hiện chưa thấy đúng file Database/database.sql trong workspace.'),
('BR-01,BR-02,BR-04','Luồng mua hàng xuyên suốt','DB/SMTP test; U1 active; A1 admin','P1 đang bán; địa chỉ và phone hợp lệ','Khách xem hàng và thêm giỏ; vào checkout; login U1; đặt; A1 đổi COMPLETED; U1 mở My Products; A1 mở báo cáo','Đơn đúng chủ, đúng giá; lịch sử/My Products/báo cáo cùng phản ánh đơn hoàn tất; không nhân bản.'),
('BR-01','Hiển thị và form trên thiết bị','App chạy','Viewport 1366x768 và 390x844; tên tiếng Việt','Mở trang chủ, chi tiết, giỏ, checkout và admin; thao tác nút/form','Chữ Việt đúng; nội dung quan trọng đọc được; nút không che nhau; form dùng được. Đây là kiểm thử chất lượng bổ sung.'),
('BR-12','Smoke build source hiện tại','JDK17+ và Maven Wrapper khả dụng','./mvnw.cmd -q -DskipTests compile','Chạy compile; ghi exit code và lỗi','Kỳ vọng compile thành công. Quan sát 21/10/2026: exit1, imageFile chưa khai báo tại ProductAController dòng83/84/131/132; chặn test runtime.')]:
    add(g,r,s,title,pre,data,steps,expected,'P0' if 'build' in title else 'P1')

counts=Counter(c['group'] for c in cases)
plan='''# TEST PLAN - MỘC STORE / ASM JAVA 5

Phiên bản 1.0 | Ngày 21/10/2026 | Cơ sở: PDF Assignment Lập trình Java 5 (5 trang) và source hiện tại trong ASM_java5.

## 1. Mục tiêu và phạm vi
Kiểm chứng các yêu cầu bán hàng và quản trị trong đề; ưu tiên phân quyền, giá tiền, tính toàn vẹn đơn và dữ liệu. Bao phủ trang chủ/tìm hàng/chi tiết, giỏ, tài khoản/email, đăng nhập, đặt hàng/lịch sử/My Products, CRUD 4 nhóm dữ liệu, doanh thu theo loại và top10 VIP. Kiểm tra đầu vào hợp lệ, không hợp lệ, giá trị biên, dữ liệu DB và luồng xuyên suốt.

Ngoài phạm vi: thanh toán cổng ngân hàng, vận chuyển ngoài hệ thống, quản lý tồn kho, mã giảm giá, tải lớn/pentest chuyên sâu; đề và source chưa xác lập những chức năng này. Không tự coi tìm kiếm/sắp xếp/phân trang mọi màn quản trị là bắt buộc: đề ghi “nếu thấy cần thiết”.

## 2. Căn cứ và quy tắc
BR-01 Trưng bày/tìm theo loại,giá/chi tiết/cùng loại (đề tr.2). BR-02 Thêm,xem,sửa,xóa giỏ (tr.2). BR-03 Đăng ký,kích hoạt,hồ sơ,đổi/quên mật khẩu (tr.2). BR-04 Checkout,danh sách,chi tiết,My Products (tr.2). BR-05 Đăng nhập cho hồ sơ/đổi mật khẩu/toàn bộ đặt hàng; quay lại URL; admin cho quản trị (tr.2-4). BR-06/07/08/09 CRUD loại/hàng/người dùng/đơn (tr.2,4). BR-10 Doanh thu theo loại đủ 5 chỉ số (tr.2). BR-11 Top10 VIP đủ tên,tổng tiền,ngày đầu/cuối (tr.3). BR-12 Công nghệ,dữ liệu mẫu và gói bàn giao (tr.1,3,5).

Quy tắc từ CODE, không phải phát biểu trực tiếp trong đề: quantity 1..99; mật khẩu ít nhất8 ký tự và tối đa72 byte UTF-8; kích hoạt24 giờ/reset30 phút; BCrypt; CSRF trên POST; checkoutKey một lần; giá chốt lại từ DB và lưu vào OrderDetail; làm tròn HALF_UP2 chữ số; chỉ COMPLETED vào My Products/bán chạy/báo cáo. Chỉ sửa dòng đơn PENDING; chỉ xóa PENDING/CANCELLED; trạng thái kết thúc không đổi sang trạng thái khác. Từng case ghi nguồn riêng.

Cần xác nhận nghiệp vụ: (1) doanh thu có chỉ tính COMPLETED không; (2) “giá trung bình” dùng trung bình theo số lượng như code hay AVG theo dòng; (3) My Products chỉ COMPLETED hay mọi đơn đã đặt; (4) hàng ngừng bán có cần ẩn khỏi toàn bộ danh sách/chi tiết hay chỉ chặn mua; (5) lọc giá gốc hay giá sau giảm; (6) có cần bắt buộc trình tự PENDING→CONFIRMED→SHIPPING→COMPLETED không (code hiện chỉ kiểm tra tập trạng thái và khóa trạng thái kết thúc). Case theo code là baseline tạm thời, không tự xác nhận code đáp ứng đúng ý kinh doanh ở các điểm này.

## 3. Môi trường và dữ liệu
JDK17+, Maven Wrapper, Spring Boot4.1.1/Thymeleaf/JPA, SQL Server2012+ có schema tương thích, trình duyệt Chromium; SQL client để đối chiếu DB; SMTP/hộp thư TEST nhận được email. Dùng DB riêng và bản sao dữ liệu; không dùng email khách thật. DB đang cấu hình tên ASMJava5, ddl-auto=validate; cần tạo schema trước. Cấu hình MAIL_USERNAME,MAIL_PASSWORD,APP_BASE_URL phù hợp. Không đưa thông tin đăng nhập môi trường thật vào báo cáo.

U1/U2=user thường active, A1=admin active, U_LOCK=inactive. Mật khẩu test Test@1234. C1/C2=hai loại; P1 giá100000 giảm10%, P2 giá200000 giảm0%, P_OFF ngừng bán. Các tên này là alias: ghi lại ID thực sau khi tạo, không giả định ID1/2 có sẵn. O1 thuộc U1, O2 thuộc U2; fixture đơn riêng cho từng trạng thái. Email dùng hộp thư test thực của người kiểm thử. Dữ liệu báo cáo phải cách ly để tổng kỳ vọng không bị các đơn có sẵn làm nhiễu.

## 4. Cách thực hiện và ưu tiên
Thứ tự: build/khởi động → smoke → phân quyền → chức năng → biên/âm → toàn vẹn giao dịch → báo cáo → luồng xuyên suốt → regression lỗi đã sửa. Kiểm thử thủ công qua UI; request trực tiếp để vượt validation trình duyệt và kiểm tra server. Mọi POST bình thường phải lấy csrf từ form cùng session; multipart phải gửi đúng trường. Checkout lấy thêm checkoutKey từ GET /order/checkout. Case thiếu/sai token là ngoại lệ cố ý. Đo byte mật khẩu bằng UTF-8; chuỗi A×n nghĩa là lặp ký tự A n lần. Mỗi biến thể trong một case là một lượt độc lập, khôi phục dữ liệu ban đầu và ghi kết quả riêng nếu cần.

P0: bảo mật, checkout/toàn vẹn/build; P1: nghiệp vụ bắt buộc và dữ liệu biên. Người thực hiện: sinh viên/tester; người xác nhận quy tắc: chủ bài/giảng viên. Ghi actual,status,bugID,bằng chứng,build và ngày chạy; retest sau sửa rồi regression nhóm liên quan.

## 5. Điều kiện bắt đầu/kết thúc
Bắt đầu runtime khi compile thành công, app kết nối DB và khởi động được, có tài khoản theo vai trò và fixtures; test email cần SMTP hoạt động. Tạm dừng nhóm bị ảnh hưởng nếu build không qua, DB/SMTP không sẵn sàng hoặc fixture sai; ghi Blocked và nguyên nhân.

Kết thúc: 100% case trong phạm vi đã thực thi hoặc có lý do Blocked được chấp thuận; 100% P0 và chức năng bắt buộc đạt; không còn lỗi nghiêm trọng về truy cập trái phép, sai tiền/mất/trùng đơn; các lỗi còn lại có quyết định chấp nhận rõ ràng. Bàn giao Test Plan, Test Cases, log/bằng chứng và danh sách lỗi.

## 6. Hiện trạng đã kiểm tra
Đã đọc source và toàn bộ nội dung PDF; chưa chạy test chức năng và chưa kiểm chứng DB/SMTP. Lệnh `.\\mvnw.cmd -q -DskipTests compile` chạy ngày21/10/2026 thất bại vì biến imageFile chưa khai báo trong ProductAController ở dòng83/84/131/132. Đây là lỗi build đã quan sát; runtime từ build mới bị chặn. Không suy ra một tiến trình web đang chạy dùng đúng source này. Case smoke build được ghi Fail; các case khác để Not Run, khi bắt đầu một đợt chạy mà lỗi build còn tồn tại thì ghi Blocked.

Chưa thấy đúng đường dẫn Database/database.sql được yêu cầu khi bàn giao; cần xác minh script thay thế/khôi phục trước test cài mới. Chưa sửa code trong tác vụ lập tài liệu này.
'''
(OUT/'Test_Plan.md').write_text(plan,encoding='utf-8')
intro=f'''# BỘ TEST CASE CHI TIẾT - MỘC STORE

Ngày 21/10/2026 | {len(cases)} test case | Nguồn nghiệp vụ: đề tr.2-5; đối chiếu source hiện tại.

Đọc Test_Plan.md trước khi chạy: có fixture, định nghĩa vai trò, cách lấy CSRF/checkoutKey, quy tắc từ đề so với code và các điểm cần xác nhận. Đây là thiết kế kiểm thử, không phải báo cáo đã chạy. Các kỳ vọng bổ sung về dữ liệu/giao diện được ghi rõ. Case có nhiều mẫu phải thực thi từng mẫu riêng và reset dữ liệu; chỉ Pass khi mọi mẫu đạt.

## Danh mục và truy vết

| Nhóm | Số case | ID |
|---|---:|---|
'''
for group,count in counts.items():
    ids=[c['id'] for c in cases if c['group']==group]
    intro+=f"| {group} | {count} | {ids[0]} - {ids[-1]} |\n"
intro+='\n## Nội dung kiểm thử\n'
for c in cases:
    actual='Exit code 1; cannot find symbol: variable imageFile trong ProductAController; compile không thành công.' if 'Smoke build' in c['title'] else 'Chưa thực hiện.'
    status='Fail (build đã chạy ngày 21/10/2026)' if 'Smoke build' in c['title'] else 'Not Run'
    intro+=f"\n### {c['id']} - {c['title']}\n\n- **Nhóm / Yêu cầu:** {c['group']} / {c['req']}\n- **Nguồn:** {c['source']}\n- **Ưu tiên:** {c['priority']}\n- **Tiền điều kiện:** {c['pre']}\n- **Dữ liệu:** {c['data']}\n- **Bước thực hiện:** {c['steps']}\n- **Kết quả mong đợi:** {c['expected']}\n- **Kết quả thực tế:** {actual}\n- **Trạng thái:** {status}\n- **Bug ID / Bằng chứng / Người chạy / Ngày:** {'BUILD-01 / Maven compile output / Codex / 21-10-2026' if 'Smoke build' in c['title'] else 'Chưa ghi'}\n"
(OUT/'Test_Cases.md').write_text(intro,encoding='utf-8')
# Single printable HTML artifact: no external assets or dependencies.
def paras(text):
    return ''.join('<p>'+html.escape(p).replace('\n','<br>')+'</p>' for p in text.split('\n\n'))
body='<h1>Test Plan & Test Cases · Mộc Store</h1><p>21/10/2026 · '+str(len(cases))+' test case · Thiết kế theo đề và source hiện tại</p>'
body+='<section>'+paras(plan)+'</section><h1>Bộ test case chi tiết</h1>'
for c in cases:
    body+='<article><h2>'+html.escape(c['id']+' · '+c['title'])+'</h2><dl>'
    for label,key in [('Nhóm','group'),('Yêu cầu','req'),('Nguồn','source'),('Ưu tiên','priority'),('Tiền điều kiện','pre'),('Dữ liệu','data'),('Bước thực hiện','steps'),('Kết quả mong đợi','expected')]:
        body+='<dt>'+label+'</dt><dd>'+html.escape(c[key])+'</dd>'
    body+='<dt>Kết quả thực tế / Trạng thái</dt><dd>'+('Compile thất bại vì imageFile chưa khai báo / Fail / BUILD-01' if 'Smoke build' in c['title'] else 'Chưa thực hiện / Not Run')+'</dd><dt>Bug ID / Bằng chứng / Người chạy / Ngày</dt><dd>........................................</dd></dl></article>'
(OUT/'Test_Plan_va_Test_Cases.html').write_text('<!doctype html><html lang="vi"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Mộc Store - Test Plan và Test Cases</title><style>body{font:16px/1.6 "Segoe UI",sans-serif;color:#22382c;background:#faf9f5;max-width:1080px;margin:40px auto;padding:0 24px}h1{color:#36553b}article,section{background:white;border:1px solid #d9dfd5;border-radius:8px;padding:24px;margin:24px 0}h2{font-size:21px;margin-top:0}dt{font-weight:650;margin-top:10px}dd{margin-left:0;color:#333}@media print{body{background:white;margin:0;max-width:none;font-size:11pt}article{break-inside:avoid;border-radius:0;padding:12px}h1{break-before:page}h1:first-child{break-before:auto}}</style><body>'+body+'</body></html>',encoding='utf-8')
assert len({c['id'] for c in cases})==len(cases)
assert all(all(str(v).strip() for v in c.values()) for c in cases)
print(f'Created {len(cases)} cases across {len(counts)} groups in {OUT}')
print(dict(counts))
