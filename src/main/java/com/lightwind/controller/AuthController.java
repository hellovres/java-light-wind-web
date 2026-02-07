package com.lightwind.controller;

import com.lightwind.entity.User;
import com.lightwind.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model, HttpSession httpSession) {
        User user = authService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "用户不存在");
            return "login";
        }

        if (!authService.validatePassword(password, user.getPassword())) {
            model.addAttribute("error", "密码错误");
            return "login";
        }
        httpSession.setAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required = false) String email,
                           Model model) {
        try {
            authService.register(username, password, email);
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession httpSession) {
        User user =(User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }
}
