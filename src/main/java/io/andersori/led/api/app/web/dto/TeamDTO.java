package io.andersori.led.api.app.web.dto;

import java.util.Arrays;
import java.util.List;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.TeamLed;
import lombok.Data;

@Data
public class TeamDTO implements DTO<TeamLed, TeamDTO> {

	private Long id;
	private String name;
	private boolean verified;
	private List<String> participants;
	private Integer score;
	private String secret;
	private Long groupId;
	private Long eventId;

	@Override
	public TeamDTO toDTO(TeamLed entity) {
		id = entity.getId();
		name = entity.getName();
		verified = entity.isVerified();
		score = entity.getScore();
		secret = entity.getSecret();
		groupId = entity.getGroup().getId();
		eventId = entity.getEvent().getId();

		participants = Arrays.asList(entity.getParticipants().split(";"));
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

		String participants = "";
		for (String p : this.participants) {
			participants += ";" + p;
		}

		entity.setParticipants(participants);
		return entity;

	}

}
