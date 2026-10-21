package org.example.asm_java5.support;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import javax.imageio.ImageIO;
import org.example.asm_java5.service.FileService;
import org.springframework.mock.web.MockMultipartFile;

public final class FileUploadProbe {
  public static void main(String[] args) throws Exception {
    ByteArrayOutputStream bytes=new ByteArrayOutputStream();
    ImageIO.write(new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB),"png",bytes);
    String filename=new FileService().save(new MockMultipartFile("photo","qa.png","image/png",bytes.toByteArray()));
    Path saved=Path.of("uploads",filename);
    if(!filename.matches("[a-f0-9-]{36}\\.png") || !Files.isRegularFile(saved)) throw new AssertionError("Upload missing or filename unsafe");
    BufferedImage image=ImageIO.read(saved.toFile());
    if(image==null || image.getWidth()!=100 || image.getHeight()!=100) throw new AssertionError("Stored image invalid");
    System.out.println("Verified UUID filename and 100x100 PNG in isolated working directory");
  }
}
