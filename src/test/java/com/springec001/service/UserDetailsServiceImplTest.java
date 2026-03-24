package com.springec001.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.springec001.entity.User;
import com.springec001.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void 存在するユーザー名でUserDetailsが正しく生成されること() {
        // 1. 準備 (Given)
        String username = "ieneko";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("$2a$10$hashed..."); // ハッシュ済みのつもり
        mockUser.setRole("ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // 2. 実行 (When)
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // 3. 検証 (Then)
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(mockUser.getPassword(), result.getPassword());
        // 権限のチェック（"ROLE_USER" が "USER" として入っているか）
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}
