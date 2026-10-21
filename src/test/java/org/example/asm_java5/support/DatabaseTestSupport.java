package org.example.asm_java5.support;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.model.CartItem;
import org.example.asm_java5.repository.*;
import org.example.asm_java5.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class DatabaseTestSupport {
  @Autowired protected AccountRepository accounts;
  @Autowired protected CategoryRepository categories;
  @Autowired protected ProductRepository products;
  @Autowired protected OrderRepository orders;
  @Autowired protected OrderDetailRepository details;
  @Autowired protected OrderService orderService;
  @Autowired protected ReportService reports;
  @Autowired protected AccountService accountService;
  @Autowired protected ProductService productService;
  @Autowired protected CategoryService categoryService;
  @Autowired protected WebApplicationContext context;
  @Autowired DataSource dataSource;
  @MockitoBean protected MailService mail;
  @LocalServerPort protected int port;
  protected Account user, other, admin;
  protected Category c1, c2;
  protected Product p1, p2;

  @BeforeEach
  public void resetTestDatabase() throws Exception {
    try (var connection = dataSource.getConnection()) {
      assertTrue(
          connection.getMetaData().getURL().startsWith("jdbc:h2:mem:"),
          "Refuse fixture cleanup outside in-memory H2");
    }
    details.deleteAll();
    orders.deleteAll();
    products.deleteAll();
    categories.deleteAll();
    accounts.deleteAll();
    user = accounts.save(Fixtures.account("qa_user", false));
    other = accounts.save(Fixtures.account("qa_other", false));
    admin = accounts.save(Fixtures.account("qa_admin", true));
    c1 = Fixtures.category(1, "Gốm QA");
    c1.setId(null);
    c1 = categories.save(c1);
    c2 = Fixtures.category(2, "Đèn QA");
    c2.setId(null);
    c2 = categories.save(c2);
    p1 = saveProduct("Bình QA", "100000", "10", c1);
    p2 = saveProduct("Đèn QA", "200000", "0", c1);
  }

  protected Product saveProduct(String name, String price, String discount, Category category) {
    Product p = Fixtures.product(1, price, discount);
    p.setId(null);
    p.setName(name);
    p.setCategory(category);
    return products.save(p);
  }

  protected CartItem item(Product p, int quantity) {
    CartItem item = new CartItem();
    item.setProduct(p);
    item.setQuantity(quantity);
    return item;
  }

  protected Order completed(Account owner, Product p, int quantity, LocalDateTime when) {
    Order o = new Order();
    o.setAccount(owner);
    o.setStatus("COMPLETED");
    o.setAddress("12 Lê Lợi");
    o.setPhone("0901234567");
    o.setCreateDate(when);
    o = orders.save(o);
    OrderDetail d = new OrderDetail();
    d.setOrder(o);
    d.setProduct(p);
    d.setQuantity(quantity);
    d.setPrice(item(p, 1).getUnitPrice());
    details.save(d);
    return o;
  }

  protected void money(String expected, Object actual) {
    assertEquals(0, new BigDecimal(expected).compareTo(new BigDecimal(actual.toString())));
  }
}
