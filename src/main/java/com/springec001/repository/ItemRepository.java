package com.springec001.repository;

import org.springframework.data.repository.CrudRepository;

import com.springec001.entity.Item;

// これだけで、全件検索や保存の魔法が使えるようになるよ
public interface ItemRepository extends CrudRepository<Item, Long> {
}