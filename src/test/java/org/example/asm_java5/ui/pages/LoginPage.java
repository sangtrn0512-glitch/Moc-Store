package org.example.asm_java5.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
  public LoginPage(WebDriver driver, String baseUrl) {
    super(driver, baseUrl);
  }

  public LoginPage open() {
    driver.get(baseUrl + "/auth/login");
    return this;
  }

  public LoginPage login(String username, String password) {
    input("username", username);
    input("password", password);
    submit("form[action='/auth/login'] button");
    return this;
  }

  public void awaitPath(String path) {
    wait.until(ExpectedConditions.urlToBe(baseUrl + path));
  }
}
