package org.example.asm_java5.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
