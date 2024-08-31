package org.emp3r0r7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

    @GetMapping("/realtime-data")
    public String getRealTimeDataPage() {
        return "realtime-data";  // Nome del file HTML da servire
    }
}
