package org.emp3r0r7.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DumpScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DumpScheduler.class);

    @Scheduled(fixedDelayString = "${scheduler.dump_scheduler.polling_rate}")
    private void doWork(){
        //per ogni ciclo di polling, leggere file output csv e interpreta dati

    }


}
