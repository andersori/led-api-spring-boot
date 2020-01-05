package io.andersori.led.api.resource.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.andersori.led.api.domain.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	Page<Event> findByNameContaining(String name, Pageable pageable);
	
}
