package io.andersori.led.api.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.andersori.led.api.domain.entity.GroupLed;

public interface GroupLedRepository extends JpaRepository<GroupLed, Long>, JpaSpecificationExecutor<GroupLed> {

	List<GroupLed> findByEventId(Long id);

}
