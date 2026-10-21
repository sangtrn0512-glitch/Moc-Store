package org.example.asm_java5.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.service.CategoryService;
import org.example.asm_java5.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

  private final ProductService productService;
  private final CategoryService categoryService;

  public ProductController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;
  }

  @GetMapping("/product/list")
  public String page0(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    if ((minPrice != null && minPrice.signum() < 0)
        || (maxPrice != null && maxPrice.signum() < 0)
        || (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0)) {
      model.addAttribute("error", "Khoảng giá không hợp lệ");
      minPrice = null;
      maxPrice = null;
    }
    List<Product> filtered = new java.util.ArrayList<>();
    for (Product product : productService.findAll()) {
      if (!product.isAvailable()) continue;
      if (!product
          .getName()
          .toLowerCase(java.util.Locale.ROOT)
          .contains(keyword.trim().toLowerCase(java.util.Locale.ROOT))) continue;
      if (categoryId != null && !product.getCategory().getId().equals(categoryId)) continue;
      if (minPrice != null && product.getPrice().compareTo(minPrice) < 0) continue;
      if (maxPrice != null && product.getPrice().compareTo(maxPrice) > 0) continue;
      filtered.add(product);
    }
    if (sort.equals("name")) filtered.sort(java.util.Comparator.comparing(Product::getName));
    if (sort.equals("price")) filtered.sort(java.util.Comparator.comparing(Product::getPrice));
    if (sort.equals("priceDesc"))
      filtered.sort(java.util.Comparator.comparing(Product::getPrice).reversed());
    model.addAttribute(
        "products", org.example.asm_java5.service.ListPage.slice(filtered, page, model));
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
    return "product-list";
  }

  @GetMapping("/product/detail/{id}")
  public String page1(@PathVariable("id") Integer id, Model model) {
    Product product = productService.findById(id).orElse(null);

    if (product == null) {
      return "redirect:/product/list";
    }

    List<Product> relatedProducts =
        productService.findRelatedProducts(product.getCategory().getId(), product.getId());

    model.addAttribute("relatedProducts", relatedProducts);

    model.addAttribute("product", product);

    return "product-detail";
  }

  @GetMapping("/product/list-by-category/{id}")
  public String listByCategory(@PathVariable Integer id) {
    return "redirect:/product/list?categoryId=" + id;
  }

  @GetMapping("/product/list-by-price")
  public String listByPrice(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
    return "redirect:/product/list?minPrice=" + minPrice + "&maxPrice=" + maxPrice;
  }
}
