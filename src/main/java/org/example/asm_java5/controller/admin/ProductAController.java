package org.example.asm_java5.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.asm_java5.entity.Category;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.service.CategoryService;
import org.example.asm_java5.service.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductAController {
  private final org.example.asm_java5.service.FileService fileService;
  private final ProductService productService;
  private final CategoryService categoryService;

  public ProductAController(
      ProductService productService,
      CategoryService categoryService,
      org.example.asm_java5.service.FileService fileService) {
    this.fileService = fileService;
    this.productService = productService;
    this.categoryService = categoryService;
  }

  @GetMapping("/admin/product/index")
  public String index(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    Product product = new Product();
    product.setAvailable(true);
    product.setDiscount(BigDecimal.ZERO);
    model.addAttribute("product", product);
    java.util.List<Product> filtered = new java.util.ArrayList<>();
    for (Product item : productService.findAll()) {
      if ((item.getName())
          .toLowerCase(java.util.Locale.ROOT)
          .contains(keyword.trim().toLowerCase(java.util.Locale.ROOT))) filtered.add(item);
    }
    if (sort.equals("name")) filtered.sort(java.util.Comparator.comparing(Product::getName));
    if (sort.equals("price")) filtered.sort(java.util.Comparator.comparing(Product::getPrice));
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute(
        "products", org.example.asm_java5.service.ListPage.slice(filtered, page, model));

    model.addAttribute("categories", categoryService.findAll());
    return "admin/product";
  }

  @GetMapping("/admin/product/edit/{id}")
  public String edit(@PathVariable("id") Integer id, Model model) {
    Product product = productService.findById(id).orElse(null);
    if (product == null) {
      return "redirect:/admin/product/index";
    }
    model.addAttribute("product", product);
    model.addAttribute("products", productService.findAll());
    model.addAttribute("categories", categoryService.findAll());
    return "admin/product";
  }

  @PostMapping("/admin/product/create")
  public String create(
      @RequestParam("name") String name,
      @RequestParam("price") BigDecimal price,
      @RequestParam("discount") BigDecimal discount,
      @RequestParam(value = "image", defaultValue = "") String image,
      @RequestParam(value = "available", defaultValue = "false") boolean available,
      @RequestParam(value = "categoryId", required = false) Integer categoryId,
      @RequestParam(required = false) org.springframework.web.multipart.MultipartFile imageFile,
      Model model,
      RedirectAttributes redirectAttributes) {
    Product product = new Product();
    product.setCreateDate(LocalDate.now());
    Category category = null;
    if (categoryId != null) {
      category = categoryService.findById(categoryId).orElse(null);
    }
    product.setName(name.trim());
    product.setPrice(price);
    product.setDiscount(discount);
    product.setImage(image.trim());
    product.setAvailable(available);
    product.setCategory(category);

    try {
      if (imageFile != null && !imageFile.isEmpty()) {
        product.setImage(fileService.save(imageFile));
      }
      productService.save(product);
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("product", product);
      model.addAttribute("products", productService.findAll());
      model.addAttribute("categories", categoryService.findAll());
      return "admin/product";
    } catch (DataIntegrityViolationException e) {
      model.addAttribute("error", "Không thể lưu sản phẩm. Vui lòng kiểm tra lại dữ liệu.");
      model.addAttribute("product", product);
      model.addAttribute("products", productService.findAll());
      model.addAttribute("categories", categoryService.findAll());
      return "admin/product";
    }
    redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm");
    return "redirect:/admin/product/index";
  }

  @PostMapping("/admin/product/update")
  public String update(
      @RequestParam("id") Integer id,
      @RequestParam("name") String name,
      @RequestParam("price") BigDecimal price,
      @RequestParam("discount") BigDecimal discount,
      @RequestParam(value = "image", defaultValue = "") String image,
      @RequestParam(value = "available", defaultValue = "false") boolean available,
      @RequestParam(value = "categoryId", required = false) Integer categoryId,
      @RequestParam(required = false) org.springframework.web.multipart.MultipartFile imageFile,
      Model model,
      RedirectAttributes redirectAttributes) {
    Product product = productService.findById(id).orElse(null);
    if (product == null) {
      return "redirect:/admin/product/index";
    }
    Category category = null;
    if (categoryId != null) {
      category = categoryService.findById(categoryId).orElse(null);
    }
    product.setName(name.trim());
    product.setPrice(price);
    product.setDiscount(discount);
    product.setImage(image.trim());
    product.setAvailable(available);
    product.setCategory(category);

    try {
      if (imageFile != null && !imageFile.isEmpty()) {
        product.setImage(fileService.save(imageFile));
      }
      productService.save(product);
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("product", product);
      model.addAttribute("products", productService.findAll());
      model.addAttribute("categories", categoryService.findAll());
      return "admin/product";
    } catch (DataIntegrityViolationException e) {
      model.addAttribute("error", "Không thể lưu sản phẩm. Vui lòng kiểm tra lại dữ liệu.");
      model.addAttribute("product", product);
      model.addAttribute("products", productService.findAll());
      model.addAttribute("categories", categoryService.findAll());
      return "admin/product";
    }
    redirectAttributes.addFlashAttribute("success", "Đã cập nhật sản phẩm");
    return "redirect:/admin/product/index";
  }

  @PostMapping("/admin/product/delete/{id}")
  public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
    try {
      productService.deleteById(id);
      redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm");
    } catch (IllegalStateException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (DataIntegrityViolationException e) {
      redirectAttributes.addFlashAttribute("error", "Sản phẩm đang được sử dụng, không thể xóa");
    }
    return "redirect:/admin/product/index";
  }
}
