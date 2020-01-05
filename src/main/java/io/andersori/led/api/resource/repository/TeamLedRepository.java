package io.andersori.led.api.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.andersori.led.api.domain.entity.TeamLed;

public interface TeamLedRepository extends JpaRepository<TeamLed, Long> {
	
	List<TeamLed> findByGroupId(Long id);
	
	List<TeamLed> findByEventId(Long id);

}
