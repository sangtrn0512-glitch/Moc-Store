package org.example.asm_java5.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
  @Id private String username;

  private String password;

  private String fullName;

  private String email;

  private String photo;

  private boolean activated;

  private boolean admin;

  private String resetToken;

  private LocalDateTime resetExpiresAt;

  private String activationToken;

  private LocalDateTime activationExpiresAt;
}
