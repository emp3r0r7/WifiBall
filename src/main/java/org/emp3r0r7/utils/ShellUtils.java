package org.emp3r0r7.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.emp3r0r7.utils.WifiMode.TYPE_MANAGED;
import static org.emp3r0r7.utils.WifiMode.TYPE_MONITOR;

public class ShellUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtils.class);

    private static final String MON = "mon";

    public static final String SEPARATOR = "$";

    /**
     * Controlla se la scheda di rete specificata è in modalità monitor.
     *
     * @param networkInterface Il nome della scheda di rete (es. wlan0).
     * @return true se la scheda di rete è in modalità monitor, false altrimenti.
     */
    public static WifiMode checkNetworkMode(String networkInterface) {

        String [] networkCardModes = new String[]{networkInterface, networkInterface + MON};

        for(String card : networkCardModes){

            String [] command = new String[]{"iw", "dev", card, "info"};

            try {

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.start().onExit().join();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.trim().contains(TYPE_MONITOR.getType()))
                        return TYPE_MONITOR;

                    if (line.trim().contains(TYPE_MANAGED.getType()))
                        return TYPE_MANAGED;
                }

            } catch (Exception e) {
                LOGGER.error("An error occurred during the execution of the command: {}", Arrays.toString(command));
            }

        }

        LOGGER.error("Cannot determine network card mode status make sure the wifi network card is connected and detected by your system, exiting..");
        System.exit(1);
        return null;
    }

    public static String setMonitorMode(String networkInterface){

        String [] command = new String[]{"sudo", "airmon-ng", "start", networkInterface};

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {

            Process process = processBuilder.start().onExit().join();

            if(process.exitValue() != 0){
                LOGGER.error("Cannot set network interface : {} to monitor mode, exiting..", networkInterface);
                System.exit(1);

            } else{
                LOGGER.info("Network interface : {} successfully set to monitor mode!", networkInterface);
                return networkInterface + MON;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return networkInterface;
    }


    public static String renameNetworkInterface(String networkInterface){

        if(!networkInterface.endsWith(MON))
            return networkInterface + MON;

        return networkInterface;
    }
}

