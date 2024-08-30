package org.emp3r0r7.websocket;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.emp3r0r7.configuration.ConfigReader;
import org.emp3r0r7.shared.GyroSharedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    private final String endpointPath = ConfigReader.getWebSocketEndpoint();

    private final GyroSharedState gyroSharedState;

    @PostConstruct
    public void init() {
        logger.info("Initializing WebSocketConfig class | Target endpoint : {}", endpointPath);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), endpointPath)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(gyroSharedState);
    }
}
