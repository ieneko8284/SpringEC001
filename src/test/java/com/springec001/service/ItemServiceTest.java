package com.springec001.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springec001.entity.Item;
import com.springec001.repository.ItemRepository;

@ExtendWith(MockitoExtension.class) // Mockitoを使うための設定
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository; // 本物のDBの代わりに「モック」を使う

    @InjectMocks
    private ItemService itemService; // テスト対象のServiceにモックを注入

    @Test
    void IDを指定して商品が取得できること() {
        // 1. 準備 (Given)
        Item mockItem = new Item();
        mockItem.setId(1L);
        mockItem.setName("テスト商品");
        // 「ID:1で検索されたら、mockItemを返せ」とモックに指示
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));

        // 2. 実行 (When)
        Optional<Item> result = itemService.getItemById(1L);

        // 3. 検証 (Then)
        assertTrue(result.isPresent());
        assertEquals("テスト商品", result.get().getName());
        // ちゃんとRepositoryのfindByIdが1回呼ばれたかもチェックできる
        verify(itemRepository, times(1)).findById(1L);
    }
}