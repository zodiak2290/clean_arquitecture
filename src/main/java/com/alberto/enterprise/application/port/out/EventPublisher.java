package com.alberto.enterprise.application.port.out;

public interface EventPublisher {
	void publish(String topic, Object payload);
}
