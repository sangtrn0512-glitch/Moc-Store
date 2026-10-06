package org.example.asm_java5.entity;

import jakarta.persistence.*;
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
}
