package org.example.asm_java5.support;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.asm_java5.entity.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class Fixtures {
  private Fixtures() {}

  public static Category category(int id, String name) {
    Category c = new Category();
    c.setId(id);
    c.setName(name);
    return c;
  }

  public static Product product(int id, String price, String discount) {
    Product p = new Product();
    p.setId(id);
    p.setName("Bình QA " + id);
    p.setPrice(new BigDecimal(price));
    p.setDiscount(new BigDecimal(discount));
    p.setAvailable(true);
    p.setCreateDate(LocalDate.now());
    p.setImage("vase.svg");
    p.setCategory(category(1, "Gốm QA"));
    return p;
  }

  public static Account account(String username, boolean admin) {
    Account a = new Account();
    a.setUsername(username);
    a.setFullName("Nguyễn QA");
    a.setEmail(username + "@example.invalid");
    a.setActivated(true);
    a.setAdmin(admin);
    a.setPassword(new BCryptPasswordEncoder().encode("Test@1234"));
    return a;
  }
}
