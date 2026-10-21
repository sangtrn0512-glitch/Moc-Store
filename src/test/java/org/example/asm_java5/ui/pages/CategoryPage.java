package org.example.asm_java5.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CategoryPage extends BasePage {
  public CategoryPage(WebDriver driver, String baseUrl) {
    super(driver, baseUrl);
  }

  public CategoryPage open() {
    driver.get(baseUrl + "/admin/category/index");
    return this;
  }

  public void create(String name) {
    input("name", name);
    submit("form[action='/admin/category/create'] button[type='submit']");
  }

  public void edit(int id) {
    driver.findElement(By.cssSelector("a[href='/admin/category/edit/" + id + "']")).click();
    wait.until(ExpectedConditions.urlToBe(baseUrl + "/admin/category/edit/" + id));
  }

  public String nameInForm() {
    return driver.findElement(By.name("name")).getAttribute("value");
  }

  public void update(String name) {
    input("name", name);
    submit("form[action='/admin/category/update'] button[type='submit']");
  }

  public void delete(int id) {
    WebElement button =
        driver.findElement(
            By.cssSelector("form[action='/admin/category/delete/" + id + "'] button"));
    button.click();
    wait.until(ExpectedConditions.alertIsPresent()).accept();
    wait.until(ExpectedConditions.stalenessOf(button));
  }

  public boolean contains(String name) {
    return driver.findElements(By.cssSelector("tbody tr td:nth-child(2)")).stream()
        .anyMatch(e -> e.getText().equals(name));
  }
}
