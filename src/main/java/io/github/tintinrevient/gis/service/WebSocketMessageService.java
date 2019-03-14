package io.github.tintinrevient.gis.service;

import io.github.tintinrevient.gis.config.ApplicationProperties;
import io.github.tintinrevient.gis.model.GisMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageService {

    private final ApplicationProperties applicationProperties;
    private final SimpMessagingTemplate template;


    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageService.class);

    public WebSocketMessageService(ApplicationProperties applicationProperties, SimpMessagingTemplate template) {
        this.applicationProperties = applicationProperties;
        this.template = template;
    }

    @Async
    public void sendGisMessage(GisMessage message) {
    	   LOGGER.info("Send message via Web Socket: " + message);
        template.convertAndSend(applicationProperties.getInfo().getMessage(), message);
    }

}
