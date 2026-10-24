package org.example.asm_java5.reporting;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import com.google.gson.*;
import java.nio.file.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

class ExtentReportingTests {
  // Kiểm tra báo cáo ghi đúng trạng thái test và đính kèm ảnh sau khi dọn dẹp.
  @Test void recordsRealJunitOutcomesAndScreenshotsAfterTeardown(@TempDir Path temporary) throws Exception {
    String previous = System.getProperty("extent.report.directory");
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    try {
      System.setProperty("extent.report.directory",temporary.toString());
      var request = LauncherDiscoveryRequestBuilder.request().selectors(selectClass(ReportingLifecycleProbe.class))
          .configurationParameter("junit.jupiter.extensions.autodetection.enabled","true").build();
      LauncherFactory.create().execute(request,listener);
    } finally {
      if(previous==null) System.clearProperty("extent.report.directory");
      else System.setProperty("extent.report.directory",previous);
    }
    var summary=listener.getSummary();
    assertEquals(3,summary.getTestsSucceededCount()); assertEquals(1,summary.getTestsFailedCount());
    assertEquals(1,summary.getTestsAbortedCount()); assertEquals(1,summary.getTestsSkippedCount());
    assertTrue(Files.isRegularFile(temporary.resolve("index.html")));
    JsonArray tests=JsonParser.parseString(Files.readString(temporary.resolve("results.json"))).getAsJsonArray();
    assertEquals(6,tests.size()); Map<String,Long> counts=new HashMap<>();
    for(JsonElement test:tests) counts.merge(test.getAsJsonObject().get("status").getAsString(),1L,Long::sum);
    assertEquals(3L,counts.get("PASS")); assertEquals(1L,counts.get("FAIL")); assertEquals(2L,counts.get("SKIP"));
    assertTrue(Files.readString(temporary.resolve("results.json")).contains("Controlled failure"));
    try(var screenshots=Files.list(temporary.resolve("screenshots"))) { assertEquals(5,screenshots.count()); }
    String html=Files.readString(temporary.resolve("index.html"));
    assertTrue(html.contains("screenshots/")); assertFalse(html.contains("src=\"https://"));
  }
}
