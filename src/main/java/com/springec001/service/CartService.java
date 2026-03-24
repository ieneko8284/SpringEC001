package com.springec001.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springec001.dto.CartItemRow;
import com.springec001.entity.Cart;
import com.springec001.entity.CartItem;
import com.springec001.entity.Item;
import com.springec001.repository.CartItemRepository;
import com.springec001.repository.CartRepository;
import com.springec001.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository; // これを忘れずに！
    private final CartItemRepository cartItemRepository;
    
 // CartService.java の addItemToCart を改造
    public void addItemToCart(Long userId, Long itemId, int quantity) {
        // 1. 商品の存在チェック（ここは今まで通り）
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("商品が見つかりません: " + itemId));

        // 2. ユーザーのカートを取得（なければ作る）
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        // 3. 【ここが加算ロジック！】
        // すでにカート内に同じ itemId の商品があるか探す
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(ci -> ci.getItemId().equals(itemId))
                .findFirst();

        if (existingItem.isPresent()) {
            // すでにあれば、その個数を増やす
            CartItem ci = existingItem.get();
            int newQuantity = ci.getQuantity() + quantity;
            
            // 4. 【在庫チェック】
            if (newQuantity > item.getStock()) {
                throw new RuntimeException("在庫数を超えています。現在の在庫: " + item.getStock());
            }
            
            ci.setQuantity(newQuantity);
        } else {
            // なければ新しく作る
            if (quantity > item.getStock()) {
                throw new RuntimeException("在庫数を超えています");
            }
            CartItem newItem = new CartItem();
            newItem.setItemId(itemId);
            newItem.setQuantity(quantity);
            cart.getCartItems().add(newItem);
        }

        // 最後にカート全体を保存（CascadeでCartItemも保存される）
        cartRepository.save(cart);
    }
    
    // CartService.java に追加
    public Cart getCartByUserId(Long userId) {
        // ユーザーIDで検索して、なければ空のカートを返す
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });
    }
    
 // カートの中身を「表示用DTO」のリストで取得する
    public List<CartItemRow> getCartItems(Long userId) {
        return cartItemRepository.findCartItemRowsByUserId(userId);
    }
    
 // CartService.java に追加
    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}