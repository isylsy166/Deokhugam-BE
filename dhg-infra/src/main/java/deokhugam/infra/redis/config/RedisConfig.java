package deokhugam.infra.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // Redis와 데이터를 주고받을 때 사용할 기본 템플릿 객체
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // Value를 JSON 형태로 변환해서 저장하기 위한 직렬화기
        // 자바 객체를 Redis에 그대로 넣을 수 없어서, 저장 가능한 형태(JSON)로 바꿔줘야 함
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // Key는 사람이 읽기 쉬운 문자열 형태로 저장하기 위한 직렬화
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Spring Boot가 만들어 준 Redis 연결 정보를 템플릿에 연결
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 일반 key는 문자열로 저장
        redisTemplate.setKeySerializer(stringSerializer);

        // hash 자료구조의 field(key 역할)도 문자열로 저장
        redisTemplate.setHashKeySerializer(stringSerializer);

        // 일반 value는 JSON으로 저장
        // 그래서 객체를 꺼낼 때도 JSON을 다시 자바 객체 형태로 읽을 수 있음
        redisTemplate.setValueSerializer(jsonSerializer);

        // hash 자료구조의 value도 JSON으로 저장
        redisTemplate.setHashValueSerializer(jsonSerializer);

        // 위에서 설정한 serializer와 connectionFactory를 RedisTemplate에 최종 반영
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
