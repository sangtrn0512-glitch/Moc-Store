package org.example.asm_java5.service;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.model.CartItem;
import org.example.asm_java5.repository.*;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

class OrderServiceTests {
  OrderRepository orders;
  OrderDetailRepository details;
  ProductRepository products;
  OrderService service;

  @BeforeEach
  void setup() {
    orders = mock(OrderRepository.class);
    details = mock(OrderDetailRepository.class);
    products = mock(ProductRepository.class);
    service = new OrderService(orders, details, products);
  }

  @Test
  @CaseIds("TC-058")
  void emptyCartCannotCreateOrder() {
    assertThrows(
        IllegalArgumentException.class,
        () -> service.checkout(account("qa", false), List.of(), "12 Lê Lợi", "0901234567"));
    verify(orders, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 500})
  @CaseIds("TC-059")
  void addressBoundaries(int length) {
    assertDoesNotThrow(() -> service.validateAddress("a".repeat(length), "0901234567"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @CaseIds("TC-060")
  void blankAddress(String address) {
    assertThrows(
        IllegalArgumentException.class, () -> service.validateAddress(address, "0901234567"));
  }

  @Test
  @CaseIds("TC-060")
  void tooLongAddress() {
    assertThrows(
        IllegalArgumentException.class,
        () -> service.validateAddress("a".repeat(501), "0901234567"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"12345678", "12345678901234567890", "+84 (90)-1234567"})
  @CaseIds("TC-061")
  void validPhones(String phone) {
    assertDoesNotThrow(() -> service.validateAddress("12 Lê Lợi", phone));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1234567", "123456789012345678901", "090abc1234"})
  @CaseIds("TC-062")
  void invalidPhones(String phone) {
    assertThrows(IllegalArgumentException.class, () -> service.validateAddress("12 Lê Lợi", phone));
  }

  @Test
  @CaseIds("TC-063")
  void checkoutRefreshesPriceFromDatabase() {
    CartItem cart = new CartItem();
    cart.setProduct(product(1, "100000", "10"));
    cart.setQuantity(2);
    when(products.findById(1)).thenReturn(Optional.of(product(1, "120000", "10")));
    service.checkout(account("qa", false), List.of(cart), "12 Lê Lợi", "0901234567");
    ArgumentCaptor<OrderDetail> saved = ArgumentCaptor.forClass(OrderDetail.class);
    verify(details).save(saved.capture());
    assertEquals(new BigDecimal("108000.00"), saved.getValue().getPrice());
    assertEquals(2, saved.getValue().getQuantity());
  }

  @Test
  @CaseIds("TC-102")
  void rejectUnknownStatus() {
    assertThrows(
        IllegalArgumentException.class,
        () -> service.update(1L, "12 Lê Lợi", "0901234567", "UNKNOWN"));
    verify(orders, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"COMPLETED", "CANCELLED"})
  @CaseIds("TC-103")
  void terminalStatusCannotBeReopened(String status) {
    Order o = new Order();
    o.setStatus(status);
    when(orders.findById(1L)).thenReturn(Optional.of(o));
    assertThrows(
        IllegalArgumentException.class,
        () -> service.update(1L, "12 Lê Lợi", "0901234567", "PENDING"));
    assertEquals(status, o.getStatus());
    verify(orders, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"CONFIRMED", "SHIPPING", "COMPLETED"})
  @CaseIds("TC-110")
  void cannotDeleteProcessingOrCompletedOrder(String status) {
    Order o = new Order();
    o.setStatus(status);
    when(orders.findById(1L)).thenReturn(Optional.of(o));
    assertThrows(IllegalArgumentException.class, () -> service.deleteById(1L));
    verify(orders, never()).delete(any());
  }

  @Test
  @CaseIds("TC-108")
  void cannotRemoveFinalLine() {
    Order o = new Order();
    o.setStatus("PENDING");
    when(orders.findById(1L)).thenReturn(Optional.of(o));
    when(details.findByOrder_Id(1L)).thenReturn(List.of(new OrderDetail()));
    assertThrows(IllegalArgumentException.class, () -> service.removeItem(1L, 7L));
    verify(details, never()).delete(any());
  }
}
