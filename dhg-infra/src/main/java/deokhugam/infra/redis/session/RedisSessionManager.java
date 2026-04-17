package deokhugam.infra.redis.session;

import deokhugam.infra.redis.common.RedisNameSpace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(RedisNameSpace nameSpace, String id, Object value) {
        String key = nameSpace.createKey(id);
        redisTemplate.opsForValue().set(key, value, nameSpace.getTtl());
    }

    public <T> Optional<T> find(RedisNameSpace nameSpace, String id, Class<T> clazz) {
        String key = nameSpace.createKey(id);
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) return Optional.empty();

        try {
            return Optional.of(clazz.cast(value));
        } catch (ClassCastException e) {
            log.error("Redis 타입 불일치 발생 - Key: {}, 예상 타입: {}, 실제 데이터 타입: {}",
                    key, clazz.getSimpleName(), value.getClass().getSimpleName());
            return Optional.empty();
        }
    }

    public void delete(RedisNameSpace nameSpace, String id) {
        String key = nameSpace.createKey(id);
        redisTemplate.delete(key);
    }

    public boolean hasKey(RedisNameSpace nameSpace, String id) {
        String key = nameSpace.createKey(id);
        return redisTemplate.hasKey(key);
    }
}
