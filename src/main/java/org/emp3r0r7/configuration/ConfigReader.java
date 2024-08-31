package org.emp3r0r7.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReader.class);

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing {}", this.getClass().getSimpleName());
    }

    @Getter
    private static String applicationName;

    @Getter
    private static Long pollingRate;

    @Getter
    private static String tempPath;

    @Getter
    private static String gyroWebSocketEndpoint;

    @Getter
    private static String frontendWebSocketEndpoint;

    @Getter
    private static Integer webSocketPort;


    @Value("${application.dump.path}")
    private void setTempPath(String tempPath) { ConfigReader.tempPath = tempPath; }

    @Value("${application.dump_polling_rate}")
    private void setPollingRate(Long pollingRate){
        ConfigReader.pollingRate = pollingRate;
    }

    @Value("${gyro.websocket.endpoint.path}")
    public void setGyroWebSocketEndpoint(String gyroWebSocketEndpoint) { ConfigReader.gyroWebSocketEndpoint = gyroWebSocketEndpoint; }

    @Value("${server.port}")
    public void setWebSocketPort(Integer webSocketPort) { ConfigReader.webSocketPort = webSocketPort; }

    @Value("${application.name}")
    private void setApplicationName(String applicationName) { ConfigReader.applicationName = applicationName; }

    @Value("${frontend.websocket.endpoint.path}")
    private void setFrontendWebSocketEndpoint(String frontendWebSocketEndpoint) { ConfigReader.frontendWebSocketEndpoint = frontendWebSocketEndpoint; }

}
