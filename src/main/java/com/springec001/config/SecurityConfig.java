package com.springec001.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable()) // ★テスト用に一時的に無効化
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/items/**", "/login", "/register", "/css/**", "/js/**").permitAll() // 誰でも見れる
                .requestMatchers("/cart/**").authenticated() // ★ここ！ /cart で始まるURLはログイン必須
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/items", true)
                .permitAll()
                )
            .logout(logout -> logout
                .logoutSuccessUrl("/items") // ログアウトしたら一覧へ
                .permitAll()
            );

        return http.build();
    }
}
