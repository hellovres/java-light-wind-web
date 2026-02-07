package com.lightwind.web.store;

import com.lightwind.web.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存用户存储
 */
@Component
public class InMemoryUserStore {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 初始化测试用户
        // 密码: admin123 (明文)
        users.put("admin", User.builder()
                .id(1L)
                .username("admin")
                .password("admin123")
                .build());

        // 密码: admin123 (明文)
        users.put("user", User.builder()
                .id(2L)
                .username("user")
                .password("admin123")
                .build());
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public User save(User user) {
        users.put(user.getUsername(), user);
        return user;
    }

    public Long getNextId() {
        return users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
    }
}
