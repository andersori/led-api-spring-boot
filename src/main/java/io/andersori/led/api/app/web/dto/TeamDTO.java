package io.andersori.led.api.app.web.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private String name;
	private boolean verified = false;
	private List<String> participants = new ArrayList<String>();
	private Integer score = 0;
	
	@Setter(AccessLevel.PRIVATE)
	private String secret = HelperFacade.secretGenerator();
	
	private Long groupId;
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

		StringBuilder participants = new StringBuilder();
		for(int i = 0; i < this.participants.size(); i++) {
			participants.append(this.participants.get(i));
			if(i != this.participants.size()-1) {
				participants.append(";");
			}
		}
		entity.setParticipants(participants.toString());
		return entity;

	}

}
