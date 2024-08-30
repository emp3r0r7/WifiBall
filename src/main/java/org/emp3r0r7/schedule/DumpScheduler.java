package org.emp3r0r7.schedule;

import lombok.AllArgsConstructor;
import org.emp3r0r7.shared.GyroSharedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DumpScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DumpScheduler.class);

    private final GyroSharedState gyroSharedState;

    @Scheduled(fixedDelayString = "${scheduler.dump_scheduler.polling_rate}")
    private void doWork(){

        String gyroState = gyroSharedState.getLastSensorState().get();

        //per ogni ciclo di polling, leggere file output csv e interpreta dati
        if(gyroState != null)
            logger.info("Last Gyro Reading : {}", gyroState);

        else
            logger.warn("Gyro Reading is null, check application!");


    }


}
