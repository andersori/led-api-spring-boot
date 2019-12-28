package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.andersori.led.api.domain.entity.GroupLed;

public interface GroupLedRepository extends JpaRepository<GroupLed, Long> {
	
	Optional<GroupLed> findByName(String name);
	
	Optional<GroupLed> findByEventId(Long id);
}
