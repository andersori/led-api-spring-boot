package io.andersori.led.api.domain.service;

import java.util.Optional;

import io.andersori.led.api.domain.entity.Event;

public interface EventService extends Service<Event> {
	
	Optional<Event> find(String name);
}
