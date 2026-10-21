package org.example.asm_java5.service;

import java.util.List;
import org.springframework.ui.Model;

public class ListPage {
  public static <T> List<T> slice(List<T> items, int page, Model model) {
    int pages = Math.max(1, (items.size() + 9) / 10);
    page = Math.max(0, Math.min(page, pages - 1));
    int from = Math.min(page * 10, items.size());
    model.addAttribute("page", page);
    model.addAttribute("pages", pages);
    model.addAttribute("count", items.size());
    return items.subList(from, Math.min(from + 10, items.size()));
  }
}
