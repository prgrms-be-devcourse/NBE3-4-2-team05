package z9.second.global.redis;

import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String userId, String refreshToken, long expiredTime) {
        String key = keyGenerator(userId);
        save(key, refreshToken, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        String key = keyGenerator(userId);
        return (String) get(key);
    }

    public void deleteRefreshToken(String userId) {
        String key = keyGenerator(userId);
        delete(key);
    }

    private static String keyGenerator(String userId) {
        return String.format("%s_%s", REFRESH_TOKEN_HEADER, userId);
    }

    private void save(String key, Object value, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, duration, timeUnit);
    }

    private Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void delete(String key) {
        redisTemplate.delete(key);
    }
}
