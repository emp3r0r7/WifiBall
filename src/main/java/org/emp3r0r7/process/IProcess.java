package org.emp3r0r7.process;

import java.util.concurrent.Future;

public interface IProcess extends Runnable {

    void run();

    boolean isTaskRunning();

    String[] getCommand();

    String getProcessName();

    void setProcessName(String processName);

    Future<?> getFuture();

    void setFuture(Future<?> future);

    Long getPid();

    Integer getExitCode();

    @FunctionalInterface
    interface ProcessExitCallback { void onProcessComplete(Process process);}

}
