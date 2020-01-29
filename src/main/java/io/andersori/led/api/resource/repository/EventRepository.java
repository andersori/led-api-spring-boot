package io.andersori.led.api.resource.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.andersori.led.api.domain.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

	List<Event> findByOwnerUserUsername(String username);

	Page<Event> findByNameContaining(String name, Pageable pageable);

}
