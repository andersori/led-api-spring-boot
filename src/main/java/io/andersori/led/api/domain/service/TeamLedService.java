package io.andersori.led.api.domain.service;

import java.util.Optional;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.TeamLed;

public interface TeamLedService extends Service<TeamLed> {
	
	Optional<TeamLed> find(GroupLed group);
	
	Optional<TeamLed> find(Event event);
	
}