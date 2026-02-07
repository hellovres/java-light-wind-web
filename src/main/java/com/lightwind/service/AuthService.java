package com.lightwind.service;

import com.lightwind.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final Map<String, User> users = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        users.put("admin", new User("admin",
               "admin123",
                "admin@example.com"));
    }

    public void register(String username, String password, String email) {
        if (users.containsKey(username)) {
            throw new RuntimeException("用户名已存在");
        }
        users.put(username, new User(username, password, email));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return  rawPassword.equals(encodedPassword);
    }
}
