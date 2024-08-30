package org.emp3r0r7.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class AccessPoint {

    private String bssid;
    private String firstTimeSeen;
    private String lastTimeSeen;
    private int channel;
    private int speed;
    private String privacy;
    private String cipher;
    private String authentication;
    private int power;
    private int beacons;
    private int iv;
    private String lanIp;
    private int idLength;
    private String essid;
    private String key;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessPoint that = (AccessPoint) o;
        return Objects.equals(bssid, that.bssid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bssid);
    }

}
