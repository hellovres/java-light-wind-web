package com.lightwind.web.service;

import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;

/**
 * 认证服务接口 - Session认证
 */
public interface AuthService {

    /**
     * 用户注册
     */
    void register(RegisterRequest request);

    /**
     * 用户登录（验证用户名密码）
     */
    void login(String username, String password);

    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUser(String username);
}
