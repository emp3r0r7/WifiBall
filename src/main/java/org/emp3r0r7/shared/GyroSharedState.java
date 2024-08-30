package org.emp3r0r7.shared;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@Getter
public class GyroSharedState {

    private final AtomicReference<String> lastSensorState = new AtomicReference<>();

}
