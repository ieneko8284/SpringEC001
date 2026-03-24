package com.springec001.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("users")
public class User {
    @Id
    private Long id;
    private String username;
    private String password; // ハッシュ化したパスワードを入れる予定
    private String email;
    private String role;     // 'ROLE_USER' とか
    private boolean enabled;
}