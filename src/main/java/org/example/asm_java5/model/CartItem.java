package org.example.asm_java5.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;
import lombok.Setter;
import org.example.asm_java5.entity.Product;

@Getter
@Setter
public class CartItem {
  private Product product;
  private int quantity;

  public BigDecimal getUnitPrice() {
    BigDecimal discount = product.getDiscount();

    if (discount == null) {
      discount = BigDecimal.ZERO;
    }

    BigDecimal remainingPercent = new BigDecimal("100").subtract(discount);

    return product
        .getPrice()
        .multiply(remainingPercent)
        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
  }

  public BigDecimal getAmount() {
    return getUnitPrice().multiply(BigDecimal.valueOf(quantity));
  }
}
