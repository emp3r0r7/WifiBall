package org.emp3r0r7.utils;

public class DateUtils {

    public static boolean isTimeExceeded(long startTime, int seconds) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - startTime;
        return timeDiff > seconds * 1000L;
    }
}
