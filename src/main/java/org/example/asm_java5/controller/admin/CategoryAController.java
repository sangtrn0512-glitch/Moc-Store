package org.example.asm_java5.controller.admin;

import org.example.asm_java5.entity.Category;
import org.example.asm_java5.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryAController {
  private final CategoryService categoryService;

  public CategoryAController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping({"/admin/category/index"})
  public String page0(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    java.util.List<Category> filtered = new java.util.ArrayList<>();
    for (Category item : categoryService.findAll()) {
      if ((item.getName())
          .toLowerCase(java.util.Locale.ROOT)
          .contains(keyword.trim().toLowerCase(java.util.Locale.ROOT))) filtered.add(item);
    }
    if (sort.equals("name")) filtered.sort(java.util.Comparator.comparing(Category::getName));
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute(
        "categories", org.example.asm_java5.service.ListPage.slice(filtered, page, model));
    model.addAttribute("category", new Category());
    return "admin/category";
  }

  @PostMapping("/admin/category/create")
  public String create(@RequestParam("name") String name, Model model) {
    name = name.trim();

    if (name.isEmpty()) {
      model.addAttribute("error", "Vui lòng nhập tên loại hàng");
      model.addAttribute("categories", categoryService.findAll());
      Category category = new Category();
      category.setName(name);
      model.addAttribute("category", category);
      return "admin/category";
    }
    if (categoryService.existsByName(name)) {
      model.addAttribute("error", "Tên loại hàng đã tồn tại");
      model.addAttribute("categories", categoryService.findAll());
      Category category = new Category();
      category.setName(name);
      model.addAttribute("category", category);
      return "admin/category";
    }

    Category category = new Category();
    category.setName(name);
    categoryService.save(category);
    return "redirect:/admin/category/index";
  }

  @GetMapping("/admin/category/edit/{id}")
  public String edit(@PathVariable("id") Integer id, Model model) {
    Category category = categoryService.findById(id).orElse(null);

    if (category == null) {
      return "redirect:/admin/category/index";
    }

    model.addAttribute("category", category);
    model.addAttribute("categories", categoryService.findAll());

    return "admin/category";
  }

  @PostMapping("/admin/category/update")
  public String update(
      @RequestParam("id") Integer id, @RequestParam("name") String name, Model model) {

    Category category = categoryService.findById(id).orElse(null);

    if (category == null) {
      return "redirect:/admin/category/index";
    }

    name = name.trim();
    String error = null;

    if (name.isEmpty()) {
      error = "Vui lòng nhập tên loại hàng";
    } else if (name.length() > 100) {
      error = "Tên loại hàng không được vượt quá 100 ký tự";
    } else if (categoryService.existsByNameAndIdNot(name, id)) {
      error = "Tên loại hàng đã tồn tại";
    }

    category.setName(name);

    if (error != null) {
      model.addAttribute("error", error);
      model.addAttribute("category", category);
      model.addAttribute("categories", categoryService.findAll());
      return "admin/category";
    }

    categoryService.save(category);
    return "redirect:/admin/category/index";
  }

  @PostMapping("/admin/category/delete/{id}")
  public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

    try {
      categoryService.deleteById(id);
      redirectAttributes.addFlashAttribute("success", "Đã xóa loại hàng");
    } catch (IllegalStateException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/admin/category/index";
  }
}
