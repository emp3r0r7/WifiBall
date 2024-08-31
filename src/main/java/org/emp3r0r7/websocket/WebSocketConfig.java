package org.emp3r0r7.websocket;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.emp3r0r7.configuration.ConfigReader;
import org.emp3r0r7.shared.SharedState;
import org.emp3r0r7.utils.NetUtils;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    private final String endpointPath = ConfigReader.getWebSocketEndpoint();

    private final Integer endpointPort = ConfigReader.getWebSocketPort();

    private final SharedState sharedState;

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing WebSocketConfig class | Target endpoint : {}", endpointPath);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), endpointPath)
                .setAllowedOrigins("*");

        LOGGER.info("Websocket registered at : {}:{}", NetUtils.obtainLocalIpAddress(), endpointPort);

    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(sharedState);
    }
}
