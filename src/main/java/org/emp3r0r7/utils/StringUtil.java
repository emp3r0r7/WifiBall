package org.emp3r0r7.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String safeTrim(String value) {
        return StringUtils.isNotBlank(value) ? value.trim() : "";
    }

    public static Integer safeParseInt(String value, int defaultValue) {
        return StringUtils.isNotBlank(value) ? Integer.parseInt(value.trim()) : defaultValue;
    }

}
