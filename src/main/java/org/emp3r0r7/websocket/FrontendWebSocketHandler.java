package org.emp3r0r7.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.emp3r0r7.model.FrontEndData;
import org.emp3r0r7.shared.SharedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class FrontendWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendWebSocketHandler.class);

    private final SharedState sharedState;

    private WebSocketSession webSocketSession;

    public FrontendWebSocketHandler(SharedState sharedState) {
        this.sharedState = sharedState;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        LOGGER.info("New WebSocket connection established with frontend: {}", session.getId());
        this.webSocketSession = session;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from the frontend if necessary
        LOGGER.info("Received message from frontend: {}", message.getPayload());
    }

    public void sendDataToClient() {
        try {

            if(webSocketSession != null && webSocketSession.isOpen()){
                // Assuming sharedState holds a reference to the readingMap
                FrontEndData frontEndData = sharedState.getFrontEndData();
                String dataJson = convertToJson(frontEndData); // Convert the map to a JSON string
                webSocketSession.sendMessage(new TextMessage(dataJson));
            }

        } catch (IOException e) {
            LOGGER.error("Error sending data to frontend via WebSocket", e);
        }
    }

    private String convertToJson(Object data) {
        // Convert the map to JSON using your preferred library (e.g., Jackson or Gson)
        // Example with Jackson:
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            LOGGER.error("Error converting map to JSON", e);
            return "{}";
        }
    }
}
