package com.springec001.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRow {
    private Long id;         // cart_itemsのID
    private Long itemId;     // 商品ID
    private String name;     // 商品名（itemsテーブルから）
    private int price;       // 単価（itemsテーブルから）
    private int quantity;    // 数量（cart_itemsテーブルから）

    // 小計を計算する便利なメソッド
    public int getSubtotal() {
        return this.price * this.quantity;
    }
}