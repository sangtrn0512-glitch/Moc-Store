package org.example.asm_java5.service;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
  private final JdbcTemplate jdbc;

  public ReportService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<java.util.Map<String, Object>> revenue() {
    return jdbc.queryForList(
        """
        SELECT c.name AS category, SUM(d.price*d.quantity) AS revenue,
        SUM(CAST(d.quantity AS BIGINT)) AS quantity, MAX(d.price) AS highest,
        MIN(d.price) AS lowest, SUM(d.price*d.quantity)/SUM(d.quantity) AS average
        FROM order_details d JOIN orders o ON o.id=d.order_id
        JOIN products p ON p.id=d.product_id JOIN categories c ON c.id=p.category_id
        WHERE o.status='COMPLETED' GROUP BY c.id,c.name ORDER BY revenue DESC
        """);
  }

  public List<java.util.Map<String, Object>> vip() {
    return jdbc.queryForList(
        """
        SELECT TOP 10 a.username, a.full_name AS customer, SUM(d.price*d.quantity) AS total,
        MIN(o.create_date) AS firstDate, MAX(o.create_date) AS lastDate
        FROM accounts a JOIN orders o ON o.username=a.username
        JOIN order_details d ON d.order_id=o.id WHERE o.status='COMPLETED'
        GROUP BY a.username,a.full_name ORDER BY total DESC,a.username
        """);
  }
}
