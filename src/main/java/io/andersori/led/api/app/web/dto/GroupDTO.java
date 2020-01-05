package io.andersori.led.api.app.web.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.entity.GroupLed;
import lombok.Data;

@Data
public class GroupDTO implements DTO<GroupLed, GroupDTO>{
	
	private Long id;
	private EventDTO event;
	private String name;
	private List<TeamDTO> teams;
	
	@Override
	public GroupDTO toDTO(GroupLed entity) {
		id = entity.getId();
		event = new EventDTO().toDTO(entity.getEvent());
		name = entity.getName();
		
		teams = entity.getTeams().stream().map(t -> {
			return new TeamDTO().toDTO(t);
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
