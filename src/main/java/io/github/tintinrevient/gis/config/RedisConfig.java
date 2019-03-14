package io.github.tintinrevient.gis.config;

import io.github.tintinrevient.gis.listener.RedisReceiver;
import io.github.tintinrevient.gis.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

@Configuration
public class RedisConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            @Qualifier("gisMessageListenerAdapter") MessageListenerAdapter gisMessageListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(gisMessageListenerAdapter,  new PatternTopic("gis"));
        return container;
    }

    @Bean("gisMessageListenerAdapter")
    MessageListenerAdapter gisMessageListenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveGisMessage");
    }

    @Bean
    RedisReceiver receiver(WebSocketMessageService webSocketMessageService) {
        return new RedisReceiver(webSocketMessageService);
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean // Redis Atomic Counter to store no. of total messages sent from multiple app instances.
    RedisAtomicInteger getGisMessageCounter(RedisTemplate redisTemplate){
        RedisAtomicInteger gisMessageCounter = new RedisAtomicInteger("total-gis-message", redisTemplate.getConnectionFactory());
        return gisMessageCounter;
    }

}
