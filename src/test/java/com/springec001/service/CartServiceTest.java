package com.springec001.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springec001.entity.Cart;
import com.springec001.entity.CartItem;
import com.springec001.entity.Item;
import com.springec001.repository.CartItemRepository;
import com.springec001.repository.CartRepository;
import com.springec001.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ItemRepository itemRepository;
    
    @Mock
    private CartRepository cartRepository; // 偽物のリポジトリ

    @Mock
    private CartItemRepository cartItemRepository; // これを Mock に追加！
    
    @InjectMocks
    private CartService cartService; // テスト対象

    @Test
    void カートが存在しない場合に新しく作成して保存されること() {
        // 1. 準備 (Given)
        Long userId = 1L;
        Long itemId = 100L;
        int quantity = 2;

        // 【ここを追加！】商品リポジトリからID:100がちゃんと返ってくるように設定
        Item mockItem = new Item();
        mockItem.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        
        // 「userId=1 で検索したら、空(Optional.empty)を返せ」とモックに指示
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // 2. 実行 (When)
        cartService.addItemToCart(userId, itemId, quantity);

        // 3. 検証 (Then)
        // 「cartRepositoryのsaveメソッドが、1回呼ばれたか？」を確認
        // かつ、その保存されたカートのuserIdが1であることをチェック
        verify(cartRepository, times(1)).save(argThat(cart -> 
            cart.getUserId().equals(userId) && 
            cart.getCartItems().size() == 1
        ));
    }
    
    @Test
    void 商品が存在しない場合は例外が投げられること() {
        // 1. 準備 (Given)
        Long userId = 1L;
        Long itemId = 999L; // 存在しない想定のID
        
        Item mockItem = new Item();
        mockItem.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        
        // モックの設定：findById(999) が呼ばれたら「空(Optional.empty)」を返す
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // 2. 実行 & 検証 (When & Then)
        // 「RuntimeExceptionが投げられること」を断言（Assert）する
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addItemToCart(userId, itemId, 1);
        });

        // 3. メッセージの抜き打ち検査 (Optional)
        assertEquals("商品が見つかりません: 999", exception.getMessage());
        
        // 重要：例外が出たなら、DB保存(save)は一度も呼ばれていないはず！
        verify(cartRepository, never()).save(any());
    }
    
    @Test
    void ユーザーIDを指定してカートの内容を正しく取得できること() {
        // 1. 準備 (Given)
        Long userId = 1L;
        
        // テスト用のカートを作成
        Cart mockCart = new Cart();
        mockCart.setUserId(userId);
        
        // 商品A (ID: 101, 数量: 1)
        CartItem item1 = new CartItem();
        item1.setItemId(101L);
        item1.setQuantity(1);
        
        // 商品B (ID: 102, 数量: 3)
        CartItem item2 = new CartItem();
        item2.setItemId(102L);
        item2.setQuantity(3);
        
        // カートに商品を追加
        mockCart.getCartItems().add(item1);
        mockCart.getCartItems().add(item2);

        // モックの台本：リポジトリがこのカートを返すように設定
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));

        // 2. 実行 (When)
        Cart result = cartService.getCartByUserId(userId);

        // 3. 検証 (Then)
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(2, result.getCartItems().size(), "カート内の商品種類数が一致しません");
        
        // 中身の具体的な値もチェック（1つ目の商品）
        CartItem resultItem = result.getCartItems().iterator().next();
        assertEquals(101L, resultItem.getItemId());
        assertEquals(1, resultItem.getQuantity());
    }
    
    @Test
    void ユーザーIDを指定して表示用のカート商品リストを正しく取得できること() {
        // 1. 準備 (Given)
        Long userId = 1L;
        
        // 表示用DTOのモックデータを作成
        com.springec001.dto.CartItemRow row1 = new com.springec001.dto.CartItemRow(1L, 101L, "JavaTシャツ", 2500, 2);
        com.springec001.dto.CartItemRow row2 = new com.springec001.dto.CartItemRow(2L, 102L, "Springマグカップ", 1500, 1);
        java.util.List<com.springec001.dto.CartItemRow> mockRows = java.util.Arrays.asList(row1, row2);

        // 新しいリポジトリの動きを定義
        when(cartItemRepository.findCartItemRowsByUserId(userId)).thenReturn(mockRows);

        // 2. 実行 (When)
        java.util.List<com.springec001.dto.CartItemRow> result = cartService.getCartItems(userId);

        // 3. 検証 (Then)
        assertEquals(2, result.size());
        assertEquals("JavaTシャツ", result.get(0).getName());
        assertEquals(5000, result.get(0).getSubtotal()); // 小計計算が正しいか
        
        verify(cartItemRepository, times(1)).findCartItemRowsByUserId(userId);
    }
}