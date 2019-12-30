package io.andersori.led.api.app.web.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.entity.GroupLed;
import lombok.Data;

@Data
public class GroupDto implements Dto<GroupLed, GroupDto>{
	
	private Long id;
	private EventDto event;
	private String name;
	private List<TeamDto> teams;
	
	@Override
	public GroupDto toDto(GroupLed entity) {
		id = entity.getId();
		event = new EventDto().toDto(entity.getEvent());
		name = entity.getName();
		
		teams = entity.getTeams().stream().map(t -> {
			return new TeamDto().toDto(t);
		}).collect(Collectors.toList());
		
		return this;
	}
	@Override
	public GroupLed toEntity() {
		GroupLed entity = new GroupLed();
		entity.setId(id);
		entity.setEvent(event.toEntity());
		entity.setName(name);
		entity.setTeams(teams.stream().map(t -> {
			return t.toEntity();
		}).collect(Collectors.toList()));
		return entity;
	}
	
}
