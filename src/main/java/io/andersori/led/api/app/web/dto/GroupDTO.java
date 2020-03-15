package io.andersori.led.api.app.web.dto;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class GroupDTO implements DTO<GroupLed, GroupDTO> {

	@Setter(AccessLevel.PRIVATE)
	private Long id;
	private Long eventId;
	private String name;
	@Setter(AccessLevel.PRIVATE)
	private List<TeamMin> teams;

	@Override
	public GroupDTO toDTO(GroupLed entity) {
		id = entity.getId();
		eventId = entity.getEvent().getId();
		name = entity.getName();
		teams = entity.getTeams().stream().map(t -> {
			TeamMin tMin = new TeamMin();
			tMin.setName(t.getName());
			tMin.setParticipants(t.getParticipants().stream().map(p -> p.getName()).collect(Collectors.toList()));
			return tMin;
		}).collect(Collectors.toList());
		return this;
	}

	public GroupLed toEntity(Event event) {
		GroupLed entity = new GroupLed();
		entity.setId(id);
		entity.setEvent(event);
		entity.setName(name);
		entity.setTeams(new HashSet<>());
		return entity;
	}

}
