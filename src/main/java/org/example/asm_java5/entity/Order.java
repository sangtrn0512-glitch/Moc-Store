package org.example.asm_java5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createDate;

  private String address;

  private String phone;

  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "username")
  private Account account;
}
