package org.emp3r0r7.thread;


import jakarta.annotation.PreDestroy;
import org.emp3r0r7.process.IProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Component
public class ExecutorOrchestrator {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorOrchestrator.class);

    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, IProcess> tasks;

    public ExecutorOrchestrator() {
        this.executorService = Executors.newCachedThreadPool();
        this.tasks = new ConcurrentHashMap<>();
    }

    public void submitTask(IProcess process) {

        String id = process.getProcessName();
        IProcess sessionProcess = tasks.get(id);

        if(sessionProcess == null){
            logger.info("Task assigned to : {} is new, proceeding...", id);
            Future<?> future = executorService.submit(process);
            process.setFuture(future); //memorizzo il Future nell'istanza di IProcess
            tasks.put(id, process);
            logger.info("Submitted new Task: {} | {}", id, process.getClass().getName());
        } else {
            logger.warn("Task is already assigned to : {} not proceeding!", id);
        }
    }

    public void cancelTask(String id) {
        logger.warn("Initiating task cancellation for : {}", id);

        IProcess process = tasks.get(id);
        if (process != null && process.getFuture() != null) {
            process.getFuture().cancel(true);  // Cancella il task
            tasks.remove(id);
            logger.warn("Task : {} cancelled!", id);
        }

    }


    public boolean isTaskRunning(String id) {
        IProcess session = tasks.get(id);
        return session != null && session.getFuture() != null && !session.getFuture().isDone();
    }

    public IProcess getProcess(String id) {
        return tasks.get(id);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }
}
