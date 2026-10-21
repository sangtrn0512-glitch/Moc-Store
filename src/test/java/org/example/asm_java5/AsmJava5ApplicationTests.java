package org.example.asm_java5;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AsmJava5ApplicationTests {
  @Autowired DataSource dataSource;

  @Test
  @CaseIds("TC-122")
  void contextLoads() throws Exception {
    try (var connection = dataSource.getConnection()) {
      assertTrue(connection.getMetaData().getURL().startsWith("jdbc:h2:mem:"));
    }
  }
}
