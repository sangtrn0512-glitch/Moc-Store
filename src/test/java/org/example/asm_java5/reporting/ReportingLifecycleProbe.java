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

  @Override public Path screenshotForReport() { return screenshot; }

  @AfterEach void evidence() throws Exception {
    Path temporary=Path.of(System.getProperty("extent.report.directory"),"probe-input");
    Files.createDirectories(temporary);
    screenshot = temporary.resolve(UUID.randomUUID()+".png");
    ImageIO.write(new BufferedImage(2,2,BufferedImage.TYPE_INT_RGB),"png",screenshot.toFile());
  }

  @Test void successful() {}
  @Test void failed() { fail("Controlled failure for reporting verification"); }
  @Test void aborted() { assumeTrue(false,"Controlled abort for reporting verification"); }
  @Test @Disabled("Controlled disabled test") void disabled() {}
  @ParameterizedTest @ValueSource(ints={1,2}) void parameterized(int value) {}
}
