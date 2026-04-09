package deokhugam.infra.redis.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Redis에서 전달받은 원본 메시지를 문자열로 변환한다.
        // 현재는 수신 확인용 로그만 남기고, 이후 여기서 알림 처리 로직으로 연결하면 된다.
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);

        log.info("Redis pub/sub message received - channel: {}, payload: {}", channel, payload);
    }
}
