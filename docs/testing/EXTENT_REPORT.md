# Extent Report trong Mộc Store

Thư viện: com.aventstack:extentreports:5.1.2, scope test. Bộ test vẫn dùng JUnit Jupiter và Selenium; không chuyển runner sang TestNG.

## Cách hoạt động

`junit-platform.properties` bật auto-detection. File `META-INF/services/org.junit.jupiter.api.extension.Extension` đăng ký `ExtentReportExtension` cho mọi test Jupiter, nên không cần thêm `@ExtendWith` vào từng lớp.

Trước mỗi test, extension tạo một mục báo cáo với tên lớp/phương thức và gắn tag ui/smoke cùng mã `@CaseIds`. Khi JUnit xác định kết quả sau teardown, TestWatcher ghi PASS/FAIL/SKIP; test tham số có mỗi lượt một mục riêng. Extension giữ một ReportSession trong root ExtensionContext và tự flush khi đợt chạy kết thúc. Nó không tự đổi kết quả assertion.

StoreSeleniumTests cung cấp ảnh qua ScreenshotEvidence. @AfterEach chụp ảnh trước khi quit driver; watcher lấy đường dẫn ảnh sau đó và sao chép vào thư mục báo cáo. Vì vậy báo cáo vẫn mở được ảnh sau khi tải Artifact xuống. Nếu Chrome chưa khởi động được thì có thể có lỗi test nhưng không có ảnh.

## Chạy và mở báo cáo

JUnit:

```powershell
.\mvnw.cmd test
```

Mở `target/extent-report/junit/index.html`.

Selenium (ẩn cửa sổ khi chạy tự động):

```powershell
.\mvnw.cmd -Pui "-Dgroups=ui" "-Dheadless=true" test
```

Mở `target/extent-report/selenium/index.html`. Dùng `-Dheadless=false` để thấy Chrome.

Nếu bấm Run trực tiếp trong IntelliJ mà không đặt property Maven, báo cáo mặc định ở `target/extent-report/local/index.html`. Có thể đặt VM option `-Dextent.report.directory=target/extent-report/my-run` để đổi chỗ lưu.

Script `scripts/run-tests.ps1` lưu từng lần chạy vào `target/test-runs/<thời điểm>/extent/junit` và `extent/selenium`; không ghi đè báo cáo của đợt trước. Dùng -ShowBrowser khi muốn nhìn Chrome; mặc định script chạy headless. Maven chạy trực tiếp ghi đè báo cáo suite tương ứng mỗi lần.

## Nội dung

- index.html: giao diện Extent Spark với danh sách test, trạng thái, thời gian, tags/Case IDs, exception và ảnh.
- results.json: kết quả do JsonFormatter của Extent tạo, dùng đối chiếu tự động với Surefire XML.
- screenshots/: ảnh Selenium được copy vào report, dùng đường dẫn tương đối.
- spark/: CSS/JS/font được xuất ở chế độ offline. Giữ toàn bộ thư mục báo cáo, không chỉ lấy riêng index.html.

Maven Surefire được cấu hình JVM UTF-8 để JSON và HTML giữ chữ tiếng Việt trên JDK17/Windows. Nếu tự chạy bằng cấu hình IntelliJ dùng encoding khác, nên thêm VM option `-Dfile.encoding=UTF-8`.

## GitHub Actions

Workflow chạy hai job JUnit/Selenium riêng. Mỗi job tạo report của mình, kiểm tra index.html tồn tại và upload cả `target/extent-report/` cùng XML/ảnh cũ, kể cả khi test thất bại. Đây là Extent Report thật, không phải trang HTML Surefire đổi tên.

Sau khi push: repository → Actions → chọn lần chạy → Artifacts → tải file java-test-reports-junit-* hoặc java-test-reports-selenium-* → giải nén toàn bộ → mở index.html trong thư mục extent-report tương ứng.

Chưa xác nhận workflow trên máy GitHub cho đến khi có lần chạy thực tế ở đó. TestWatcher không báo kết quả phương thức chưa được thực thi vì lỗi @BeforeAll/class-level initialization; xem thêm Surefire XML và log CI để biết lỗi cấp lớp. Nếu test bị JVM kill/timeout thì báo cáo có thể không được flush; log/artifacts còn lại vẫn là nguồn chẩn đoán.

## Kiểm tra chính phần báo cáo

ExtentReportingTests chạy một fixture JUnit riêng trong thư mục tạm: có Pass, Fail có chủ đích, Aborted, Disabled và hai lượt ParameterizedTest. Nó xác nhận tổng trạng thái, nội dung lỗi, ảnh sau teardown và file offline. Các lỗi chủ đích chỉ nằm trong fixture để kiểm chứng reporter; chúng không phải lỗi nghiệp vụ và không làm bộ test chính thất bại khi reporter hoạt động đúng.

Tài liệu chính thức: https://extentreports.com/docs/versions/5/java/index.html và https://docs.junit.org/6.0.3/extensions/keeping-state-in-extensions.html
