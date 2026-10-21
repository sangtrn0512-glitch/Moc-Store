# Minh chứng AI, debug và review Y3 / Y4

Ngày: 22/10/2026. Công cụ: Codex trong phiên chat này. Model ID chi tiết không được ghi nhận, không tự điền một phiên bản model suy đoán.

## Prompt và ngữ cảnh

Prompt thực hiện trực tiếp của sinh viên:

> ok hãy làm cho tôi y3 và y4

Ngữ cảnh đã cung cấp trong cùng hội thoại:

- `C:/Users/sang/Desktop/ASM_kiemthu/Assignment (1).pdf`: đề Kiểm thử nâng cao cùng AI, Y3 JUnit và Y4 Selenium.
- `C:/Users/sang/Desktop/ALL FPT/05_SOF3022 - Lap trinh Java 5/De Assignment/Assignment_Lập trình Java 5.pdf`: yêu cầu nghiệp vụ bán hàng/quản trị.
- Source ASM_java5 hiện tại; bộ 122 Test Case Y2 được tạo trước đó. Catalogue được đọc từ bản generator đã có trong Git commit `922df4dcf1e559d4447c838c2316a5e4a8103fec`, không phục hồi những file cũ mà sinh viên đã xóa.
- Sinh viên đã hỏi cách tách code test để không ảnh hưởng code chính; giải pháp thực hiện dùng src/test, Mockito và H2 in-memory, email mock.

Đây là ngữ cảnh/RAG được cung cấp trực tiếp từ tài liệu và source, không phải bằng chứng đã xây dựng một hệ thống vector database riêng.

## Phiên bản AI và phiên bản cuối

- `evidence/ai-draft-before-selenium-fix.zip`: snapshot code test/pom được lưu ngay trước sửa lỗi selector Selenium. Đây là bản trung gian AI đã tạo, không tuyên bố là bản đầu tiên chưa từng sửa.
- `evidence/ai-final-test-code.zip`: snapshot code test/pom sau các bước debug và review của agent.
- Code đang dùng nằm ở `src/test`, cấu hình dependencies tại pom.xml.
- Catalogue JSON và kết quả Markdown là bản AI cập nhật từ Y2 để khớp source 22/10: URL lọc sản phẩm đã chuyển hướng; CategoryService đã có giới hạn100; upload đã có imageFile; script database.sql hiện có; lỗi compile cũ không còn. Đây là cập nhật của AI, không gán cho sinh viên.
- Chưa có bằng chứng sinh viên tự chỉnh sửa trong lần thực hiện này. Không đổi nhãn bản cuối của AI thành “bản sinh viên chỉnh sửa”. Sinh viên cần review, ghi quyết định và lưu bản chỉnh sửa thực tế của mình sau đó.

## Lỗi test harness đã xử lý

### DEBUG-01: MockMvc standalone bị Circular view path

Log thật: `evidence/initial-controller-test-errors.txt`.

Triệu chứng: trả logical view `sign-up` bị resolver mặc định của standalone MockMvc diễn giải thành forward lại URL hiện tại, gây Circular view path. Đây là cấu hình test harness, không kết luận lỗi Thymeleaf của app thật.

Cách AI xử lý: cấu hình InternalResourceViewResolver với prefix `/test-views/` và suffix `.html` cho standalone controller test. Test chỉ kiểm tra view/model/redirect; các integration test và Selenium kiểm tra render Thymeleaf thật riêng.

### DEBUG-02: Test dùng luồng lọc sản phẩm cũ

Log thật cũng nằm trong initial-controller-test-errors.txt: kỳ vọng 200 tại URL cũ nhưng thực tế 302.

Cách AI xử lý: đọc lại ProductController hiện tại. URL list-by-category và list-by-price nay chuyển hướng sang `/product/list` có query params; cập nhật assertions theo luồng đã có, vẫn kiểm tra kết quả lọc/validation ở URL mới. Không sửa controller để khớp kỳ vọng lỗi thời.

### DEBUG-03: Selenium không tìm thấy form đăng ký

Log thật: `evidence/initial-selenium-errors.txt`; ảnh trước sửa: `evidence/initial-registration-timeout.png`.

Triệu chứng: TimeoutException khi tìm `form[action='/account/sign-up']`. Ở request đầu chưa có cookie, Spring có thể rewrite action thành `/account/sign-up;jsessionid=...`.

Cách AI xử lý: đổi selector của SignUpPage thành `form[action^='/account/sign-up']`, vẫn tìm đúng form đăng ký kể cả action chứa mã phiên. Chạy lại browser test để xác minh thao tác đăng ký, account trong DB, hash mật khẩu và yêu cầu gửi mail.

### DEBUG-04: Phiên bản Selenium không đồng nhất

Log lỗi Selenium đầu tiên ghi Build info 4.43.0 trong khi dependency selenium-java được khai báo 4.51.0. Spring Boot dependency management đang quản lý các module con ở phiên bản khác.

Cách AI xử lý: đặt property `selenium.version=4.51.0` trong pom để đồng bộ Selenium BOM và các module; kiểm tra dependency tree và chạy lại trên JDK17. Không chỉ thay số version ở selenium-java rồi bỏ qua các dependency con.

## Rủi ro nghiệp vụ được regression test kiểm tra

### REVIEW-01: Kiểm tra lại giới hạn tên loại giữa tạo và sửa

