package org.example.asm_java5.service;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.io.TempDir;

class FileServiceTests {
  // Kiểm tra lưu ảnh hợp lệ trong tiến trình và thư mục tạm riêng.
  @Test @CaseIds({"TC-044","TC-085"})
  void savesValidImageInIsolatedProcess(@TempDir Path temp) throws Exception {
    String classpath=System.getProperty("surefire.test.class.path",System.getProperty("java.class.path"));
    Path args=temp.resolve("java.args");
    Files.writeString(args,"-cp\n\""+classpath.replace('\\','/')+"\"\norg.example.asm_java5.support.FileUploadProbe\n");
    String binary=System.getProperty("os.name").startsWith("Windows")?"java.exe":"java";
    Path java=Path.of(System.getProperty("java.home"),"bin",binary);
    Path log=temp.resolve("upload.log");
    Process child=new ProcessBuilder(java.toString(),"@"+args).directory(temp.toFile()).redirectErrorStream(true).redirectOutput(log.toFile()).start();
    try {
      assertTrue(child.waitFor(30,TimeUnit.SECONDS),"Upload subprocess timed out");
      assertEquals(0,child.exitValue(),Files.readString(log));
      try(var saved=Files.list(temp.resolve("uploads"))) {assertEquals(1,saved.count());}
    } finally {if(child.isAlive()) child.destroyForcibly();}
  }
  // Kiểm tra từ chối file rỗng, giả ảnh hoặc quá 2 MB.
  @Test
  @CaseIds("TC-045")
  void rejectsEmptyFakeAndOversizedFiles() {
    FileService files = new FileService();
    assertThrows(
        IllegalArgumentException.class,
        () -> files.save(new MockMultipartFile("photo", new byte[0])));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            files.save(
                new MockMultipartFile("photo", "fake.jpg", "image/jpeg", "plain text".getBytes())));
    assertThrows(
        IllegalArgumentException.class,
        () -> files.save(new MockMultipartFile("photo", new byte[2 * 1024 * 1024 + 1])));
  }

  // Kiểm tra từ chối ảnh rộng hơn 5.000 pixel.
  @Test
  @CaseIds("TC-045")
  void rejectsImageWiderThanFiveThousandPixels() throws Exception {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ImageIO.write(new BufferedImage(5001, 1, BufferedImage.TYPE_INT_RGB), "png", bytes);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new FileService()
                .save(
                    new MockMultipartFile("photo", "large.png", "image/png", bytes.toByteArray())));
  }
}
