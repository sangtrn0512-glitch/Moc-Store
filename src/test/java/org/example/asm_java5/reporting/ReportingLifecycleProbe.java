package org.example.asm_java5.reporting;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReportingLifecycleProbe implements ScreenshotEvidence {
  private Path screenshot;

  @Override
  public Path screenshotForReport() {
    return screenshot;
  }

  @AfterEach
  void evidence() throws Exception {
    Path temporary = Path.of(System.getProperty("extent.report.directory"), "probe-input");
    Files.createDirectories(temporary);
    screenshot = temporary.resolve(UUID.randomUUID() + ".png");
    ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", screenshot.toFile());
  }

  // Tạo test thành công để kiểm tra báo cáo ghi PASS.
  @Test
  void successful() {}

  // Cố tình tạo lỗi để kiểm tra báo cáo ghi FAIL.
  @Test
  void failed() {
    fail("Controlled failure for reporting verification");
  }

  // Cố tình dừng test để kiểm tra báo cáo ghi SKIP.
  @Test
  void aborted() {
    assumeTrue(false, "Controlled abort for reporting verification");
  }

  // Tạo test bị vô hiệu hóa để kiểm tra báo cáo ghi SKIP.
  @Test
  @Disabled("Controlled disabled test")
  void disabled() {}

  // Tạo test nhiều bộ dữ liệu để kiểm tra báo cáo ghi từng lần chạy.
  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void parameterized(int value) {}
}
