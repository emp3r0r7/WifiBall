package org.emp3r0r7.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {


    @Value("${frontend.websocket.endpoint.path}")
    private String webSocketEndpoint;

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/realtime-data")
    public String getRealTimeDataPage(Model model) {
        // Aggiungi le proprietà al modello per Thymeleaf
        model.addAttribute("webSocketEndpoint", webSocketEndpoint);
        model.addAttribute("port", port);
        return "realtime-data";
    }
}
