package org.example.asm_java5.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportAController {
  @GetMapping({"/admin/report/revenue"})
  public String page0() {
    return "admin/revenue";
  }

  @GetMapping({"/admin/report/vip"})
  public String page1() {
    return "admin/vip";
  }
}
