package com.lightwind.web.controller;

import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;
import com.lightwind.web.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 页面控制器
 */
@Controller
@RequiredArgsConstructor
public class PageController {

    private final AuthService authService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        if (authentication != null) {
            // 已登录，显示用户信息
            String username = authentication.getName();
            model.addAttribute("user", username);
            model.addAttribute("username", username);
        }
        return "index";
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", getErrorMessage(error));
        }
        return "auth/login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    /**
     * 个人中心
     */
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        UserInfoVO userInfo = authService.getCurrentUser(username);

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("username", username);

        return "profile";
    }

    /**
     * 获取错误消息
     */
    private String getErrorMessage(String error) {
        return switch (error) {
            case "credentials" -> "用户名或密码错误";
            case "locked" -> "账号已被锁定，请联系管理员";
            case "disabled" -> "账号已被禁用，请联系管理员";
            default -> "登录失败，请重试";
        };
    }
}
