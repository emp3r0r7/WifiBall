package org.emp3r0r7.schedule;

import lombok.AllArgsConstructor;
import org.emp3r0r7.process.AirodumpProcess;
import org.emp3r0r7.shared.SharedState;
import org.emp3r0r7.thread.ExecutorOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.emp3r0r7.process.RequiredProcess.AIRODUMP;

@Component
@AllArgsConstructor
public class DumpScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpScheduler.class);

    private final SharedState sharedState;

    private final ExecutorOrchestrator executorOrchestrator;

    @Scheduled(fixedDelayString = "${scheduler.dump_scheduler.polling_rate}", initialDelay = 5000)
    private void doWork(){

        String gyroState = sharedState.getLastGyroSensorState().get();

        if(!executorOrchestrator.isTaskRunning(AIRODUMP.getProcessName())){
            LOGGER.warn("Airodump process is NOT running, restoring..");
            executorOrchestrator.cancelTask(AIRODUMP.getProcessName());
            executorOrchestrator.submitTask(new AirodumpProcess(sharedState.getNetworkCardInUse().get(), null));
        }

        //per ogni ciclo di polling, leggere file output csv e interpreta dati
        if(gyroState != null)
            LOGGER.info("Last Gyro Reading : {}", gyroState);

        else {
            LOGGER.warn("Gyro Reading is null, check application!");
            return; //gyro not present, skipping cycle
        }

        //todo remove controllo xyz corretti, tutto okay si procede
        var proc = executorOrchestrator.getProcess(AIRODUMP.getProcessName());
        System.out.println(proc);
        boolean test = executorOrchestrator.isTaskRunning(AIRODUMP.getProcessName());
        System.out.println(test);

    }


}
