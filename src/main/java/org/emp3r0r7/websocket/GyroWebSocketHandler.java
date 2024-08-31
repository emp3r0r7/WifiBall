package org.emp3r0r7.websocket;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.emp3r0r7.shared.SharedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@AllArgsConstructor
public class GyroWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GyroWebSocketHandler.class);

    private final SharedState sharedState;

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session, TextMessage message) {

        String payload = message.getPayload();

        //LOGGER.info("Received: {}", payload);

        //gestire indirizzamento in base a diverse fonti in ingresso
        sharedState.getLastGyroSensorState().set(payload);

    }
}
