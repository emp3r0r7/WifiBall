package org.emp3r0r7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataReading {

    private GyroData gyroData;

    private int highestPower;

    private AccessPoint accessPoint;

    private List<Station> associatedStations;

}
