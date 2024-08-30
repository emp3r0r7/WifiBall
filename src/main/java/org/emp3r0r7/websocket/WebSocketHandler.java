package org.emp3r0r7.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Qui ricevi i dati e li puoi processare
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
        // Puoi anche rispondere al client se necessario
        session.sendMessage(new TextMessage("Received your data"));
    }
}
