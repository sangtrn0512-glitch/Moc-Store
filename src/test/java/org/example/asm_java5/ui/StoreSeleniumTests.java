package org.example.asm_java5.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.file.*;
import java.time.Duration;
import org.example.asm_java5.support.*;
import org.example.asm_java5.ui.pages.*;
import org.example.asm_java5.reporting.ScreenshotEvidence;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Tag("ui")
class StoreSeleniumTests extends DatabaseTestSupport implements ScreenshotEvidence {
  WebDriver driver;
  String url;
  private Path reportScreenshot;

  // Trả ảnh chụp test để đính kèm báo cáo.
  @Override
  public Path screenshotForReport() { return reportScreenshot; }

  // Mở trình duyệt mới và chuẩn bị URL ứng dụng trước mỗi test.
  @BeforeEach
  void browser() {
    reportScreenshot = null;
    ChromeOptions options = new ChromeOptions();
    if (Boolean.parseBoolean(System.getProperty("headless", "false")))
      options.addArguments("--headless=new");
    options.addArguments("--window-size=1440,1000", "--disable-dev-shm-usage");
    driver = new ChromeDriver(options);
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    url = "http://localhost:" + port;
  }

  @AfterEach
  // Lưu ảnh, HTML làm minh chứng và đóng trình duyệt sau mỗi test.
  void evidenceAndClose(TestInfo info) throws Exception {
    if (driver == null) return;
    try {
      Path directory = Path.of("target", "selenium-evidence");
      Files.createDirectories(directory);
      String name = info.getTestMethod().orElseThrow().getName();
      Files.write(
          directory.resolve(name + ".png"),
          ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
      Files.writeString(directory.resolve(name + ".html"), driver.getPageSource());
      reportScreenshot = directory.resolve(name + ".png");
    } finally {
      driver.quit();
    }
  }

  // Đăng nhập admin và mở trang quản lý danh mục.
  CategoryPage adminCategories() {
    new LoginPage(driver, url)
        .open()
        .login("qa_admin", "Test@1234")
        .awaitPath("/admin/product/index");
    return new CategoryPage(driver, url).open();
  }

  // Kiểm tra đăng ký qua trình duyệt lưu tài khoản chưa kích hoạt và mật khẩu mã hóa.
  @Test
  @Tag("smoke")
  @CaseIds("TC-024")
  void registrationPersistsInactiveHashedAccount() {
    SignUpPage page =
        new SignUpPage(driver, url)
            .open()
            .register("qa_new", "Nguyễn Kiểm Thử", "qa_new@example.invalid", "Test@1234");
    assertTrue(page.success().contains("Đăng ký thành công"));
    var a = accounts.findById("qa_new").orElseThrow();
    assertFalse(a.isActivated());
    assertFalse(a.isAdmin());
    assertTrue(new BCryptPasswordEncoder().matches("Test@1234", a.getPassword()));
    assertNotNull(a.getActivationToken());
    verify(mail)
        .send(
            eq("qa_new@example.invalid"), contains("Kích hoạt"), contains(a.getActivationToken()));
  }

  // Kiểm tra trình duyệt báo lỗi khi đăng ký trùng tên đăng nhập.
  @Test
  @CaseIds("TC-026")
  void duplicateRegistrationRejectedInBrowser() {
    long before = accounts.count();
    SignUpPage page =
        new SignUpPage(driver, url)
            .open()
            .register("qa_user", "QA", "new@example.invalid", "Test@1234");
    assertTrue(page.error().contains("đã tồn tại"));
    assertEquals(before, accounts.count());
    verifyNoInteractions(mail);
  }

  // Kiểm tra mở link kích hoạt rồi đăng nhập được tài khoản mới.
  @Test
  @CaseIds("TC-030")
  void activationLinkAllowsNewAccountLogin() {
    new SignUpPage(driver, url)
        .open()
        .register("qa_new", "Nguyễn QA", "qa_new@example.invalid", "Test@1234")
        .success();
    String token = accounts.findById("qa_new").orElseThrow().getActivationToken();
    driver.get(url + "/account/activate?token=" + token);
    assertTrue(accounts.findById("qa_new").orElseThrow().isActivated());
    new LoginPage(driver, url).open().login("qa_new", "Test@1234").awaitPath("/");
    assertFalse(driver.findElements(By.cssSelector("a[href='/account/edit-profile']")).isEmpty());
  }

  // Kiểm tra user và admin đăng nhập được chuyển tới đúng trang theo quyền.
  @Test
  @Tag("smoke")
  @CaseIds("TC-046")
  void userAndAdminLoginRedirectByRole() {
    new LoginPage(driver, url).open().login("qa_user", "Test@1234").awaitPath("/");
    assertFalse(
        driver.findElements(By.cssSelector("form[action='/auth/logout'] button")).isEmpty());
    driver.findElement(By.cssSelector("form[action='/auth/logout'] button")).click();
    new WebDriverWait(driver, Duration.ofSeconds(12))
        .until(ExpectedConditions.urlToBe(url + "/auth/login"));
    new LoginPage(driver, url).login("qa_admin", "Test@1234").awaitPath("/admin/product/index");
    assertTrue(driver.findElement(By.tagName("h1")).getText().contains("sản phẩm"));
  }

  // Kiểm tra trên trình duyệt: sai mật khẩu hoặc chưa kích hoạt đều không đăng nhập được.
  @Test
  @CaseIds("TC-047")
  void badPasswordAndInactiveAccountCannotLogin() {
    LoginPage page = new LoginPage(driver, url).open().login("qa_user", "wrong-password");
    assertTrue(page.error().contains("Sai tên đăng nhập hoặc mật khẩu"));
    user.setActivated(false);
    accounts.save(user);
    page.open().login("qa_user", "Test@1234");
    assertTrue(page.error().contains("chưa kích hoạt"));
    assertTrue(driver.getCurrentUrl().endsWith("/auth/login"));
  }

  // Kiểm tra trình duyệt quay lại trang yêu cầu ban đầu sau đăng nhập.
  @Test
  @CaseIds("TC-049")
  void loginReturnsToOriginalProtectedPage() {
    driver.get(url + "/order/list");
    new LoginPage(driver, url).login("qa_user", "Test@1234").awaitPath("/order/list");
    assertTrue(driver.findElement(By.tagName("h1")).getText().contains("Đơn hàng"));
  }

  // Kiểm tra thêm danh mục qua trình duyệt và thấy dữ liệu đã tạo.
  @Test
  @Tag("smoke")
  @CaseIds("TC-070")
  void createAndReadCategoryViaBrowser() {
    CategoryPage page = adminCategories();
    page.create("Selenium QA mới");
    assertTrue(page.contains("Selenium QA mới"));
    var created =
        categories.findAll().stream()
            .filter(c -> c.getName().equals("Selenium QA mới"))
            .findFirst()
            .orElseThrow();
    page.edit(created.getId());
    assertEquals("Selenium QA mới", page.nameInForm());
  }

  // Kiểm tra sửa danh mục qua trình duyệt.
  @Test
  @CaseIds("TC-072")
  void updateCategoryViaBrowser() {
    CategoryPage page = adminCategories();
    page.edit(c2.getId());
    assertEquals(c2.getName(), page.nameInForm());
    page.update("Selenium QA sửa");
    assertTrue(page.contains("Selenium QA sửa"));
    assertFalse(page.contains("Đèn QA"));
    assertEquals("Selenium QA sửa", categories.findById(c2.getId()).orElseThrow().getName());
  }

  // Kiểm tra xóa danh mục chưa có sản phẩm qua trình duyệt.
  @Test
  @CaseIds("TC-075")
  void deleteEmptyCategoryViaBrowser() {
    CategoryPage page = adminCategories();
    page.delete(c2.getId());
    assertFalse(page.contains(c2.getName()));
    assertFalse(categories.existsById(c2.getId()));
  }

  // Kiểm tra trình duyệt báo lỗi khi thêm trùng danh mục.
  @Test
  @CaseIds("TC-071")
  void duplicateCategoryRejectedViaBrowser() {
    CategoryPage page = adminCategories();
    long before = categories.count();
    page.create(c1.getName());
    assertTrue(page.error().contains("đã tồn tại"));
    assertEquals(before, categories.count());
  }

  // Kiểm tra trình duyệt chặn xóa danh mục đang có sản phẩm.
  @Test
  @CaseIds("TC-076")
  void cannotDeleteCategoryContainingProductsViaBrowser() {
    CategoryPage page = adminCategories();
    page.delete(c1.getId());
    assertTrue(page.error().contains("đang có sản phẩm"));
    assertTrue(categories.existsById(c1.getId()));
    assertEquals(2, products.count());
  }

  // Kiểm tra mua hàng qua trình duyệt, hoàn tất đơn và thấy lịch sử cùng doanh thu đúng.
  @Test
  @CaseIds("TC-120")
  void purchaseThroughUiAppearsInHistoryAndReports() {
    CheckoutPage checkout = new CheckoutPage(driver, url);
    checkout.addProduct(p1.getId());
    checkout.requestCheckout();
    new LoginPage(driver, url).login("qa_user", "Test@1234").awaitPath("/order/checkout");
    checkout.placeOrder("12 Lê Lợi", "0901234567");
    assertEquals(1, orders.count());
    var order = orders.findAll().get(0);
    assertEquals(user.getUsername(), order.getAccount().getUsername());
    money("90000", orderService.total(order.getId()));
    assertTrue(driver.findElement(By.tagName("body")).getText().contains(p1.getName()));
    checkout.logout();
    new LoginPage(driver, url).login("qa_admin", "Test@1234").awaitPath("/admin/product/index");
    new AdminOrderPage(driver, url).complete(order.getId());
    assertEquals("COMPLETED", orders.findById(order.getId()).orElseThrow().getStatus());
    driver.get(url + "/admin/report/revenue");
    assertTrue(driver.findElement(By.cssSelector("tbody")).getText().contains(c1.getName()));
    money("90000", reports.revenue().get(0).get("revenue"));
    checkout.logout();
    new LoginPage(driver, url).login("qa_user", "Test@1234").awaitPath("/");
    driver.get(url + "/order/my-product-list");
    assertTrue(driver.findElement(By.cssSelector("tbody")).getText().contains(p1.getName()));
    assertEquals(1, orderService.purchased(user.getUsername()).size());
  }
}
