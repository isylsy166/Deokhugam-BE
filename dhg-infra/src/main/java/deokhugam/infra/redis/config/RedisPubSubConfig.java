package deokhugam.infra.redis.config;

import deokhugam.infra.redis.pubsub.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisPubSubConfig {

    public static final String NOTIFICATION_TOPIC = "notification";

    @Bean
    public ChannelTopic notificationTopic() {
        // pub/sub에서 메시지를 주고받을 채널 이름을 Topic 객체로 등록
        return new ChannelTopic(NOTIFICATION_TOPIC);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            RedisSubscriber redisSubscriber,
            ChannelTopic notificationTopic
    ) {
        // Redis pub/sub 메시지를 수신할 리스너 컨테이너
        // subscriber를 채널에 연결해두면 같은 토픽으로 발행된 메시지를 받을 수 있다.
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisSubscriber, notificationTopic);
        return container;
    }
}
