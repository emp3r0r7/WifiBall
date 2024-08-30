package org.emp3r0r7.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Station {

    private String stationMac;
    private String firstTimeSeen;
    private String lastTimeSeen;
    private int power;
    private int packets;
    private String bssid;
    private String probedEssids;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(stationMac, station.stationMac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationMac);
    }

}
