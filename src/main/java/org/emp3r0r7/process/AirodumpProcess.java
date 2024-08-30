package org.emp3r0r7.process;


import lombok.Getter;
import org.emp3r0r7.configuration.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Getter
public class AirodumpProcess implements IProcess {

    private static final Logger logger = LoggerFactory.getLogger(AirodumpProcess.class);

    private Process process;

    private Long pid;

    private final static Long pollingRate = ConfigReader.getPollingRate();

    private final static String tempPath = ConfigReader.getTempPath() + AirodumpProcess.class.getSimpleName().toLowerCase();

    //setup wlan, todo give possibility to choose network card for dumping
    private final static String [] command = {"sudo", "airodump-ng", "wlan0mon", "-w", tempPath, "--write-interval", String.valueOf(pollingRate), "-o", "csv"};

    @Override
    public void run() {

        if(process != null && process.isAlive()){
            logger.error("Process has not terminated its work yet, cannot continue..");
            return;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            process = processBuilder.start();
            pid = process.pid();
            logger.info("Process started with pid {}", pid);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
