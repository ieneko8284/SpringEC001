package com.springec001.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springec001.entity.Order;
import com.springec001.entity.User;
import com.springec001.service.OrderService;
import com.springec001.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService; // ユーザー特定のために必要
    
    @GetMapping("/order/success")
    public String showOrderSuccess() {
        return "order_success";
    }
    
    @GetMapping("/orders/history")
    public String showOrderHistory(Authentication authentication, Model model) {
        // 1. 現在ログインしているユーザーを取得
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        // 2. そのユーザーの注文履歴を取得
        List<Order> orders = orderService.getOrderHistory(user.getId());
        
        // 3. HTMLにデータを渡す
        model.addAttribute("orders", orders);
        
        // 4. order_history.html を表示
        return "order_history";
    }
}
