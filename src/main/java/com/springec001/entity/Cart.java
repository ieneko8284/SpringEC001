package com.springec001.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("carts")
public class Cart {
    @Id
    private Long id;
    
    private Long userId; // どのユーザーのカートか
    
    private LocalDateTime createdAt = LocalDateTime.now();

    // 1つのカートに複数のアイテム（1対多）
    // idColumnは相手側のテーブル(cart_items)にある自分を指すカラム名
    @MappedCollection(idColumn = "cart_id")
    private Set<CartItem> cartItems = new HashSet<>();
}