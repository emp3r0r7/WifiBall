package org.emp3r0r7.configuration;

import org.emp3r0r7.process.AirodumpProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInit.class);

    @Override
    public void run(String... args){

        logger.info("Invoked application initializer now..");

        AirodumpProcess airodumpProcess = new AirodumpProcess();
        //airodumpProcess.run();

    }
}
