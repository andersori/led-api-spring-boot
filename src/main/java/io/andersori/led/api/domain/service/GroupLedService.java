package io.andersori.led.api.domain.service;

import java.util.Optional;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;

public interface GroupLedService extends Service<GroupLed> {
	
	Optional<GroupLed> get(String name);
	
	Optional<GroupLed> get(Event event);
}
