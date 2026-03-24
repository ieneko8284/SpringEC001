package com.springec001.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springec001.dto.CartItemRow;
import com.springec001.entity.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    // ログインユーザーのIDに紐づくカート商品を、商品情報(items)とガッチャンコして取得する
    @Query("SELECT ci.id, ci.item_id, i.name, i.price, ci.quantity " +
           "FROM cart_items ci " +
           "JOIN items i ON ci.item_id = i.id " +
           "JOIN carts c ON ci.cart_id = c.id " +
           "WHERE c.user_id = :userId")
    List<CartItemRow> findCartItemRowsByUserId(@Param("userId") Long userId);
}
