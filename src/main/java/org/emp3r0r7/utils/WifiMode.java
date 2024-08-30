package org.emp3r0r7.utils;

import lombok.Getter;

@Getter
public enum WifiMode {

    TYPE_MONITOR("type monitor"),
    TYPE_MANAGED("type managed");

    private final String type;

    WifiMode (String type){this.type = type;}

}
