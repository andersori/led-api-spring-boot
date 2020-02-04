package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.TeamLed;
import io.andersori.led.api.domain.exception.DomainException;

public interface TeamLedService extends Service<TeamLed, TeamDTO> {

	List<TeamLed> find(GroupDTO group) throws DomainException;

	List<TeamLed> find(EventDTO event) throws DomainException;

	TeamLed updateGroup(Long id, Long idGroup, String secret) throws DomainException;

	TeamLed updateVerified(Long id, Boolean verified, String secret) throws DomainException;
}
