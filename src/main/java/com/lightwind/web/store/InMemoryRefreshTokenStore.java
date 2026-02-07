package com.lightwind.web.store;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存刷新令牌存储
 */
@Component
public class InMemoryRefreshTokenStore {

    private final ConcurrentHashMap<String, RefreshTokenInfo> tokens = new ConcurrentHashMap<>();

    public void save(String token, Long userId) {
        tokens.put(token, new RefreshTokenInfo(
                token,
                userId,
                Instant.now().plusSeconds(604800)  // 7天后过期
        ));
    }

    public boolean existsByTokenAndUserId(String token, Long userId) {
        RefreshTokenInfo info = tokens.get(token);
        return info != null && info.getUserId().equals(userId) && !info.isExpired();
    }

    public void deleteByUserId(Long userId) {
        tokens.entrySet().removeIf(entry ->
                entry.getValue().getUserId().equals(userId)
        );
    }

    @Data
    private static class RefreshTokenInfo {
        private final String token;
        private final Long userId;
        private final Instant expiredTime;

        public boolean isExpired() {
            return Instant.now().isAfter(expiredTime);
        }
    }
}
