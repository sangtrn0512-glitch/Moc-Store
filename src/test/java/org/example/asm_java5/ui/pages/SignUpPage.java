package org.example.asm_java5.ui.pages;

import org.openqa.selenium.WebDriver;

public class SignUpPage extends BasePage {
  public SignUpPage(WebDriver driver, String baseUrl) {
    super(driver, baseUrl);
  }

  public SignUpPage open() {
    driver.get(baseUrl + "/account/sign-up");
    return this;
  }

  public SignUpPage register(String username, String name, String email, String password) {
    input("username", username);
    input("fullName", name);
    input("email", email);
    input("password", password);
    submit("form[action^='/account/sign-up'] button[type='submit']");
    return this;
  }
}
