package org.emp3r0r7.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class FrontEndData {

    private GyroData gyroData = new GyroData();

    private Map<String, DataReading> wifiDataMap = new ConcurrentHashMap<>();

}
