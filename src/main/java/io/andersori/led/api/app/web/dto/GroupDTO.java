package io.andersori.led.api.app.web.dto;

import java.util.Arrays;

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

	@Override
	public GroupDTO toDTO(GroupLed entity) {
		id = entity.getId();
		eventId = entity.getEvent().getId();
		name = entity.getName();
		return this;
	}

	public GroupLed toEntity(Event event) {
		GroupLed entity = new GroupLed();
		entity.setId(id);
		entity.setEvent(event);
		entity.setName(name);
		entity.setTeams(Arrays.asList());
		return entity;
	}

}
