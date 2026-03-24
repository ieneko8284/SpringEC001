package com.springec001.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("cart_items")
public class CartItem {
    @Id
    private Long id;
    
    // cart_id は @MappedCollection が管理してくれるからここには書かなくてOK
    
    private Long itemId; // どの商品か
    
    private Integer quantity; // 個数
}