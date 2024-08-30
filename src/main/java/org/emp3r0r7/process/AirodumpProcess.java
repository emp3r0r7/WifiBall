package org.emp3r0r7.process;


import lombok.Getter;
import lombok.Setter;
import org.emp3r0r7.configuration.ConfigReader;
import org.emp3r0r7.thread.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Future;

@Getter
public class AirodumpProcess implements IProcess {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirodumpProcess.class);

    private final static Long POLLING_RATE = ConfigReader.getPollingRate();

    private final static String TEMP_PATH = ConfigReader.getTempPath() + AirodumpProcess.class.getSimpleName().toLowerCase();

    private final String [] command;

    @Setter
    private String processName;

    @Setter
    private Future<?> future;

    private Process process;

    private Long pid;

    private Integer exitCode;

    private final String networkCard;

    private final ProcessExitCallback processExitCallback;

    public AirodumpProcess(String networkCard, ProcessExitCallback callback){
        this.processName = RequiredProcess.AIRODUMP.getProcessName();
        this.networkCard = networkCard;
        this.processExitCallback = callback;
        command = new String[]{"sudo", "airodump-ng", networkCard, "-w", TEMP_PATH, "--write-interval", String.valueOf(POLLING_RATE), "-o", "csv"};
    }

    @Override
    public boolean isTaskRunning(){
        if(process != null && process.isAlive()){
            LOGGER.error("Process has not terminated its work yet, cannot continue..");
            return true;
        }

        return false;
    }

    @Override
    public void run() {

        if(isTaskRunning()) return;
        LOGGER.info("{} initiated!", this.getClass().getSimpleName());

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            LOGGER.info("{} launching command : {}", this.getClass().getSimpleName(), command);

            this.process = processBuilder.start();
            ThreadUtils.addShutdownHook(process, LOGGER);
            this.process.onExit().join();

            this.pid = process.pid();
            this.exitCode = process.exitValue();

            if(this.processExitCallback != null)
                processExitCallback.onProcessComplete(process);

            LOGGER.info("Process started with pid {}", pid);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
