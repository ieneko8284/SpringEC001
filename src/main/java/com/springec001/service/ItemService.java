package com.springec001.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springec001.entity.Item;
import com.springec001.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // 1件検索するロジック
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
}