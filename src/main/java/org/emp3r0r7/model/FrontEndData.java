package org.emp3r0r7.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class FrontEndData {

    private GyroData gyroData = new GyroData(); //instantaneous gyro data when sent to front-end

    private Map<String, ApData> wifiDataMap = new ConcurrentHashMap<>();

    private Map<String, StationData> notAssociatedStationDataMap = new ConcurrentHashMap<>();

}
