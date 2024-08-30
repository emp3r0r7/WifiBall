package org.emp3r0r7.configuration;

import lombok.Getter;

@Getter
public enum ConfigProperties {

    APPLICATION_NAME("application.name"),
    NETWORK_CARD("network.card");

    private final String prop;

    ConfigProperties(String prop) {this.prop = prop;}

}
