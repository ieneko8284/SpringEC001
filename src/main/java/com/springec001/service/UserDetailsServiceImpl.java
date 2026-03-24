package com.springec001.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springec001.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 家村くんが作った UserRepository で DB から User(entity) を探す
        com.springec001.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        // 2. Spring Security が理解できる「UserDetails」という型に詰め替えて返す
        // ※ password は DB に入れたハッシュ済みのものをそのまま渡せば OK！
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole()) // "ROLE_USER" などの文字列をそのまま渡す
                .disabled(!user.isEnabled()) // アカウントが無効ならログイン不可にする
                .build();
    }
}
