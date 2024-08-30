package org.emp3r0r7.configuration;

import lombok.AllArgsConstructor;
import org.emp3r0r7.process.AirodumpProcess;
import org.emp3r0r7.process.IProcess;
import org.emp3r0r7.shared.SharedState;
import org.emp3r0r7.thread.ExecutorOrchestrator;
import org.emp3r0r7.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

import static org.emp3r0r7.configuration.ConfigProperties.NETWORK_CARD;

@Component
@AllArgsConstructor
public class ApplicationInit implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInit.class);

    private final ConfigurationFileManager configurationFileManager;

    private final ExecutorOrchestrator orchestrator;

    private final SharedState sharedState;

    @Override
    public void run(String... args){

        long startTime = System.currentTimeMillis();

        LOGGER.info("Invoked application initializer now..");

        //leggere dal file di configurazione
        Properties properties = configurationFileManager.loadConfiguration();
        String networkCard = properties.getProperty(NETWORK_CARD.getProp());

        if(ObjectUtils.isEmpty(networkCard)) {
            LOGGER.error("Configuration Parameter {} not set! Please edit your configuration file at : {}", NETWORK_CARD.getProp(), ConfigurationFileManager.CONFIG_DIR);
            System.exit(1);
        }

        sharedState.getNetworkCardInUse().set(networkCard);

        //process submission
        IProcess airodump = new AirodumpProcess(networkCard, process -> {
            //if the process terminates instantly, the application halts
            if(!DateUtils.isTimeExceeded(startTime, 1)){
                LOGGER.error(
                        "Airodump process is not running, check if the airodump suite is installed and check " +
                        "if the interface you set is correct! | Exit code : {}", process.exitValue()
                );
                System.exit(1);
            }
        });

        orchestrator.submitTask(airodump);

    }
}
