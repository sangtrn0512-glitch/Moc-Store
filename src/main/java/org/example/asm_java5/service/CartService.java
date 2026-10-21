package org.example.asm_java5.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class CartService {
  private final ProductService productService;

  public CartService(ProductService productService) {
    this.productService = productService;
  }

  private final List<CartItem> items = new ArrayList<>();

  public void add(Integer productId) {
    Product product = productService.findById(productId).orElse(null);

    if (product == null) {
      throw new IllegalArgumentException("Không tìm thấy sản phẩm");
    }

    if (!product.isAvailable()) {
      throw new IllegalStateException("Sản phẩm đã ngừng bán");
    }

    for (CartItem item : items) {
      if (item.getProduct().getId().equals(productId)) {
        if (item.getQuantity() >= 99) {
          throw new IllegalArgumentException("Mỗi sản phẩm chỉ được có tối đa 99 món trong giỏ");
        }
        item.setProduct(product);
        item.setQuantity(item.getQuantity() + 1);
        return;
      }
    }

    CartItem newItem = new CartItem();
    newItem.setProduct(product);
    newItem.setQuantity(1);
    items.add(newItem);
  }

  public void update(Integer productId, int quantity) {
    if (quantity < 1 || quantity > 99) {
      throw new IllegalArgumentException("Số lượng phải từ 1 đến 99");
    }

    for (CartItem item : items) {
      if (item.getProduct().getId().equals(productId)) {
        item.setQuantity(quantity);
        return;
      }
    }

    throw new IllegalArgumentException("Sản phẩm không có trong giỏ");
  }

  public void remove(Integer productId) {
    for (int i = 0; i < items.size(); i++) {
      CartItem item = items.get(i);

      if (item.getProduct().getId().equals(productId)) {
        items.remove(i);
        return;
      }
    }
  }

  public void clear() {
    items.clear();
  }

  public List<CartItem> getItems() {
    return items;
  }

  public BigDecimal getTotal() {
    BigDecimal total = BigDecimal.ZERO;

    for (CartItem item : items) {
      total = total.add(item.getAmount());
    }

    return total;
  }
}
