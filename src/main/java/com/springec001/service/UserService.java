package com.springec001.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springec001.entity.User;
import com.springec001.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // PasswordConfigで定義した「専門家」をここで呼び出す
    private final PasswordEncoder passwordEncoder;

    /**
     * 新しいユーザーを登録する（パスワードをハッシュ化する）
     */
    public void registerUser(User user) {
        // 1. パスワードのハッシュ化
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. 権限と有効化フラグをセット（これを忘れるとログインできないことがあるよ）
        if (user.getRole() == null) {
            user.setRole("ROLE_USER"); // 一般ユーザー権限
        }
        user.setEnabled(true); // アカウントを有効にする

        // 3. 保存！
        userRepository.save(user);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + username));
    }
}