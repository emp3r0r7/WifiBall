package org.emp3r0r7.shared;

import lombok.Getter;
import org.emp3r0r7.model.FrontEndData;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@Getter
public class SharedState {

    private final AtomicReference<String> lastGyroSensorState = new AtomicReference<>();

    private final AtomicReference<String> networkCardInUse = new AtomicReference<>();

    private final FrontEndData frontEndData = new FrontEndData();

}
