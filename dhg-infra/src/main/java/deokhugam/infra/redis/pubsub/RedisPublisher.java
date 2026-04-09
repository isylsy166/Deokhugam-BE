package deokhugam.infra.redis.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    public void publish(Object message) {
        // 등록된 notification 채널로 메시지를 발행한다.
        // subscriber가 같은 채널을 구독 중이면 즉시 메시지를 받을 수 있다.
        redisTemplate.convertAndSend(notificationTopic.getTopic(), message);
    }
}
