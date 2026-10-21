package org.example.asm_java5.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.support.*;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BusinessIntegrationTests extends DatabaseTestSupport {
  MockMvc mvc;

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

  @Test
  @Tag("smoke")
  @CaseIds("TC-057")
  void checkoutPersistsHeaderLinesAndTotal() {
    Order o =
        orderService.checkout(user, List.of(item(p1, 2), item(p2, 1)), " 12 Lê Lợi ", "0901234567");
    assertEquals(1, orders.count());
    assertEquals(2, details.count());
    assertEquals("PENDING", orders.findById(o.getId()).orElseThrow().getStatus());
    assertEquals(
        user.getUsername(), orders.findById(o.getId()).orElseThrow().getAccount().getUsername());
    assertEquals("12 Lê Lợi", o.getAddress());
    money("380000", orderService.total(o.getId()));
  }

  @Test
  @CaseIds("TC-064")
  void checkoutRollsBackHeaderAndEarlierLines() {
    p2.setAvailable(false);
    products.save(p2);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            orderService.checkout(
                user, List.of(item(p1, 2), item(p2, 1)), "12 Lê Lợi", "0901234567"));
    assertEquals(0, orders.count());
    assertEquals(0, details.count());
  }

  @Test
  @CaseIds("TC-065")
  void checkoutKeyCanOnlyCreateOneOrder() throws Exception {
    MockHttpSession s = session(user);
    mvc.perform(post("/cart/add/" + p1.getId()).session(s).param("csrf", "token"))
        .andExpect(status().is3xxRedirection());
    mvc.perform(get("/order/checkout").session(s)).andExpect(status().isOk());
    String key = (String) s.getAttribute("checkoutKey");
    assertNotNull(key);
    for (int i = 0; i < 2; i++)
      mvc.perform(
              post("/order/checkout")
                  .session(s)
                  .param("csrf", "token")
                  .param("checkoutKey", key)
                  .param("address", "12 Lê Lợi")
                  .param("phone", "0901234567"))
          .andExpect(status().is3xxRedirection());
    assertEquals(1, orders.count());
    assertEquals(1, details.count());
    mvc.perform(get("/cart/index").session(s))
        .andExpect(model().attribute("items", org.hamcrest.Matchers.empty()));
  }

  @Test
  @CaseIds("TC-066")
  void wrongCheckoutKeyDoesNotLoseCart() throws Exception {
    MockHttpSession s = session(user);
    mvc.perform(post("/cart/add/" + p1.getId()).session(s).param("csrf", "token"));
    mvc.perform(
            post("/order/checkout")
                .session(s)
                .param("csrf", "token")
                .param("checkoutKey", "wrong")
                .param("address", "12 Lê Lợi")
                .param("phone", "0901234567"))
        .andExpect(redirectedUrl("/order/checkout"))
        .andExpect(flash().attributeExists("error"));
    assertEquals(0, orders.count());
    mvc.perform(get("/cart/index").session(s))
        .andExpect(model().attribute("items", org.hamcrest.Matchers.hasSize(1)));
  }

  @Test
  @CaseIds("TC-022")
  void cartIsActuallySessionScoped() throws Exception {
    MockHttpSession a = session(null), b = session(null);
    mvc.perform(post("/cart/add/" + p1.getId()).session(a).param("csrf", "token"));
    mvc.perform(post("/cart/add/" + p2.getId()).session(b).param("csrf", "token"));
    var itemsA =
        (List<?>)
            mvc.perform(get("/cart/index").session(a))
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("items");
    var itemsB =
        (List<?>)
            mvc.perform(get("/cart/index").session(b))
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("items");
    assertEquals(
        p1.getId(), ((org.example.asm_java5.model.CartItem) itemsA.get(0)).getProduct().getId());
    assertEquals(
        p2.getId(), ((org.example.asm_java5.model.CartItem) itemsB.get(0)).getProduct().getId());
  }

  @Test
  @CaseIds("TC-055")
  void orderOwnershipAndMissingId() throws Exception {
    Order o = completed(other, p1, 1, LocalDateTime.now());
    mvc.perform(get("/order/detail/" + o.getId()).session(session(user)))
        .andExpect(status().isForbidden());
    mvc.perform(get("/order/detail/999999").session(session(user)))
        .andExpect(status().isNotFound());
  }

  @Test
  @CaseIds({"TC-067", "TC-068"})
  void historiesOnlyContainOwnersAndCompletedPurchases() {
    completed(user, p1, 1, LocalDateTime.now());
    completed(other, p2, 1, LocalDateTime.now());
    orderService.checkout(user, List.of(item(p2, 1)), "12 Lê Lợi", "0901234567");
    assertEquals(2, orderService.mine(user.getUsername()).size());
    var purchased = orderService.purchased(user.getUsername());
    assertEquals(1, purchased.size());
    assertEquals(p1.getId(), purchased.get(0).getProduct().getId());
  }

  @Test
  @CaseIds("TC-069")
  void orderPricesRemainHistorical() {
    Order o = orderService.checkout(user, List.of(item(p1, 2)), "12 Lê Lợi", "0901234567");
    p1.setPrice(new BigDecimal("999999"));
    products.save(p1);
    money("180000", orderService.total(o.getId()));
  }

  @Test
  @CaseIds({"TC-111", "TC-112", "TC-113"})
  void revenueAggregatesOnlyCompletedByCategory() {
    completed(user, p1, 2, LocalDateTime.now());
    completed(user, p2, 1, LocalDateTime.now());
    Product p3 = saveProduct("C2", "500000", "0", c2);
    completed(other, p3, 1, LocalDateTime.now());
    orderService.checkout(user, List.of(item(p1, 99)), "12 Lê Lợi", "0901234567");
    var rows = reports.revenue();
    assertEquals(2, rows.size());
    assertEquals(c2.getName(), rows.get(0).get("category"));
    var row = rows.get(1);
    money("380000", row.get("revenue"));
    money("3", row.get("quantity"));
    money("200000", row.get("highest"));
    money("90000", row.get("lowest"));
    assertTrue(
        new BigDecimal(row.get("average").toString())
                .subtract(new BigDecimal("126666.6666667"))
                .abs()
                .compareTo(new BigDecimal("0.01"))
            < 0);
  }

  @Test
  @CaseIds("TC-114")
  void vipReturnsExactlyTopTen() {
    for (int i = 1; i <= 11; i++) {
      Account a = accounts.save(Fixtures.account("vip" + String.format("%02d", i), false));
      completed(a, p1, i, LocalDateTime.now());
    }
    var rows = reports.vip();
    assertEquals(10, rows.size());
    assertEquals("vip11", rows.get(0).get("username"));
    assertEquals("vip02", rows.get(9).get("username"));
  }

  @Test
  @CaseIds("TC-115")
  void vipTotalAndDatesIgnorePending() {
    LocalDateTime first = LocalDateTime.of(2026, 10, 1, 10, 0),
        last = LocalDateTime.of(2026, 10, 20, 10, 0);
    completed(user, p1, 2, first);
    completed(user, p2, 1, last);
    orderService.checkout(user, List.of(item(p2, 99)), "12 Lê Lợi", "0901234567");
    var row = reports.vip().get(0);
    money("380000", row.get("total"));
    assertEquals(first, ((java.sql.Timestamp) row.get("firstDate")).toLocalDateTime());
    assertEquals(last, ((java.sql.Timestamp) row.get("lastDate")).toLocalDateTime());
  }

  @Test
  @CaseIds("TC-116")
  void vipTieBreaksByUsernameAndDoesNotPadRows() {
    completed(user, p1, 1, LocalDateTime.now());
    completed(other, p1, 1, LocalDateTime.now());
    var rows = reports.vip();
    assertEquals(2, rows.size());
    assertEquals("qa_other", rows.get(0).get("username"));
  }

  @Test
  @CaseIds("TC-117")
  void reportsEmptyWithoutCompletedOrders() {
    assertTrue(reports.revenue().isEmpty());
    assertTrue(reports.vip().isEmpty());
  }

  @Test
  @CaseIds("TC-003")
  void bestSellersIgnorePendingAndUnavailable() {
    completed(user, p1, 2, LocalDateTime.now());
    completed(user, p2, 3, LocalDateTime.now());
    Product off = saveProduct("Off", "1", "0", c1);
    completed(user, off, 99, LocalDateTime.now());
    off.setAvailable(false);
    products.save(off);
    orderService.checkout(user, List.of(item(p1, 99)), "12 Lê Lợi", "0901234567");
    assertEquals(
        List.of(p2.getId(), p1.getId()),
        productService.findBestSellingProducts().stream().map(Product::getId).toList());
  }

  @Test
  @CaseIds({"TC-005", "TC-006"})
  void priceFilterIncludesBothBoundaries() {
    saveProduct("Outside", "300000", "0", c1);
    assertEquals(
        2,
        productService
            .findByPriceBetween(new BigDecimal("100000"), new BigDecimal("200000"))
            .size());
    assertEquals(
        1,
        productService
            .findByPriceBetween(new BigDecimal("200000"), new BigDecimal("200000"))
            .size());
  }

  @Test
  @CaseIds("TC-002")
  void discountTopFourExcludeUnavailableAndZero() {
    for (int i = 1; i <= 5; i++) saveProduct("Discount" + i, "100000", String.valueOf(i * 10), c1);
    Product off = saveProduct("Off", "100000", "99", c1);
    off.setAvailable(false);
    products.save(off);
    assertEquals(
        List.of(50, 40, 30, 20),
        productService.findDiscountProducts().stream()
            .map(p -> p.getDiscount().intValue())
            .toList());
  }

  @Test
  @CaseIds("TC-001")
  void newProductsUseDateThenId() {
    List<Integer> ids = new ArrayList<>();
    for (int i = 1; i <= 6; i++) {
      Product p = saveProduct("New" + i, "1", "0", c1);
      p.setCreateDate(LocalDate.now().plusDays(i));
      products.save(p);
      ids.add(p.getId());
    }
    Collections.reverse(ids);
    assertEquals(
        ids.subList(0, 4), productService.findNewProducts().stream().map(Product::getId).toList());
  }

  @Test
  @CaseIds({"TC-076", "TC-088", "TC-097"})
  void relatedRecordsCannotBeDeleted() throws Exception {
    completed(user, p1, 1, LocalDateTime.now());
    assertThrows(IllegalStateException.class, () -> categoryService.deleteById(c1.getId()));
    assertThrows(IllegalStateException.class, () -> productService.deleteById(p1.getId()));
    mvc.perform(
            post("/admin/account/delete/" + user.getUsername())
                .session(session(admin))
                .param("csrf", "token"))
        .andExpect(flash().attributeExists("error"));
    assertTrue(accounts.existsById(user.getUsername()));
    assertTrue(products.existsById(p1.getId()));
  }

  @Test
  @CaseIds("TC-109")
  void deletePendingAndCancelledRemovesDetails() {
    for (String status : List.of("PENDING", "CANCELLED")) {
      Order o = orderService.checkout(user, List.of(item(p1, 1)), "12 Lê Lợi", "0901234567");
      if (status.equals("CANCELLED"))
        orderService.update(o.getId(), o.getAddress(), o.getPhone(), status);
      orderService.deleteById(o.getId());
      assertFalse(orders.existsById(o.getId()));
    }
    assertEquals(0, details.count());
  }

  @Test
  @CaseIds("TC-104")
  void saveItemMergesProductLineAndRecalculatesTotal() {
    Order o = orderService.checkout(user, List.of(item(p1, 1)), "12 Lê Lợi", "0901234567");
    orderService.saveItem(o.getId(), p2.getId(), 2);
    orderService.saveItem(o.getId(), p2.getId(), 3);
    assertEquals(2, details.count());
    money("690000", orderService.total(o.getId()));
  }
}
