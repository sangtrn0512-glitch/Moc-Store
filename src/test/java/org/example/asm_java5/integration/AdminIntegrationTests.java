package org.example.asm_java5.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.support.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AdminIntegrationTests extends DatabaseTestSupport {
  MockMvc mvc;

  // Tạo MockMvc từ cấu hình Spring để gửi request trước mỗi test.
  @BeforeEach
  void mvc() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  MockHttpSession session(Account a) {
    MockHttpSession s = new MockHttpSession();
    s.setAttribute("csrf", "token");
    if (a != null) {
      s.setAttribute("username", a.getUsername());
      s.setAttribute("authStamp", a.getPassword());
    }
    return s;
  }

  MockHttpServletRequestBuilder adminPost(String url) {
    return post(url).servletPath(url).session(session(admin)).param("csrf", "token");
  }

  MockHttpServletRequestBuilder productForm(String url) {
    return adminPost(url)
        .param("name", "Bình mới")
        .param("price", "100000")
        .param("discount", "10")
        .param("image", "vase.svg")
        .param("available", "true")
        .param("categoryId", c1.getId().toString());
  }

  MockHttpServletRequestBuilder accountForm(String url, String username) {
    return adminPost(url)
        .param("username", username)
        .param("fullName", "Nguyễn QA sửa")
        .param("email", username + "@example.invalid")
        .param("activated", "true")
        .param("admin", "false");
  }

  // Kiểm tra admin tạo sản phẩm và đọc lại dữ liệu đã lưu.
  @Test
  @CaseIds("TC-078")
  void productCreateAndRead() throws Exception {
    mvc.perform(productForm("/admin/product/create")).andExpect(flash().attributeExists("success"));
    Product saved =
        products.findAll().stream()
            .filter(p -> p.getName().equals("Bình mới"))
            .findFirst()
            .orElseThrow();
    assertTrue(saved.isAvailable());
    money("100000", saved.getPrice());
    assertNotNull(saved.getCreateDate());
    mvc.perform(get("/admin/product/edit/" + saved.getId()).session(session(admin)))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("product"));
  }

  // Kiểm tra sửa sản phẩm giữ nguyên mã và ngày tạo.
  @Test
  @CaseIds("TC-079")
  void productUpdateKeepsIdAndCreationDate() throws Exception {
    mvc.perform(productForm("/admin/product/update").param("id", p1.getId().toString()))
        .andExpect(flash().attributeExists("success"));
    var saved = products.findById(p1.getId()).orElseThrow();
    assertEquals("Bình mới", saved.getName());
    assertEquals(p1.getCreateDate(), saved.getCreateDate());
    assertEquals(2, products.count());
  }

  // Kiểm tra sản phẩm ngừng bán không được mua hoặc hiển thị trong danh sách bán.
  @Test
  @CaseIds("TC-086")
  void unavailableProductCannotBePurchasedOrListed() throws Exception {
    p1.setAvailable(false);
    products.save(p1);
    MockHttpSession s = session(user);
    mvc.perform(post("/cart/add/" + p1.getId()).session(s).param("csrf", "token"))
        .andExpect(flash().attributeExists("error"));
    var listed =
        (List<?>)
            mvc.perform(get("/product/list"))
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("products");
    assertEquals(1, listed.size());
    assertEquals(p2.getId(), ((Product) listed.get(0)).getId());
  }

  // Kiểm tra xóa sản phẩm chưa được tham chiếu.
  @Test
  @CaseIds("TC-087")
  void deleteUnreferencedProduct() throws Exception {
    mvc.perform(adminPost("/admin/product/delete/" + p1.getId()))
        .andExpect(flash().attributeExists("success"));
    assertFalse(products.existsById(p1.getId()));
    assertTrue(products.existsById(p2.getId()));
  }

  // Kiểm tra chuyển hướng khi mã cần chỉnh sửa không tồn tại.
  @Test
  @CaseIds({"TC-077", "TC-089"})
  void missingEditIdsRedirect() throws Exception {
    mvc.perform(get("/admin/category/edit/999999").session(session(admin)))
        .andExpect(redirectedUrl("/admin/category/index"));
    mvc.perform(get("/admin/product/edit/999999").session(session(admin)))
        .andExpect(redirectedUrl("/admin/product/index"));
  }

  // Kiểm tra admin tạo tài khoản đã kích hoạt với mật khẩu mã hóa.
  @Test
  @CaseIds("TC-090")
  void adminCreatesActiveUserWithHashedPassword() throws Exception {
    mvc.perform(accountForm("/admin/account/create", "qa_staff").param("password", "Test@1234"))
        .andExpect(flash().attributeExists("success"));
    var a = accounts.findById("qa_staff").orElseThrow();
    assertTrue(a.isActivated());
    assertFalse(a.isAdmin());
    assertTrue(new BCryptPasswordEncoder().matches("Test@1234", a.getPassword()));
    mvc.perform(get("/admin/account/edit/qa_staff").session(session(admin)))
        .andExpect(status().isOk());
  }

  // Kiểm tra admin sửa tài khoản không nhập mật khẩu thì giữ mật khẩu cũ.
  @Test
  @CaseIds("TC-091")
  void adminUpdateWithoutPasswordKeepsHash() throws Exception {
    String hash = user.getPassword();
    mvc.perform(accountForm("/admin/account/update", user.getUsername()).param("password", ""))
        .andExpect(flash().attributeExists("success"));
    var a = accounts.findById(user.getUsername()).orElseThrow();
    assertEquals(hash, a.getPassword());
    assertEquals("Nguyễn QA sửa", a.getFullName());
  }

  // Kiểm tra admin đổi mật khẩu tài khoản thành công.
  @Test
  @CaseIds("TC-092")
  void adminChangesPassword() throws Exception {
    mvc.perform(
            accountForm("/admin/account/update", user.getUsername()).param("password", "New@12345"))
        .andExpect(flash().attributeExists("success"));
    String hash = accounts.findById(user.getUsername()).orElseThrow().getPassword();
    assertTrue(new BCryptPasswordEncoder().matches("New@12345", hash));
    assertFalse(new BCryptPasswordEncoder().matches("Test@1234", hash));
  }

  // Kiểm tra admin tạo tài khoản sai dữ liệu thì không lưu.
  @Test
  @CaseIds("TC-093")
  void badAdminAccountCreationDoesNotPersist() throws Exception {
    long before = accounts.count();
    mvc.perform(accountForm("/admin/account/create", "qa_bad").param("password", "1234567"))
        .andExpect(flash().attributeExists("error"));
    assertEquals(before, accounts.count());
  }

  // Kiểm tra admin không được tự hạ quyền.
  @Test
  @CaseIds("TC-094")
  void adminCannotDemoteSelf() throws Exception {
    mvc.perform(accountForm("/admin/account/update", admin.getUsername()))
        .andExpect(flash().attributeExists("error"));
    assertTrue(accounts.findById(admin.getUsername()).orElseThrow().isAdmin());
  }

  // Kiểm tra admin không được tự xóa tài khoản.
  @Test
  @CaseIds("TC-095")
  void adminCannotDeleteSelf() throws Exception {
    mvc.perform(adminPost("/admin/account/delete/" + admin.getUsername()))
        .andExpect(flash().attributeExists("error"));
    assertTrue(accounts.existsById(admin.getUsername()));
  }

  // Kiểm tra xóa tài khoản chưa có đơn hàng.
  @Test
  @CaseIds("TC-096")
  void deleteUserWithoutOrders() throws Exception {
    mvc.perform(adminPost("/admin/account/delete/" + other.getUsername()))
        .andExpect(flash().attributeExists("success"));
    assertFalse(accounts.existsById(other.getUsername()));
  }

  // Kiểm tra admin tạo đơn và xem đúng chi tiết đơn.
  @Test
  @CaseIds("TC-099")
  void adminCreatesOrderAndReadsDetails() throws Exception {
    mvc.perform(
            adminPost("/admin/order/create")
                .param("username", user.getUsername())
                .param("productId", p1.getId().toString())
                .param("quantity", "2")
                .param("address", "12 Lê Lợi")
                .param("phone", "0901234567"))
        .andExpect(flash().attributeExists("success"));
    assertEquals(1, orders.count());
    assertEquals(1, details.count());
    var o = orders.findAll().get(0);
    assertEquals("PENDING", o.getStatus());
    money("180000", orderService.total(o.getId()));
    mvc.perform(get("/admin/order/edit/" + o.getId()).session(session(admin)))
        .andExpect(status().isOk());
  }

  // Kiểm tra admin tạo đơn với số lượng sai thì hoàn tác dữ liệu.
  @ParameterizedTest
  @ValueSource(ints = {0, 100})
  @CaseIds("TC-100")
  void adminInvalidOrderRollsBack(int quantity) throws Exception {
    mvc.perform(
            adminPost("/admin/order/create")
                .param("username", user.getUsername())
                .param("productId", p1.getId().toString())
                .param("quantity", String.valueOf(quantity))
                .param("address", "12 Lê Lợi")
                .param("phone", "0901234567"))
        .andExpect(flash().attributeExists("error"));
    assertEquals(0, orders.count());
    assertEquals(0, details.count());
  }

  // Kiểm tra admin sửa đúng địa chỉ và trạng thái đơn.
  @Test
  @CaseIds("TC-101")
  void adminUpdatesOrderAddressAndStatus() throws Exception {
    Order o = orderService.checkout(user, List.of(item(p1, 1)), "12 Lê Lợi", "0901234567");
    mvc.perform(
            adminPost("/admin/order/update")
                .param("id", o.getId().toString())
                .param("address", "34 Nguyễn Huệ")
                .param("phone", "0912345678")
                .param("status", "CONFIRMED"))
        .andExpect(flash().attributeExists("success"));
    var saved = orders.findById(o.getId()).orElseThrow();
    assertEquals("34 Nguyễn Huệ", saved.getAddress());
    assertEquals("CONFIRMED", saved.getStatus());
    assertEquals("0912345678", saved.getPhone());
    money("90000", orderService.total(o.getId()));
  }

  // Kiểm tra khóa sửa dòng hàng khi đơn không còn chờ xử lý.
  @ParameterizedTest
  @ValueSource(strings = {"CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED"})
  @CaseIds("TC-105")
  void nonPendingOrderLinesAreLocked(String status) {
    Order o =
        orderService.checkout(user, List.of(item(p1, 1), item(p2, 1)), "12 Lê Lợi", "0901234567");
    orderService.update(o.getId(), o.getAddress(), o.getPhone(), status);
    Long detailId = orderService.details(o.getId()).get(0).getId();
    assertThrows(
        IllegalArgumentException.class, () -> orderService.saveItem(o.getId(), p1.getId(), 2));
    assertThrows(
        IllegalArgumentException.class, () -> orderService.removeItem(o.getId(), detailId));
    assertEquals(2, details.count());
  }

  // Kiểm tra dòng hàng từ chối số lượng sai hoặc sản phẩm ngừng bán.
  @Test
  @CaseIds("TC-106")
  void orderLinesRejectInvalidQuantityAndUnavailableProduct() {
    Order o = orderService.checkout(user, List.of(item(p1, 1)), "12 Lê Lợi", "0901234567");
    assertThrows(
        IllegalArgumentException.class, () -> orderService.saveItem(o.getId(), p2.getId(), 0));
    assertThrows(
        IllegalArgumentException.class, () -> orderService.saveItem(o.getId(), p2.getId(), 100));
    p2.setAvailable(false);
    products.save(p2);
    assertThrows(
        IllegalArgumentException.class, () -> orderService.saveItem(o.getId(), p2.getId(), 1));
    assertEquals(1, details.count());
  }

  // Kiểm tra không xóa nhầm dòng của đơn khác và xóa được dòng đúng đơn.
  @Test
  @CaseIds({"TC-107", "TC-108"})
  void removeLineRejectsOtherOrderThenRemovesOwnLine() {
    Order a =
        orderService.checkout(user, List.of(item(p1, 1), item(p2, 1)), "12 Lê Lợi", "0901234567");
    Order b = orderService.checkout(other, List.of(item(p1, 1)), "12 Lê Lợi", "0901234567");
    Long foreign = orderService.details(b.getId()).get(0).getId();
    assertThrows(IllegalArgumentException.class, () -> orderService.removeItem(a.getId(), foreign));
    assertEquals(3, details.count());
    Long own = orderService.details(a.getId()).get(0).getId();
    orderService.removeItem(a.getId(), own);
    assertEquals(1, orderService.details(a.getId()).size());
    assertTrue(details.existsById(foreign));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "/account/edit-profile",
        "/account/change-password",
        "/order/checkout",
        "/order/list",
        "/order/detail/1",
        "/order/my-product-list"
      })
  @CaseIds("TC-048")
  void protectedPagesRequireLogin(String path) throws Exception {
    mvc.perform(get(path)).andExpect(redirectedUrl("/auth/login"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "/admin/category/index",
        "/admin/product/index",
        "/admin/account/index",
        "/admin/order/index",
        "/admin/report/revenue",
        "/admin/report/vip"
      })
  @CaseIds({"TC-050", "TC-051"})
  void allAdminPagesRejectGuestAndUser(String path) throws Exception {
    mvc.perform(get(path)).andExpect(redirectedUrl("/auth/login"));
    mvc.perform(get(path).session(session(user))).andExpect(status().isForbidden());
  }

  // Kiểm tra request GET không được xóa sản phẩm hoặc xóa giỏ.
  @Test
  @CaseIds("TC-056")
  void getCannotDeleteProductOrClearCart() throws Exception {
    mvc.perform(get("/admin/product/delete/" + p1.getId()).session(session(admin)))
        .andExpect(status().isMethodNotAllowed());
    mvc.perform(get("/cart/clear")).andExpect(status().isMethodNotAllowed());
    assertTrue(products.existsById(p1.getId()));
  }

  // Kiểm tra tìm kiếm không khớp hiển thị danh sách rỗng.
  @Test
  @CaseIds("TC-008")
  void emptySearchRendersEmptyResults() throws Exception {
    mvc.perform(get("/product/list").param("categoryId", "999999"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("products", org.hamcrest.Matchers.empty()));
  }

  // Kiểm tra tạo danh mục từ chối tên dài hơn 100 ký tự.
  @Test
  @CaseIds("TC-074")
  void categoryCreateRejectsNameOverOneHundred() throws Exception {
    long before = categories.count();
    mvc.perform(adminPost("/admin/category/create").param("name", "a".repeat(101)))
        .andExpect(status().isBadRequest());
    assertEquals(before, categories.count(), "Creating must enforce the same 100-character limit as updating");
  }
}
