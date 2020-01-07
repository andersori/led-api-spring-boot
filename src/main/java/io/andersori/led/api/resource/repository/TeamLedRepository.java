package io.andersori.led.api.resource.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.andersori.led.api.domain.entity.TeamLed;

public interface TeamLedRepository extends JpaRepository<TeamLed, Long> {
	
	List<TeamLed> findByGroupId(Long id);
	
	List<TeamLed> findByEventId(Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE TeamLed t SET t.group.id = :idGroup WHERE t.id = :idTeam")
	Integer changeGroup(Long idTeam, Long idGroup);

}
