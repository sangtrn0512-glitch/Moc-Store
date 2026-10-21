package org.example.asm_java5.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.model.CartItem;
import org.example.asm_java5.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
  private final OrderRepository orders;
  private final OrderDetailRepository details;
  private final ProductRepository products;

  public OrderService(
      OrderRepository orders, OrderDetailRepository details, ProductRepository products) {
    this.orders = orders;
    this.details = details;
    this.products = products;
  }

  public List<Order> findAll() {
    return orders.findAll();
  }

  public Optional<Order> findById(Long id) {
    return orders.findById(id);
  }

  public List<Order> mine(String username) {
    return orders.findByAccount_UsernameOrderByIdDesc(username);
  }

  public List<OrderDetail> details(Long id) {
    return details.findByOrder_Id(id);
  }

  public List<OrderDetail> purchased(String username) {
    return details.findByOrder_Account_UsernameAndOrder_Status(username, "COMPLETED");
  }

  public BigDecimal total(Long id) {
    BigDecimal total = BigDecimal.ZERO;
    for (OrderDetail item : details(id))
      total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
    return total;
  }

  public void validateAddress(String address, String phone) {
    if (address == null || address.isBlank() || address.length() > 500)
      throw new IllegalArgumentException("Địa chỉ cần có từ 1 đến 500 ký tự");
    if (phone == null || !phone.matches("[+0-9 ()-]{8,20}"))
      throw new IllegalArgumentException("Số điện thoại không hợp lệ");
  }

  @Transactional
  public Order checkout(Account account, List<CartItem> cart, String address, String phone) {
    validateAddress(address, phone);
    if (cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng trống");
    Order order = new Order();
    order.setAccount(account);
    order.setAddress(address.trim());
    order.setPhone(phone.trim());
    order.setCreateDate(LocalDateTime.now());
    order.setStatus("PENDING");
    orders.save(order);
    for (CartItem item : cart) {
      Product product = products.findById(item.getProduct().getId()).orElse(null);
      if (product == null || !product.isAvailable())
        throw new IllegalArgumentException("Có sản phẩm không còn bán, hãy kiểm tra lại giỏ");
      if (item.getQuantity() < 1 || item.getQuantity() > 99)
        throw new IllegalArgumentException("Số lượng không hợp lệ");
      CartItem fresh = new CartItem();
      fresh.setProduct(product);
      fresh.setQuantity(item.getQuantity());
      OrderDetail detail = new OrderDetail();
      detail.setOrder(order);
      detail.setProduct(product);
      detail.setQuantity(item.getQuantity());
      detail.setPrice(fresh.getUnitPrice());
      details.save(detail);
    }
    return order;
  }

  @Transactional
  public void update(Long id, String address, String phone, String status) {
    validateAddress(address, phone);
    if (!List.of("PENDING", "CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED").contains(status))
      throw new IllegalArgumentException("Trạng thái không hợp lệ");
    Order order =
        orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn"));
    if (List.of("COMPLETED", "CANCELLED").contains(order.getStatus())
        && !order.getStatus().equals(status))
      throw new IllegalArgumentException("Đơn đã kết thúc không thể đổi trạng thái");
    order.setAddress(address.trim());
    order.setPhone(phone.trim());
    order.setStatus(status);
    orders.save(order);
  }

  @Transactional
  public void deleteById(Long id) {
    Order order =
        orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn"));
    if (!List.of("PENDING", "CANCELLED").contains(order.getStatus()))
      throw new IllegalArgumentException("Chỉ xóa đơn chờ xác nhận hoặc đã hủy");
    details.deleteAll(details.findByOrder_Id(id));
    orders.delete(order);
  }

  @Transactional
  public void saveItem(Long orderId, Integer productId, int quantity) {
    Order order =
        orders
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn"));
    if (!"PENDING".equals(order.getStatus()))
      throw new IllegalArgumentException("Chỉ sửa sản phẩm trong đơn chờ xác nhận");
    Product product =
        products
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));
    if (!product.isAvailable() || quantity < 1 || quantity > 99)
      throw new IllegalArgumentException("Sản phẩm ngừng bán hoặc số lượng không hợp lệ");
    OrderDetail detail = null;
    for (OrderDetail item : details(orderId))
      if (item.getProduct().getId().equals(productId)) detail = item;
    if (detail == null) {
      detail = new OrderDetail();
      detail.setOrder(order);
      detail.setProduct(product);
      CartItem item = new CartItem();
      item.setProduct(product);
      item.setQuantity(1);
      detail.setPrice(item.getUnitPrice());
    }
    detail.setQuantity(quantity);
    details.save(detail);
  }

  @Transactional
  public void removeItem(Long orderId, Long detailId) {
    Order order =
        orders
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn"));
    if (!"PENDING".equals(order.getStatus()))
      throw new IllegalArgumentException("Chỉ sửa đơn chờ xác nhận");
    if (details(orderId).size() < 2)
      throw new IllegalArgumentException("Đơn cần ít nhất một sản phẩm");
    OrderDetail detail =
        details
            .findById(detailId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dòng hàng"));
    if (!detail.getOrder().getId().equals(orderId))
      throw new IllegalArgumentException("Dòng hàng không thuộc đơn");
    details.delete(detail);
  }
}
