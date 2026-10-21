# Hướng dẫn Y3 và Y4 - Mộc Store

## Các phần đã tích hợp

- Y3: JUnit Jupiter, Mockito, test tham số, MockMvc cho controller/interceptor, test tích hợp transaction/repository/report với H2.
- Y4: Selenium Java 4.51.0, ChromeDriver do Selenium Manager tìm/cấp, Page Objects và assertions cho đăng ký, kích hoạt, đăng nhập, CRUD loại hàng; bổ sung luồng mua hàng.
- Toàn bộ code kiểm thử nằm trong `src/test`. Dependencies H2/Selenium có scope `test`, không đưa vào runtime của ứng dụng đóng gói.
- Đã bổ sung GitHub Actions chạy JUnit/Selenium và Extent Report thật. Xem EXTENT_REPORT.md để chạy, mở HTML và tải Artifact. Execution_Report.html cũ vẫn là trang tổng hợp riêng, không phải Extent.

Phiên bản đã xác nhận từ classpath của lần chạy: JUnit Jupiter 6.0.3, Mockito 5.23.0, H2 2.4.240, Selenium 4.51.0, Maven Surefire 3.5.6; runtime Java 17.0.20.1. JUnit/Mockito/H2 được Spring Boot quản lý phiên bản; property selenium.version đồng bộ các module Selenium. Chrome và ChromeDriver được Selenium dùng từ môi trường máy, không thay phiên bản Chrome hệ thống.

## Cách ly với ứng dụng chính

`application-test.properties` nằm tại `src/test/resources`, chỉ dùng khi chạy test. Profile `test` dùng H2 in-memory, Hibernate create-drop; các lớp Spring test có `@ActiveProfiles("test")`. Không thay cấu hình SQL Server của ứng dụng chính.

MailService được Mockito thay thế trong test tích hợp/browser: không gửi thư đến Gmail. Test kiểm tra nội dung yêu cầu gửi thư và token; việc thực sự nhận thư SMTP phải kiểm tra riêng. Bộ fixture kiểm tra JDBC URL phải bắt đầu bằng `jdbc:h2:mem:` trước khi xóa/tạo dữ liệu test.

Selenium khởi động ứng dụng thật tại cổng ngẫu nhiên bằng `@SpringBootTest(RANDOM_PORT)`, không điều khiển cửa sổ web đang dùng và không cần bật app chính ở 8080. Mỗi test có phiên Chrome mới; chạy trực tiếp hiện mặc định mở cửa sổ, dùng -Dheadless=true để ẩn. Script và CI chạy headless. Sau test, driver được quit.

Test lưu ảnh thật của FileService chạy trong JVM con với working directory tạm do JUnit cấp; không ghi ảnh vào thư mục uploads của app chính. Test multipart controller giả lập kho file để kiểm tra binding và lưu DB độc lập.

## Chạy trên PowerShell, tại thư mục chứa pom.xml

Nên dùng JDK 17 đã có trên máy; chỉ đặt biến cho terminal hiện tại:

```powershell
$env:JAVA_HOME='C:/Users/sang/.jdks/ms-17.0.20.1'
```

Chạy JUnit (không mở trình duyệt):

```powershell
.\mvnw.cmd test
```

Hoặc dùng script tiện ích để chạy test, giữ log theo từng đợt và tự cập nhật báo cáo:

```powershell
.\scripts\run-tests.ps1 -Suite all
```

Các giá trị `-Suite` gồm `junit`, `selenium`, `smoke`, `all`. Có thể thêm `-ShowBrowser` khi muốn nhìn Chrome. Script vẫn chạy bộ Selenium sau khi JUnit có lỗi để ghi đủ bằng chứng, nhưng trả exit code 1 nếu bất kỳ bộ nào thất bại.

Chạy Selenium riêng:

```powershell
.\mvnw.cmd -Pui "-Dgroups=ui" test
```

Muốn xem cửa sổ Chrome thao tác:

```powershell
.\mvnw.cmd -Pui "-Dgroups=ui" "-Dheadless=false" test
```

Chạy smoke JUnit, lưu báo cáo riêng để không ghi đè kết quả full run:

```powershell
.\mvnw.cmd "-Dgroups=smoke" "-Dtest.reportDirectory=target/smoke-reports" test
```

