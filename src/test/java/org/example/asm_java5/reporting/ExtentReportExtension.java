package org.example.asm_java5.reporting;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.extension.*;

public class ExtentReportExtension implements BeforeEachCallback, TestWatcher {
  private static final ExtensionContext.Namespace NAMESPACE =
      ExtensionContext.Namespace.create(ExtentReportExtension.class);

  private ReportSession session(ExtensionContext context) {
    return context.getRoot().getStore(NAMESPACE).getOrComputeIfAbsent(
        ReportSession.class,
        key -> new ReportSession(Path.of(System.getProperty(
            "extent.report.directory", "target/extent-report/local"))),
        ReportSession.class);
  }

  private ExtentTest node(ExtensionContext context) {
    Set<String> categories = new LinkedHashSet<>(context.getTags());
    context.getTestMethod().ifPresent(method -> {
      CaseIds ids = method.getAnnotation(CaseIds.class);
      if (ids != null) categories.addAll(Arrays.asList(ids.value()));
    });
    String name = context.getRequiredTestClass().getSimpleName() + " · " + context.getDisplayName();
    return session(context).start(context.getUniqueId(), name, categories);
  }

  @Override public void beforeEach(ExtensionContext context) { node(context); }

  private void finish(ExtensionContext context, Status status, Throwable error, String message) {
    ExtentTest test = node(context);
    Path screenshot = context.getTestInstance()
        .filter(ScreenshotEvidence.class::isInstance)
        .map(ScreenshotEvidence.class::cast)
        .map(ScreenshotEvidence::screenshotForReport).orElse(null);
    session(context).complete(test, status, error, message, screenshot);
  }

  @Override public void testSuccessful(ExtensionContext context) {
    finish(context, Status.PASS, null, "All assertions passed");
  }
  @Override public void testFailed(ExtensionContext context, Throwable cause) {
    finish(context, Status.FAIL, cause, "Test failed");
  }
  @Override public void testAborted(ExtensionContext context, Throwable cause) {
    finish(context, Status.SKIP, cause, "Test aborted");
  }
  @Override public void testDisabled(ExtensionContext context, Optional<String> reason) {
    finish(context, Status.SKIP, null, reason.orElse("Test disabled"));
  }

  static final class ReportSession implements AutoCloseable {
    final Path directory;
    private final ExtentReports extent = new ExtentReports();
    private final Map<String, ExtentTest> tests = new ConcurrentHashMap<>();

    ReportSession(Path directory) {
      this.directory = directory.toAbsolutePath();
      try { Files.createDirectories(this.directory); }
      catch (IOException e) { throw new IllegalStateException("Cannot create Extent report directory", e); }
      ExtentSparkReporter spark = new ExtentSparkReporter(this.directory.resolve("index.html").toString());
      spark.config().setDocumentTitle("Mộc Store - Extent Report");
      spark.config().setReportName("JUnit & Selenium - Mộc Store");
      spark.config().setEncoding("UTF-8");
      spark.config().setOfflineMode(true);
      extent.attachReporter(spark, new JsonFormatter(this.directory.resolve("results.json").toString()));
      extent.setSystemInfo("Java", System.getProperty("java.version"));
      extent.setSystemInfo("OS", System.getProperty("os.name"));
      extent.setSystemInfo("Profile", System.getProperty("spring.profiles.active", "test"));
    }

    ExtentTest start(String id, String name, Set<String> categories) {
      return tests.computeIfAbsent(id, key -> {
        ExtentTest test = extent.createTest(name);
        if (!categories.isEmpty()) test.assignCategory(categories.toArray(String[]::new));
        return test;
      });
    }

    void complete(ExtentTest test, Status status, Throwable error, String message, Path screenshot) {
      if (error == null) test.log(status, message);
      else test.log(status, error);
      if (screenshot != null && Files.isRegularFile(screenshot)) {
        try {
          Path destination = directory.resolve("screenshots").resolve(UUID.randomUUID() + ".png");
          Files.createDirectories(destination.getParent());
          Files.copy(screenshot, destination);
          test.addScreenCaptureFromPath(directory.relativize(destination).toString().replace('\\', '/'), "Browser after test");
        } catch (IOException e) {
          test.info("Could not attach screenshot: " + e.getMessage());
        }
      }
    }

    @Override public synchronized void close() { extent.flush(); }
  }
}
