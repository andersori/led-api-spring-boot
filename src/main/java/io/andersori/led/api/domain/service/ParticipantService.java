package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.Participant;
import io.andersori.led.api.domain.exception.DomainException;

public interface ParticipantService extends Service<Participant, ParticipantDTO> {

	List<Participant> find(EventDTO event);

	Participant updateName(Long id, ParticipantDTO parti) throws DomainException;

	void updateTeam(ParticipantDTO parti, TeamDTO team) throws DomainException;

	Participant random(Long id, String secret) throws DomainException;

	List<Participant> shuffle(Long idEvent) throws DomainException;

	Participant findWithSecret(Long id, String secret) throws DomainException;

	Participant setTeamNull(Long id) throws DomainException;

	List<Participant> reverseShuffle(Long idEvent) throws DomainException;
}
