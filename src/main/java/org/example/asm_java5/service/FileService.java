package org.example.asm_java5.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
  public String save(MultipartFile file) {
    if (file.isEmpty() || file.getSize() > 2 * 1024 * 1024)
      throw new IllegalArgumentException("Chọn ảnh PNG/JPG tối đa 2 MB");
    try {
      java.awt.image.BufferedImage image = ImageIO.read(file.getInputStream());
      if (image == null || image.getWidth() > 5000 || image.getHeight() > 5000)
        throw new IllegalArgumentException("Ảnh không hợp lệ hoặc quá lớn");
      Path directory = Path.of("uploads").toAbsolutePath();
      Files.createDirectories(directory);
      String name = UUID.randomUUID() + ".png";
      ImageIO.write(image, "png", directory.resolve(name).toFile());
      return name;
    } catch (IOException e) {
      throw new IllegalArgumentException("Không thể lưu ảnh");
    }
  }
}
