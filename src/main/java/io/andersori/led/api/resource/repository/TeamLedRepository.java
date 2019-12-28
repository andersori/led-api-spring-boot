package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.andersori.led.api.domain.entity.TeamLed;

public interface TeamLedRepository extends JpaRepository<TeamLed, Long> {
	
	Optional<TeamLed> findByGroupId(Long id);
	
	Optional<TeamLed> findByEventId(Long id);

}
