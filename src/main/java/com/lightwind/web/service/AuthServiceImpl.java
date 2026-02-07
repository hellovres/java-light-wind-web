package com.lightwind.web.service;

import com.lightwind.web.exception.AuthenticationException;
import com.lightwind.web.exception.UserAlreadyExistsException;
import com.lightwind.web.model.User;
import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;
import com.lightwind.web.store.InMemoryUserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现 - Session认证
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final InMemoryUserStore userStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userStore.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("用户名已存在");
        }

        User user = User.builder()
                .id(userStore.getNextId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userStore.save(user);
    }

    @Override
    public void login(String username, String password) {
        User user = userStore.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // Session管理由Spring Security处理，无需返回token
    }

    @Override
    public UserInfoVO getCurrentUser(String username) {
        User user = userStore.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }

        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
