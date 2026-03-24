package com.springec001.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    @Test
    void パスワードのハッシュ値を生成してコンソールに出す() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "osakanasan"; // ハッシュ化したい文字
        String result = encoder.encode(rawPassword);
        
        System.out.println("生成されたハッシュ値はこれだよ！ -> " + result);
    }
}