- Test: `AdminIntegrationTests#categoryCreateRejectsNameOverOneHundred`, TC-074.
- Tiền điều kiện: admin active; H2 riêng có 2 loại; CSRF hợp lệ.
- Thao tác: POST `/admin/category/create` với tên gồm 101 ký tự `a`, vượt giới hạn 100 được áp dụng ở nhánh update và maxlength của form.
- Mong đợi: từ chối trước khi lưu; số loại giữ nguyên.
- Kết quả: test đạt trên source hiện tại; không thêm bản ghi quá giới hạn. Xem XML cuối cùng trong evidence và Test_Cases_Y3_Y4.md.
- Phân tích source: create không kiểm tra length ngay trong controller nhưng CategoryService.save hiện đã kiểm tra tên trắng/null/>100 và ném IllegalArgumentException; FormErrors xử lý thành HTTP400. Vì vậy không kết luận có lỗi chỉ dựa trên controller hoặc nhận định từ snapshot cũ.
- Giới hạn kết luận: test quan sát trên H2 sinh schema từ entity. SQL Server thật có thể từ chối bằng constraint riêng; chưa chạy lại ở SQL Server nên không tự gán mã HTTP/chi tiết lỗi cho môi trường đó.
- Quyết định: giữ regression assertion để chống tái phát. Không sửa code nghiệp vụ chính trong tác vụ kiểm thử này. Chưa có lỗi nghiệp vụ được xác nhận bởi bộ test cuối; không gán Bug ID cho nghi ngờ đã được source/runtime bác bỏ.

## Review của AI và quyết định áp dụng

| Đề xuất | Quyết định và lý do |
|---|---|
| Tách test khỏi SQL Server thật | Áp dụng H2 in-memory và Mockito; giảm phụ thuộc và tránh sửa dữ liệu học tập |
| Không gửi SMTP thật | Mock MailService; verify địa chỉ/nội dung/token; ghi rõ chưa test hạ tầng email |
| Kiểm tra transaction rollback bằng mock | Không dùng mock để chứng minh rollback; dùng Spring bean thật với H2 |
| Kiểm tra giỏ cách ly chỉ bằng hai đối tượng Java | Thay bằng hai MockHttpSession qua Spring MVC và bean SessionScope thật |
| Bỏ assertion khi test thất bại | Không áp dụng; phân biệt sai test harness/kỳ vọng cũ với lỗi nghiệp vụ thật |
| Dùng sleep cố định cho Selenium | Không áp dụng; dùng WebDriverWait theo điều kiện URL/DOM/alert |
| Dùng Page Object và fixture độc lập | Áp dụng; mỗi browser test tạo phiên và dữ liệu mới, không dựa vào thứ tự chạy |
| Lưu ảnh vào uploads của app chính khi test | Không áp dụng; FileService thật chạy trong JVM con ở thư mục tạm |
| Ghi Pass cho SQL Server và email vì test H2/mock đạt | Không áp dụng; tách giới hạn và các case chưa chạy |
| Ghi “bản sinh viên chỉnh sửa” cho code AI | Không áp dụng khi chưa có hành động review/chỉnh sửa thật của sinh viên |

## Tài liệu chính thức đã đối chiếu

- Selenium Chrome options/headless: https://www.selenium.dev/documentation/webdriver/browsers/chrome/
- Selenium releases: https://www.selenium.dev/downloads/
- H2 compatibility modes và giới hạn: https://h2database.github.io/html/features.html

Các tài liệu trên hỗ trợ lựa chọn công cụ; kết quả Pass/Fail chỉ lấy từ execution XML của project.

## Bổ sung Extent Report theo yêu cầu sau đó

Prompt của sinh viên: “ok thêm phần này vào code của tôi”, sau trao đổi về tích hợp Extent vào JUnit/Selenium và upload qua GitHub Actions.

AI bổ sung ExtentReportExtension tự đăng ký bằng ServiceLoader, ReportSession tự flush, reporter Spark offline và JsonFormatter, liên kết CaseIds/tags; lấy screenshot sau teardown qua ScreenshotEvidence. Workflow upload toàn bộ Extent Report và kiểm tra index.html được tạo. Không thêm TestNG vì bộ test hiện dùng JUnit và đề cho lựa chọn báo cáo Extent ở Y5.

DEBUG-05: lần chạy đầu trên JDK17/Windows gặp MalformedInputException khi đọc JSON UTF-8 vì JsonFormatter ghi bằng encoding mặc định JVM. Log gốc: evidence/extent-initial-encoding-error.log. AI cấu hình argLine `-Dfile.encoding=UTF-8` cho Surefire, chạy lại reporter validation và toàn bộ test. HTML Spark cũng cấu hình UTF-8.

ExtentReportingTests dùng một fixture độc lập có kết quả Fail/Abort/Disabled có chủ đích và hai lượt parameterized để kiểm chứng reporter. Ngoại lệ được launcher test riêng thu thập; không phải lỗi app và không được tính là Fail của bộ nghiệp vụ. Fixture nằm trong thư mục tạm và không xuất vào báo cáo chính. Đây là kiểm thử tính đúng của hệ thống báo cáo, tránh chỉ kiểm tra file HTML có tồn tại.

Report Spark có CSS/JS/font offline và ảnh dùng đường dẫn tương đối; bản ZIP evidence/extent-reports-verified.zip lưu báo cáo đã tạo. Chi tiết vận hành nằm trong EXTENT_REPORT.md. Việc chạy thực tế trên GitHub vẫn cần push và xem lần chạy Actions; không suy ra CI đã thành công chỉ từ test Windows local.
