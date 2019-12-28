package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.andersori.led.api.domain.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	Optional<Event> findByName(String name);
	
}
