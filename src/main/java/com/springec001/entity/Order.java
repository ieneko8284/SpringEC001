package com.springec001.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("orders")
public class Order {

    @Id
    private Long id;
    
    private Long userId;
    
    private LocalDateTime orderDate;
    
    private int totalPrice;

    // 1対多の関連付け
    // idColumn は order_items 側の親IDを指すカラム名
    @MappedCollection(idColumn = "order_id")
    private Set<OrderItem> orderItems = new HashSet<>();

    // 便利メソッド：明細を追加しやすくする
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }
}