Chạy cả JUnit và Selenium trong cùng đợt:

```powershell
.\mvnw.cmd -Pui test
```

Profile ui chỉ cho phép nhóm ui được chạy; `-Dgroups=ui` mới giới hạn chỉ nhóm browser. Các lớp test có tên kết thúc bằng Tests để Surefire tìm thấy. `@Tag("smoke")` gắn cho những test quan trọng.

Chrome phải được cài; lần chạy đầu cần mạng để tải dependencies/driver khi chưa có cache. Không bật chạy test song song vì bộ fixture cố ý reset DB giữa từng test.

## Kết quả và cách đọc

- JUnit mặc định: `target/surefire-reports/TEST-*.xml` và `.txt`.
- Chạy profile ui: `target/selenium-reports/`.
- Ảnh và HTML sau mỗi test Selenium: `target/selenium-evidence/`.
- Test Case đã cập nhật: `docs/testing/Test_Cases_Y3_Y4.md`.
- Báo cáo dễ xem: `docs/testing/Execution_Report.html`.
- Kết quả thực thi có cấu trúc: `docs/testing/execution-results.json`.

Lưu ý: `mvn clean` xóa target, nhưng bản evidence được script sao chép vào docs/testing sẽ còn. Báo cáo không tự cập nhật sau khi sửa test; phải chạy lại test và chạy script bên dưới. XML được cung cấp từ lần chạy nào thì kết quả chỉ đại diện lần chạy đó; không trộn báo cáo smoke với full run.

```powershell
python scripts/update_test_results.py
```

Nếu máy không có lệnh python, dùng Python bundled đã được dùng để tạo báo cáo:

```powershell
& 'C:/Users/sang/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe' -X utf8 scripts/update_test_results.py
```

Nếu chạy toàn bộ bằng `-Pui test`, chỉ lấy bộ XML đó để tránh đếm hai lần:

```powershell
python scripts/update_test_results.py --reports target/selenium-reports
```

`@CaseIds` liên kết test Java với mã TC trong catalogue Y2; nó là nhãn truy vết do project định nghĩa, không phải annotation có sẵn của JUnit. Script đọc annotation và XML để điền kết quả thật. Một case nhiều biến thể chỉ được coi là hoàn tất toàn bộ khi mọi biến thể cần thiết đã được thực hiện; Pass ở bảng hiện tại mô tả phạm vi script, không thay thế review thủ công.

## Những phần cần hiểu khi bảo vệ

1. Service test: chuẩn bị mock → gọi phương thức → assert kết quả, verify có/không ghi repository.
2. Controller test: MockMvc gửi request thật qua Spring MVC hoặc standalone controller; kiểm tra status, view, model, redirect và CSRF.
3. Integration test: dùng Spring bean thật với H2, kiểm tra rollback và phép tính DB; không dùng mock để tuyên bố transaction đã rollback.
4. Page Object: lưu locator/thao tác của trang; Test Script quyết định dữ liệu và assertions.
5. Fixture: dữ liệu riêng được tạo lại trước từng test; không phụ thuộc ID cố định hoặc thứ tự test.
6. Selenium waits dựa vào điều kiện DOM/URL/alert, không dùng sleep cố định. Ảnh chụp chỉ là bằng chứng bổ sung; assertions quyết định kết quả.

## Giới hạn cần ghi đúng trong bài

H2 MODE=MSSQLServer chỉ tương thích một phần, không chứng minh schema, collation hay script SQL Server thật hoạt động giống hệt. Các case dữ liệu mẫu, cài mới SQL Server và đánh giá toàn bộ giao diện mobile/desktop được để Not Run khi chưa thực hiện. Việc khẳng định đã nhận email thật cũng không thuộc kết quả của mock mail.

Nếu regression test phát hiện lỗi nghiệp vụ, giữ Fail và ghi Bug ID; không xóa assertion hoặc đổi mong đợi để làm xanh. Đọc BUGS_AND_AI_EVIDENCE.md để xem lỗi phát hiện và những sửa đổi của code kiểm thử. Sinh viên cần tự review bản AI và lưu quyết định/chỉnh sửa thật; chưa có bản chỉnh sửa của sinh viên thì không ghi là đã có.
