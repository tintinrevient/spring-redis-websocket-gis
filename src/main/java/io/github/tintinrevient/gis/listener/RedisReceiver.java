package io.github.tintinrevient.gis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tintinrevient.gis.model.GisMessage;
import io.github.tintinrevient.gis.service.WebSocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RedisReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisReceiver.class);

    private final WebSocketMessageService webSocketMessageService;

    public RedisReceiver(WebSocketMessageService webSocketMessageService) {
        this.webSocketMessageService = webSocketMessageService;
    }

    // Invoked when message is publish to "gis" channel
    public void receiveGisMessage(String message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
					   GisMessage gisMessage = objectMapper.readValue(message, GisMessage.class);

        LOGGER.info("Notification Message Received: " + gisMessage);
        webSocketMessageService.sendGisMessage(gisMessage);
    }

}
