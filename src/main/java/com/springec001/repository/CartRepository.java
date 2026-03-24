package com.springec001.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springec001.entity.Cart;

public interface CartRepository extends CrudRepository<Cart, Long> {
    
    Optional<Cart> findByUserId(Long userId);

    // 購入後にユーザーのカートを削除する魔法の呪文
    @Modifying
    @Query("DELETE FROM carts WHERE user_id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}