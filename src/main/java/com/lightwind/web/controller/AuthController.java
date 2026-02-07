package com.lightwind.web.controller;

import com.lightwind.web.model.dto.LoginRequest;
import com.lightwind.web.model.dto.LoginResponse;
import com.lightwind.web.model.dto.RefreshTokenRequest;
import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;
import com.lightwind.web.security.JwtUtil;
import com.lightwind.web.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(createSuccessResponse("注册成功", response));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(createSuccessResponse("登录成功", response));
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        UserInfoVO userInfo = authService.getCurrentUser(username);
        return ResponseEntity.ok(createSuccessResponse("success", userInfo));
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        String newToken = authService.refreshAccessToken(request.getRefreshToken());

        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        data.put("expiresIn", 7200);

        return ResponseEntity.ok(createSuccessResponse("刷新成功", data));
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        authService.logout(token);
        return ResponseEntity.ok(createSuccessResponse("登出成功", null));
    }

    /**
     * 从Authentication中提取Token
     */
    private String getTokenFromAuthentication(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        return credentials != null ? credentials.toString() : "";
    }

    /**
     * 创建成功响应
     */
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
