package org.emp3r0r7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GyroData {

    private Double axisX;
    private Double axisY;
    private Double axisZ;

    private Long readEpoch;

}
