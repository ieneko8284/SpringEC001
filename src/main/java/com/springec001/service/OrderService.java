package com.springec001.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springec001.dto.CartItemRow;
import com.springec001.entity.Item;
import com.springec001.entity.Order;
import com.springec001.entity.OrderItem;
import com.springec001.repository.CartRepository;
import com.springec001.repository.ItemRepository;
import com.springec001.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @Transactional // ★これ重要！途中でエラーが起きたら全部白紙に戻す
    public void createOrder(Long userId) {
        // 1. カートの中身（DTO）を取得
        List<CartItemRow> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("カートが空です");
        }

        // 2. 在庫チェックと在庫の減算
        int totalPrice = 0;
        for (CartItemRow row : cartItems) {
            Item item = itemRepository.findById(row.getItemId())
                    .orElseThrow(() -> new RuntimeException("商品が存在しません"));
            
            if (item.getStock() < row.getQuantity()) {
                throw new RuntimeException(item.getName() + " の在庫が足りません");
            }
            
            // 在庫を減らして保存
            item.setStock(item.getStock() - row.getQuantity());
            itemRepository.save(item);
            
            totalPrice += row.getSubtotal();
        }

        // 3. Order（親）の作成と保存
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        
        // 4. OrderItems（明細）の作成
        for (CartItemRow row : cartItems) {
            OrderItem detail = new OrderItem();
            detail.setItemId(row.getItemId());
            detail.setQuantity(row.getQuantity());
            detail.setPriceAtOrder(row.getPrice()); // 購入時の価格を記録！
            order.addOrderItem(detail); // Entity側のリストに追加
        }
        
        orderRepository.save(order);

        // 5. カートを空にする
        cartRepository.deleteByUserId(userId);
    }
    
 // OrderService.java に追記
    public List<Order> getOrderHistory(Long userId) {
        // ユーザーIDに紐づく注文を、新しい順（降順）で取得する
        // ※ OrderRepository に findByUserIdOrderByOrderDateDesc を作る必要があるよ
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }
}