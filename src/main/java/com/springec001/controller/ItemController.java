package com.springec001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springec001.entity.Item;
import com.springec001.repository.ItemRepository;
import com.springec001.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

    // 1. Repositoryに加えて、Serviceも「大事な部品」として登録する
    private final ItemRepository itemRepository;
    private final ItemService itemService; // これを書き足す！

    @GetMapping("/items")
    public String list(Model model) {
        Iterable<Item> itemList = itemRepository.findAll();
        model.addAttribute("items", itemList);
        return "item_list";
    }
    
    @GetMapping("/items/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        // さっきテストしたServiceのメソッドをここで使う！
        Item item = itemService.getItemById(id)
                        .orElseThrow(() -> new RuntimeException("商品が見つかりません"));
        
        model.addAttribute("item", item);
        return "item_detail";
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/items"; // 商品一覧へ自動転送！
    }
}