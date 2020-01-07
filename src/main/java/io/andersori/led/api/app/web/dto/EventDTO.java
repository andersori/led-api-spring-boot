package io.andersori.led.api.app.web.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.Event;
import lombok.Data;

@Data
public class EventDTO implements DTO<Event, EventDTO> {

	private Long id;
	private String ownerUsername;
	private String name;
	private LocalDate date;
	private String description;
	private List<GroupDTO> groups = new ArrayList<GroupDTO>();

	@Override
	public EventDTO toDTO(Event entity) {
		id = entity.getId();
		ownerUsername = entity.getOwner().getUser().getUsername();
		name = entity.getName();
		date = entity.getDate();
		description = entity.getDescription();

		groups = entity.getGroups().stream().map(gp -> {
			return new GroupDTO().toDTO(gp);
		}).collect(Collectors.toList());

		return this;
	}

	public Event toEntity(Account owner) {
		Event entity = new Event();
		entity.setId(id);
		entity.setOwner(owner);
		entity.setName(name);
		entity.setDate(date);
		entity.setDescription(description);
		entity.setGroups(groups.stream().map(gp -> {
			return gp.toEntity(entity);
		}).collect(Collectors.toList()));
		return entity;

	}

}
