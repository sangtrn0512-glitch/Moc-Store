package org.example.asm_java5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class FormErrors {
  @ExceptionHandler({
    IllegalArgumentException.class,
    org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
    org.springframework.web.bind.MissingServletRequestParameterException.class,
    org.springframework.web.multipart.MaxUploadSizeExceededException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String invalid(Exception e, Model model) {
    model.addAttribute(
        "message", "Dữ liệu không hợp lệ. Kiểm tra các ô nhập hoặc kích thước ảnh rồi thử lại.");
    return "form-error";
  }

  @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String conflict(Model model) {
    model.addAttribute("message", "Không thể lưu hoặc xóa vì dữ liệu trùng hay đang được sử dụng.");
    return "form-error";
  }
}
