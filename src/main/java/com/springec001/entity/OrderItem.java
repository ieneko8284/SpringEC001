package com.springec001.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("order_items") // テーブル名と一致させる
public class OrderItem {

    @Id
    private Long id;
    
    // 親である orders テーブルの ID が自動的に入る（Spring Data JDBCの仕組み）
    private Long orderId;
    
    private Long itemId;
    
    private int quantity;
    
    // 購入時の価格（テーブル設計の price_at_order と合わせる）
    private int priceAtOrder;
}