package org.emp3r0r7.schedule;

import org.apache.commons.lang3.StringUtils;
import org.emp3r0r7.filesystem.CsvParser;
import org.emp3r0r7.filesystem.FileSystemEngine;
import org.emp3r0r7.model.AccessPoint;
import org.emp3r0r7.model.DataReading;
import org.emp3r0r7.model.GyroData;
import org.emp3r0r7.model.Station;
import org.emp3r0r7.process.AirodumpProcess;
import org.emp3r0r7.process.IProcess;
import org.emp3r0r7.shared.SharedState;
import org.emp3r0r7.thread.ExecutorOrchestrator;
import org.emp3r0r7.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.emp3r0r7.filesystem.FileSystemEngine.TEMP_PATH;
import static org.emp3r0r7.process.RequiredProcess.AIRODUMP;

@Component
public class DumpScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpScheduler.class);

    private final SharedState sharedState;

    private final ExecutorOrchestrator orchestrator;

    private Long previousEpoch = null;

    private File file = null;

    private final Map<String, DataReading> readingMap = new HashMap<>();

    public DumpScheduler(SharedState sharedState, ExecutorOrchestrator orchestrator){
        this.sharedState = sharedState;
        this.orchestrator = orchestrator;
    }

    @Scheduled(fixedDelayString = "${scheduler.dump_scheduler.polling_rate}", initialDelay = 5000)
    private void doWork(){

        String gyroState = sharedState.getLastGyroSensorState().get();
        GyroData gyroData;

        if(!orchestrator.isTaskRunning(AIRODUMP.getProcessName())){
            LOGGER.warn("Airodump process is NOT running, restoring..");
            orchestrator.cancelTask(AIRODUMP.getProcessName());
            orchestrator.submitTask(new AirodumpProcess(sharedState.getNetworkCardInUse().get(), null));
        }

        //per ogni ciclo di polling, leggere file output csv e interpreta dati
        if(gyroState != null){
            LOGGER.info("Last Gyro Reading : {}", gyroState);
            gyroData = StringUtil.extractGyroData(gyroState);
        }

        else {
            LOGGER.warn("Gyro Reading is null, check Android application!");
            gyroData = null;
            //return; //gyro not present, skipping cycle
        }


        //todo remove controllo xyz corretti, tutto okay si procede
        IProcess proc = orchestrator.getProcess(AIRODUMP.getProcessName());
        file = this.evaluateProcEpoch(proc.getEpoch(), file);

        List<AccessPoint> accessPoints = CsvParser.parseAccessPoints(file.getPath());
        List<Station> stations = CsvParser.parseStations(file.getPath());

        for(AccessPoint ap : accessPoints){

            String bssid = ap.getBssid();

            List<Station> filteredStations = stations
                    .stream()
                    .filter(station -> {

                        String stationBssid = station.getBssid();
                        String apBssid = ap.getBssid();

                        if(StringUtils.isNotBlank(stationBssid) && StringUtils.isNotBlank(apBssid))
                            return stationBssid.equals(apBssid);

                        return false;

                    }).toList();

            readingMap.compute(bssid , (accessPoint, dataReading) -> {

                int apPower = ap.getPower();

                if (dataReading == null) {
                    // Se dataReading è null, creiamo un nuovo oggetto DataReading
                    dataReading = new DataReading(gyroData, apPower, ap, filteredStations);

                } else {
                    // se la potenza è maggiore, aggiorna la potenza e le stazioni filtrate
                    // if the power is greater, updates the power and its filtered stations
                    if (apPower > dataReading.getHighestPower()) {
                        dataReading.setHighestPower(apPower);
                        dataReading.setAccessPoint(ap);
                    }

                    dataReading.setAssociatedStations(filteredStations);

                }
                return dataReading;
            });
        }
    }


    private File evaluateProcEpoch (Long currentEpoch, File file){

        if (!currentEpoch.equals(previousEpoch)) {
            file = FileSystemEngine.searchFileContainingString(TEMP_PATH, currentEpoch.toString());

            if(file == null){
                LOGGER.error("Cannot find generated csv file from Airodump, cannot proceed, exiting..");
                System.exit(1);
            }

            previousEpoch = currentEpoch;
            LOGGER.info("Currently pointing to: {}", file);
        }

        return file;

    }

}
