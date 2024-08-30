package org.emp3r0r7.process;

import lombok.Getter;

@Getter
public enum RequiredProcess {

    AIRODUMP("airodump");
    //aggiungere altri se necessario

    private final String processName;

    RequiredProcess(String processName) {this.processName = processName;}

}
