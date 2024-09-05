package org.emp3r0r7.utils;

import org.apache.commons.lang3.StringUtils;
import org.emp3r0r7.model.GyroData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    public static String safeTrim(String value) {
        return StringUtils.isNotBlank(value) ? value.trim() : "";
    }

    public static Integer safeParseInt(String value, int defaultValue) {
        return StringUtils.isNotBlank(value) ? Integer.parseInt(value.trim()) : defaultValue;
    }

    private static String cleanGyroData(String axis){
        return axis
                .replaceAll(",", ".")
                .replaceAll("[^0-9.-]", "");
    }

    public static GyroData extractGyroData(String gyroState){

        //Y=3.0328019|X=-2.373595|Z=151.55737
        String[] split = gyroState.split("\\|");

        try {
            if(split.length == 4){

                GyroData gyroData = new GyroData();
                String y = split[0];
                String x = split[1];
                String z = split[2];

                Long epochReading = Long.valueOf(split[3]);

                gyroData.setAxisY(Double.valueOf(cleanGyroData(y)));
                gyroData.setAxisX(Double.valueOf(cleanGyroData(x)));
                gyroData.setAxisZ(Double.valueOf(cleanGyroData(z)));

                gyroData.setReadEpoch(epochReading);
                return gyroData;
            }
        } catch (NumberFormatException e) {
            LOGGER.error("Error parsing gyro state", e);
        }

        return null;

    }

}
