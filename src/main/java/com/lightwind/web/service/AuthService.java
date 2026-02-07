package com.lightwind.web.service;

import com.lightwind.web.model.dto.LoginRequest;
import com.lightwind.web.model.dto.LoginResponse;
import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户注册
     */
    LoginResponse register(RegisterRequest request);

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 刷新访问令牌
     */
    String refreshAccessToken(String refreshToken);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUser(String username);
}
