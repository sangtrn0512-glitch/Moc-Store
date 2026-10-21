package org.example.asm_java5.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {
  public CheckoutPage(WebDriver driver,String baseUrl) {super(driver,baseUrl);}
  public void addProduct(int id) {
    driver.get(baseUrl+"/product/detail/"+id); submit("form[action^='/cart/add/'] button");
  }
  public void requestCheckout() {driver.get(baseUrl+"/order/checkout");}
  public void placeOrder(String address,String phone) {
    input("address",address); input("phone",phone); submit("form[action='/order/checkout'] button");
    wait.until(ExpectedConditions.urlContains("/order/detail/"));
  }
  public void logout() {submit("form[action='/auth/logout'] button");}
}
