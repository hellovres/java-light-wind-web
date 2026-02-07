package com.lightwind.web.service;

import com.lightwind.web.exception.AuthenticationException;
import com.lightwind.web.exception.InvalidTokenException;
import com.lightwind.web.exception.UserAlreadyExistsException;
import com.lightwind.web.model.User;
import com.lightwind.web.model.dto.LoginRequest;
import com.lightwind.web.model.dto.LoginResponse;
import com.lightwind.web.model.dto.RegisterRequest;
import com.lightwind.web.model.vo.UserInfoVO;
import com.lightwind.web.security.JwtUtil;
import com.lightwind.web.store.InMemoryRefreshTokenStore;
import com.lightwind.web.store.InMemoryUserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final InMemoryUserStore userStore;
    private final InMemoryRefreshTokenStore refreshTokenStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userStore.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("用户名已存在");
        }

        User user = User.builder()
                .id(userStore.getNextId())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        userStore.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

        refreshTokenStore.save(refreshToken, user.getId());

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(7200L)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userStore.findByUsername(request.getUsername());

        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }

        if (!request.getPassword().equals(user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

        refreshTokenStore.save(refreshToken, user.getId());

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(7200L)
                .build();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        Long userId = jwtUtil.extractUserId(refreshToken);

        if (!refreshTokenStore.existsByTokenAndUserId(refreshToken, userId)) {
            throw new InvalidTokenException("RefreshToken无效");
        }

        return jwtUtil.generateAccessToken(username, userId);
    }

    @Override
    public void logout(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userStore.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }

        refreshTokenStore.deleteByUserId(user.getId());
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
