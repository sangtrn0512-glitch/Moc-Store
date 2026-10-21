package org.example.asm_java5.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.repository.OrderDetailRepository;
import org.example.asm_java5.repository.ProductRepository;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.Test;

class ProductServiceTests {
  @Test
  @CaseIds("TC-088")
  void cannotDeleteProductWithOrderHistory() {
    ProductRepository products = mock(ProductRepository.class);
    OrderDetailRepository details = mock(OrderDetailRepository.class);
    when(products.existsById(7)).thenReturn(true);
    when(details.existsByProduct_Id(7)).thenReturn(true);
    ProductService service = new ProductService(products, details);
    assertThrows(IllegalStateException.class, () -> service.deleteById(7));
    verify(products, never()).deleteById(anyInt());
  }

  @Test
  @CaseIds("TC-087")
  void deletesProductWithoutOrders() {
    ProductRepository products = mock(ProductRepository.class);
    OrderDetailRepository details = mock(OrderDetailRepository.class);
    when(products.existsById(7)).thenReturn(true);
    new ProductService(products, details).deleteById(7);
    verify(products).deleteById(7);
  }

  @Test
  @CaseIds("TC-081")
  void rejectsNegativePriceBeforeDatabaseWrite() {
    ProductRepository products = mock(ProductRepository.class);
    Product product = new Product();
    product.setName("Bình gốm");
    product.setPrice(new BigDecimal("-1"));
    product.setDiscount(BigDecimal.ZERO);
    ProductService service = new ProductService(products, mock(OrderDetailRepository.class));
    assertThrows(IllegalArgumentException.class, () -> service.save(product));
    verify(products, never()).save(any());
  }
}
