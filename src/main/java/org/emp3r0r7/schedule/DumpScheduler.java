package org.emp3r0r7.schedule;

import org.apache.commons.lang3.StringUtils;
import org.emp3r0r7.filesystem.CsvParser;
import org.emp3r0r7.filesystem.FileSystemEngine;
import org.emp3r0r7.model.*;
import org.emp3r0r7.process.AirodumpProcess;
import org.emp3r0r7.process.IProcess;
import org.emp3r0r7.shared.SharedState;
import org.emp3r0r7.thread.ExecutorOrchestrator;
import org.emp3r0r7.utils.StringUtil;
import org.emp3r0r7.websocket.FrontendWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static org.emp3r0r7.filesystem.CsvParser.NOT_ASSOCIATED;
import static org.emp3r0r7.filesystem.FileSystemEngine.TEMP_PATH;
import static org.emp3r0r7.process.RequiredProcess.AIRODUMP;
import static org.emp3r0r7.utils.DateUtils.isTimeExceeded;

@Component
public class DumpScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpScheduler.class);

    private final SharedState sharedState;

    private final ExecutorOrchestrator orchestrator;

    private final FrontendWebSocketHandler webSocketHandler;

    private Long previousEpoch = null;

    private File file = null;

    //log threshold
    long lastLogTime = 0;
    long logInterval = 5000; // 5 seconds

    public DumpScheduler(SharedState sharedState,
                         ExecutorOrchestrator orchestrator,
                         FrontendWebSocketHandler webSocketHandler){
        this.sharedState = sharedState;
        this.orchestrator = orchestrator;
        this.webSocketHandler = webSocketHandler;
    }

    @Scheduled(fixedDelayString = "${scheduler.dump_scheduler.polling_rate}", initialDelay = 5000)
    private void doWork(){

        String gyroState = this.sharedState.getLastGyroSensorState().get();
        GyroData gyroData;

        if(!orchestrator.isTaskRunning(AIRODUMP.getProcessName())){
            //todo implement max retry limit
            LOGGER.warn("Airodump process is NOT running, restoring..");
            orchestrator.cancelTask(AIRODUMP.getProcessName());
            orchestrator.submitTask(new AirodumpProcess(sharedState.getNetworkCardInUse().get(), null));
        }

        //per ogni ciclo di polling, leggere file output csv e interpreta dati
        if(gyroState != null){
            //LOGGER.info("Last Gyro Reading : {}", gyroState);
            gyroData = StringUtil.extractGyroData(gyroState);

            if(gyroData != null && isTimeExceeded(gyroData.getReadEpoch(), 2)){
                this.thresholdLogging("Gyro Reading is expired!, check Android application!", Level.WARN);
                return;
            }

            this.sharedState.getFrontEndData().setGyroData(gyroData);
        }

        else {
            this.thresholdLogging("Gyro Reading is null, check Android application!", Level.WARN);
            return; //gyro not present, skipping cycle
        }

        IProcess proc = orchestrator.getProcess(AIRODUMP.getProcessName());
        file = this.evaluateProcEpoch(proc.getEpoch(), file);

        List<AccessPoint> accessPoints = CsvParser.parseAccessPoints(file.getPath());
        List<Station> stations = CsvParser.parseStations(file.getPath());

        List<Station> notAssociatedStations = stations.stream()
                .filter(station -> {
                    String stationBssid = station.getBssid();
                    return StringUtils.isNotBlank(stationBssid) && stationBssid.contains(NOT_ASSOCIATED);
                }).toList();

        this.cycleAccessPoints(accessPoints, stations);
        this.cycleNotAssociatedStations(notAssociatedStations);

        webSocketHandler.sendDataToClient();
        this.thresholdLogging(String.valueOf(this.sharedState.getFrontEndData().getGyroData()), Level.INFO);

    }

    private void cycleNotAssociatedStations(List<Station> notAssociatedStations){

        for(Station station : notAssociatedStations){

            String stationMac = station.getStationMac();

            this.sharedState.getFrontEndData().getNotAssociatedStationDataMap().compute(stationMac , (macAddress, stationData) -> {

                int stationPower = station.getPower();

                if(stationData == null)
                    stationData = new StationData(stationPower, station);

                else {
                    if(stationPower > stationData.getHighestPower()){
                        stationData.setHighestPower(stationPower);
                        stationData.setStation(station);
                    }
                }

                return stationData;
            });
        }
    }

    private void cycleAccessPoints(List<AccessPoint> accessPoints, List<Station> stations){

        for(AccessPoint ap : accessPoints){

            String bssid = ap.getBssid();

            List<Station> associatedStations = stations
                    .stream()
                    .filter(station -> {

                        String stationBssid = station.getBssid();
                        String apBssid = ap.getBssid();

                        if(StringUtils.isNotBlank(stationBssid) && StringUtils.isNotBlank(apBssid))
                            return stationBssid.equals(apBssid);

                        return false;

                    }).toList();

            this.sharedState.getFrontEndData().getWifiDataMap().compute(bssid , (macAddress, apData) -> {

                int apPower = ap.getPower();

                if (apData == null) // Se dataReading è null, creiamo un nuovo oggetto DataReading
                    apData = new ApData(apPower, ap, associatedStations);

                else {
                    // se la potenza è maggiore, aggiorna la potenza e le stazioni filtrate
                    // if the power is greater, updates the power and its filtered stations
                    if (apPower > apData.getHighestPower()) {
                        apData.setHighestPower(apPower);
                        apData.setAccessPoint(ap);
                    }

                    apData.setAssociatedStations(associatedStations);

                }
                return apData;
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

    private void thresholdLogging(String logMessage, Level level){
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastLogTime >= logInterval){

            switch (level){

                case WARN -> LOGGER.warn(logMessage);

                case INFO -> LOGGER.info(logMessage);

                case DEBUG -> LOGGER.debug(logMessage);

                case ERROR -> LOGGER.error(logMessage);
            }

            lastLogTime = currentTime;
        }
    }

}
