package io.github.tintinrevient.gis.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tintinrevient.gis.model.GisMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final RedisAtomicInteger gisMessageCounter;
    private final StringRedisTemplate stringRedisTemplate;

    public MessageController(RedisAtomicInteger gisMessageCounter, StringRedisTemplate stringRedisTemplate) {
        this.gisMessageCounter = gisMessageCounter;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @MessageMapping("/message")
    public void sendWsGisMessage(String message) throws JsonProcessingException {
        LOGGER.info("Incoming WebSocket Message : {}", message);

        publishMessageToRedis(message);
    }

    @PostMapping("/message")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendHttpGisHttpMessage(@RequestBody Map<String, String> message) throws JsonProcessingException {
        String httpMessage = message.get("message");
        LOGGER.info("Incoming HTTP Message : {}", httpMessage);
        publishMessageToRedis(httpMessage);

        Map<String, String> response = new HashMap<>();
        response.put("response", "Message Sent Successfully over HTTP");

        return ResponseEntity.ok(response);
    }

    private void publishMessageToRedis(String message) throws JsonProcessingException {

        Integer totalChatMessage = gisMessageCounter.incrementAndGet();


        GisMessage gisMessage = new GisMessage(totalChatMessage, message, new Timestamp((new Date()).getTime()).toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String gisString = objectMapper.writeValueAsString(gisMessage);

        // Publish Message to Redis Channels
        stringRedisTemplate.convertAndSend("gis", gisString);
    }
}
