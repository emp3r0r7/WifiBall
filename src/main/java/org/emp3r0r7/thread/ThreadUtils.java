package org.emp3r0r7.thread;

import org.slf4j.Logger;

public class ThreadUtils {

    public static void addShutdownHook(Process process, Logger logger){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process.isAlive()) {
                logger.warn("Application is shutting down, terminating process with pid {}", process.pid());
                process.destroyForcibly();
            }
        }));
    }


}
