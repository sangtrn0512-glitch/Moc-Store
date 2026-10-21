package org.example.asm_java5.controller.admin;

import org.example.asm_java5.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportAController {
  private final ReportService reports;

  public ReportAController(ReportService reports) {
    this.reports = reports;
  }

  @GetMapping("/admin/report/revenue")
  public String revenue(Model model) {
    model.addAttribute("rows", reports.revenue());
    return "admin/revenue";
  }

  @GetMapping("/admin/report/vip")
  public String vip(Model model) {
    model.addAttribute("rows", reports.vip());
    return "admin/vip";
  }
}
