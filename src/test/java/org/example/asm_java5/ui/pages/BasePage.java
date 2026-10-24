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

  // Chờ ô nhập hiển thị, xóa nội dung cũ và nhập giá trị mới.
  protected void input(String name, String value) {
    WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
    field.clear();
    field.sendKeys(value);
  }

  // Cuộn tức thì tới nút và chờ bấm được bằng Selenium.
  protected WebElement click(By locator) {
    return wait.until(browser -> {
      try {
        WebElement element = browser.findElement(locator);
        if (!element.isDisplayed() || !element.isEnabled()) return null;
        ((JavascriptExecutor) browser).executeScript(
            "arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});",
            element);
        element.click();
        return element;
      } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
        return null;
      }
    });
  }

  // Bấm gửi form và chờ nút ở trang cũ mất hiệu lực.
  protected void submit(String selector) {
    WebElement button = click(By.cssSelector(selector));
    wait.until(ignored -> {
      try {
        button.isEnabled();
        return false;
      } catch (StaleElementReferenceException e) {
        return true;
      } catch (WebDriverException e) {
        if (e.getMessage() != null
            && e.getMessage().contains("Node with given id does not belong to the document")) {
          return true;
        }
        throw e;
      }
    });
  }

  // Chờ và lấy nội dung thông báo lỗi.
  public String error() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='alert']")))
        .getText();
  }

  // Chờ và lấy nội dung thông báo thành công.
  public String success() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='status']")))
        .getText();
  }
}
