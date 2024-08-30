package org.emp3r0r7.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {

    @Getter
    private static Long pollingRate;

    @Getter
    private static String tempPath;

    @Getter
    private static String webSocketEndpoint;


    @Value("${application.dump.path}")
    private void setTempPath(String tempPath) { ConfigReader.tempPath = tempPath; }

    @Value("${application.dump_polling_rate}")
    private void setPollingRate(Long pollingRate){
        ConfigReader.pollingRate = pollingRate;
    }

    @Value("${websocket.endpoint.path}")
    public void setWebSocketEndpoint(String webSocketEndpoint) { ConfigReader.webSocketEndpoint = webSocketEndpoint; }

}
