package io.andersori.led.api.app.web.dto;

import io.andersori.led.api.domain.HelperFacade;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.Participant;
import io.andersori.led.api.domain.entity.TeamLed;
import lombok.Data;

@Data
public class ParticipantDTO implements DTO<Participant, ParticipantDTO> {

	private Long id;
	private String name;
	private String secret = HelperFacade.secretGenerator();
	private Long idTeam;
	private Long idEvent;

	@Override
	public ParticipantDTO toDTO(Participant entity) {
		id = entity.getId();
		name = entity.getName();
		secret = entity.getSecret();
		idTeam = entity.getTeam().getId();
		idEvent = entity.getEvent().getId();
		return this;
	}

	public Participant toEntity(TeamLed team, Event event) {
		Participant entity = new Participant();
		entity.setName(name);
		entity.setSecret(secret);
		entity.setTeam(team);
		entity.setEvent(event);
		return entity;
	}
}
