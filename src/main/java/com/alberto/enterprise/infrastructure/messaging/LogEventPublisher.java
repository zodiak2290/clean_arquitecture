package com.alberto.enterprise.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alberto.enterprise.application.port.out.EventPublisher;

@Component
public class LogEventPublisher  implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LogEventPublisher.class);

    @Override
    public void publish(String topic, Object payload) {
        log.info("EVENT topic={} payload={}", topic, payload);
    }
}
