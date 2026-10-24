package org.example.asm_java5.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {
  public CheckoutPage(WebDriver driver,String baseUrl) {super(driver,baseUrl);}
  // Mở chi tiết sản phẩm và bấm thêm vào giỏ.
  public void addProduct(int id) {
    driver.get(baseUrl+"/product/detail/"+id); submit("form[action^='/cart/add/'] button");
  }
  // Mở trang đặt hàng.
  public void requestCheckout() {driver.get(baseUrl+"/order/checkout");}
  // Nhập địa chỉ, số điện thoại, đặt hàng và chờ trang chi tiết đơn.
  public void placeOrder(String address,String phone) {
    input("address",address); input("phone",phone); submit("form[action='/order/checkout'] button");
    wait.until(ExpectedConditions.urlContains("/order/detail/"));
  }
  // Bấm nút đăng xuất.
  public void logout() {submit("form[action='/auth/logout'] button");}
}
