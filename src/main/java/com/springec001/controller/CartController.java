package com.springec001.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springec001.dto.CartItemRow;
import com.springec001.entity.User;
import com.springec001.service.CartService;
import com.springec001.service.OrderService;
import com.springec001.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    
    // 商品詳細画面の「カートに入れる」ボタンから飛んでくる場所
    @PostMapping("/cart/add")
    public String addCart(@RequestParam Long itemId, @RequestParam int quantity, 
                          Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            cartService.addItemToCart(user.getId(), itemId, quantity);
            
            // 成功メッセージ
            redirectAttributes.addFlashAttribute("successMessage", "カートに追加しました！");
            return "redirect:/cart";
            
        } catch (RuntimeException e) {
            // Serviceで投げた "在庫数を超えています" などのメッセージを捕まえる
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            // 商品詳細画面（元のページ）にリダイレクトして戻す
            return "redirect:/items/" + itemId;
        }
    }
    
    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        // 1. ログイン中のユーザー名を取得
        String username = authentication.getName();
        
        // 2. ユーザー名からユーザー情報を取得
        User user = userService.findByUsername(username);
        
        // 3. ここ！ 型を List<CartItemRow> に書き換える
        List<CartItemRow> cartItems = cartService.getCartItems(user.getId());
        
        model.addAttribute("cartItems", cartItems);
        return "cart_view";
    }
    
 // CartController.java に追加
    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        // 削除が終わったら、またカート画面に戻る（リフレッシュされる）
        return "redirect:/cart";
    }
    
 // CartController.java に追加
    private final OrderService orderService;

    @PostMapping("/cart/checkout")
    public String checkout(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        // 注文処理を実行！
        orderService.createOrder(user.getId());
        
        // 完了したら、サンクスページへ
        return "redirect:/order/success";
    }
}