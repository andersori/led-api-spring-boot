package io.andersori.led.api.app.web.dto;

import java.util.Arrays;
import java.util.List;

import io.andersori.led.api.domain.entity.TeamLed;
import lombok.Data;

@Data
public class TeamDto implements Dto<TeamLed, TeamDto> {

	private Long id;
	private String name;
	private List<String> participants;
	private Integer score;
	private String secret;
	private GroupDto group;
	private EventDto event;
	
	@Override
	public TeamDto toDto(TeamLed entity) {
		id = entity.getId();
		name = entity.getName();
		score = entity.getScore();
		secret = entity.getSecret();
		group = new GroupDto().toDto(entity.getGroup());
		event = new EventDto().toDto(entity.getEvent());
		
		participants = Arrays.asList(entity.getParticipants().split(";"));
		return this;
	}
	
	@Override
	public TeamLed toEntity() {
		TeamLed entity = new TeamLed();
		entity.setId(id);
		entity.setName(name);
		entity.setScore(score);
		entity.setSecret(secret);
		entity.setGroup(group.toEntity());
		entity.setEvent(event.toEntity());
		
		String participants = "";
		for (String p : this.participants) {
			participants += ";" + p;
		}
		
		entity.setParticipants(participants);
		return entity;
	}

}
