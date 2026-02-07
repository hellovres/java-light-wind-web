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
        // 密码: admin123 (BCrypt加密: $2a$10$rKw5F8H3W6QfzLzv9gZpQe5mY9sZ1q3X5w8nY3q4k9p7v2m5n8q1x4z6)
        users.put("admin", User.builder()
                .id(1L)
                .username("admin")
                .password("$2a$10$rKw5F8H3W6QfzLzv9gZpQe5mY9sZ1q3X5w8nY3q4k9p7v2m5n8q1x4z6")
                .build());

        // 密码: admin123 (BCrypt加密)
        users.put("user", User.builder()
                .id(2L)
                .username("user")
                .password("$2a$10$rKw5F8H3W6QfzLzv9gZpQe5mY9sZ1q3X5w8nY3q4k9p7v2m5n8q1x4z6")
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
