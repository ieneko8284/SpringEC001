package com.springec001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springec001.entity.User;
import com.springec001.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    // 登録画面を表示する（GET）
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // 空のユーザーオブジェクトを渡して、th:object と紐付ける
        model.addAttribute("user", new User());
        return "register";
    }

    // 登録を実行する（POST）
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // Service でハッシュ化して保存！
        userService.registerUser(user);
        
        // 登録が終わったら、成功メッセージ付きでログイン画面へ
        return "redirect:/login?register_success";
    }
}