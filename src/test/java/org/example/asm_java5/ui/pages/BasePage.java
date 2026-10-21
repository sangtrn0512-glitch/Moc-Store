package org.example.asm_java5.ui.pages;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public abstract class BasePage {
  protected final WebDriver driver;
  protected final WebDriverWait wait;
  protected final String baseUrl;

  protected BasePage(WebDriver driver, String baseUrl) {
    this.driver = driver;
    this.baseUrl = baseUrl;
    wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  protected void input(String name, String value) {
    WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
    field.clear();
    field.sendKeys(value);
  }

  protected void submit(String selector) {
    WebElement button =
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
    button.click();
    wait.until(ExpectedConditions.stalenessOf(button));
  }

  public String error() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='alert']")))
        .getText();
  }

  public String success() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='status']")))
        .getText();
  }
}
