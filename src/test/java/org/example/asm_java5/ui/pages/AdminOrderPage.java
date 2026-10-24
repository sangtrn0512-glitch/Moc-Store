package org.example.asm_java5.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class AdminOrderPage extends BasePage {
  public AdminOrderPage(WebDriver driver,String baseUrl) {super(driver,baseUrl);}
  // Mở đơn hàng và chuyển trạng thái sang hoàn tất.
  public void complete(long id) {
    driver.get(baseUrl+"/admin/order/edit/"+id);
    new Select(driver.findElement(By.name("status"))).selectByValue("COMPLETED");
    submit("form[action='/admin/order/update'] button");
  }
}
