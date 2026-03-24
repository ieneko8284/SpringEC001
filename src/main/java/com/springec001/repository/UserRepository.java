package com.springec001.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.springec001.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    // ログイン時に使う「ユーザー名で検索」
    Optional<User> findByUsername(String username);
}