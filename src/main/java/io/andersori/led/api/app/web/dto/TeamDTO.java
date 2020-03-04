package io.andersori.led.api.app.web.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.HelperFacade;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.TeamLed;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class TeamDTO implements DTO<TeamLed, TeamDTO> {

	@Setter(AccessLevel.PRIVATE)
	private Long id;
	@Setter(AccessLevel.PRIVATE)
	private boolean verified = false;
	@Setter(AccessLevel.PRIVATE)
	private Integer score = 0;
	@Setter(AccessLevel.PRIVATE)
	private String secret = HelperFacade.secretGenerator();
	@Setter(AccessLevel.PRIVATE)
	private Long groupId;
	@Setter(AccessLevel.PRIVATE)
	private List<String> participants;

	private String name;
	private Long eventId;

	@Override
	public TeamDTO toDTO(TeamLed entity) {
		id = entity.getId();
		name = entity.getName();
		verified = entity.isVerified();
		score = entity.getScore();
		secret = entity.getSecret();
		groupId = entity.getGroup() != null ? entity.getGroup().getId() : null;
		eventId = entity.getEvent().getId();

		participants = entity.getParticipants() != null
				? entity.getParticipants().stream().map(part -> part.getName()).collect(Collectors.toList())
				: Arrays.asList();
		return this;
	}

	public TeamLed toEntity(GroupLed group, Event event) {
		TeamLed entity = new TeamLed();
		entity.setId(id);
		entity.setName(name);
		entity.setVerified(verified);
		entity.setScore(score);
		entity.setSecret(secret);
		entity.setGroup(group);
		entity.setEvent(event);

		return entity;
	}

}
