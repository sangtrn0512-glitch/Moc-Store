package org.example.asm_java5.service;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CartServiceTests {
  ProductService products;
  CartService cart;
  Product p;

  // Tạo giỏ hàng mới, ProductService giả và sản phẩm mẫu trước mỗi test.
  @BeforeEach
  void setup() {
    products = mock(ProductService.class);
    cart = new CartService(products);
    p = product(1, "100000", "10");
    when(products.findById(1)).thenReturn(Optional.of(p));
  }

  // Kiểm tra thêm sản phẩm đang bán vào giỏ.
  @Test
  @Tag("smoke")
  @CaseIds("TC-012")
  void addAvailableProduct() {
    cart.add(1);
    assertEquals(1, cart.getItems().size());
    assertEquals(1, cart.getItems().get(0).getQuantity());
    assertEquals(1, cart.getItems().get(0).getProduct().getId());
  }

  // Kiểm tra thêm trùng sản phẩm thì cộng dồn số lượng.
  @Test
  @CaseIds("TC-013")
  void mergeRepeatedProduct() {
    cart.add(1);
    cart.add(1);
    assertEquals(1, cart.getItems().size());
    assertEquals(2, cart.getItems().get(0).getQuantity());
  }

  // Kiểm tra tổng tiền tính đúng giảm giá và số lượng.
  @Test
  @CaseIds("TC-014")
  void totalUsesDiscountAndQuantity() {
    when(products.findById(2)).thenReturn(Optional.of(product(2, "200000", "0")));
    cart.add(1);
    cart.update(1, 2);
    cart.add(2);
    assertEquals(new BigDecimal("180000.00"), cart.getItems().get(0).getAmount());
    assertEquals(new BigDecimal("380000.00"), cart.getTotal());
  }

  // Kiểm tra chấp nhận số lượng ở hai giới hạn hợp lệ.
  @ParameterizedTest
  @ValueSource(ints = {1, 99})
  @CaseIds("TC-015")
  void quantityBoundaries(int quantity) {
    cart.add(1);
    cart.update(1, quantity);
    assertEquals(quantity, cart.getItems().get(0).getQuantity());
  }

  // Kiểm tra số lượng sai bị từ chối và không đổi giỏ hàng.
  @ParameterizedTest
  @ValueSource(ints = {0, -1, 100})
  @CaseIds("TC-016")
  void rejectInvalidQuantityWithoutMutation(int quantity) {
    cart.add(1);
    cart.update(1, 2);
    assertThrows(IllegalArgumentException.class, () -> cart.update(1, quantity));
    assertEquals(2, cart.getItems().get(0).getQuantity());
  }

  // Kiểm tra không cho tăng số lượng sản phẩm lên 100.
  @Test
  @CaseIds("TC-017")
  void rejectHundredthItem() {
    cart.add(1);
    cart.update(1, 99);
    assertThrows(IllegalArgumentException.class, () -> cart.add(1));
    assertEquals(99, cart.getItems().get(0).getQuantity());
  }

  // Kiểm tra không thêm sản phẩm không tồn tại hoặc ngừng bán.
  @Test
  @CaseIds("TC-018")
  void rejectMissingAndUnavailableProducts() {
    assertThrows(IllegalArgumentException.class, () -> cart.add(999));
    p.setAvailable(false);
    assertThrows(IllegalStateException.class, () -> cart.add(1));
    assertTrue(cart.getItems().isEmpty());
  }

  // Kiểm tra không cập nhật sản phẩm chưa có trong giỏ.
  @Test
  @CaseIds("TC-019")
  void rejectUpdateAbsentProduct() {
    assertThrows(IllegalArgumentException.class, () -> cart.update(2, 1));
    assertTrue(cart.getItems().isEmpty());
  }

  // Kiểm tra xóa đúng dòng được chọn và giữ các dòng còn lại.
  @Test
  @CaseIds("TC-020")
  void removeOnlySelectedLine() {
    when(products.findById(2)).thenReturn(Optional.of(product(2, "200000", "0")));
    cart.add(1);
    cart.add(2);
    cart.remove(1);
    assertEquals(1, cart.getItems().size());
    assertEquals(2, cart.getItems().get(0).getProduct().getId());
    assertEquals(new BigDecimal("200000.00"), cart.getTotal());
  }

  // Kiểm tra xóa giỏ nhiều lần vẫn giữ giỏ rỗng.
  @Test
  @CaseIds("TC-021")
  void clearIsIdempotent() {
    cart.add(1);
    cart.clear();
    cart.clear();
    assertTrue(cart.getItems().isEmpty());
    assertEquals(BigDecimal.ZERO, cart.getTotal());
  }

  // Kiểm tra làm tròn đơn giá trước khi nhân số lượng.
  @Test
  @CaseIds("TC-023")
  void roundUnitPriceBeforeMultiplication() {
    p.setPrice(new BigDecimal("10.05"));
    cart.add(1);
    cart.update(1, 3);
    assertEquals(new BigDecimal("9.05"), cart.getItems().get(0).getUnitPrice());
    assertEquals(new BigDecimal("27.15"), cart.getTotal());
  }
}
