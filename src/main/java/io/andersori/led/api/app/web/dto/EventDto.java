package io.andersori.led.api.app.web.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.andersori.led.api.domain.entity.Event;
import lombok.Data;

@Data
public class EventDto implements Dto<Event, EventDto> {

	private Long id;
	private AccountDto owner;
	private String name;
	private LocalDate date;
	private String description;
	private List<GroupDto> groups = Arrays.asList();

	@Override
	public EventDto toDto(Event entity) {
		id = entity.getId();

		owner = new AccountDto().toDto(entity.getOwner());

		name = entity.getName();
		date = entity.getDate();
		description = entity.getDescription();

		groups = entity.getGroups().stream().map(gp -> {
			return new GroupDto().toDto(gp);
		}).collect(Collectors.toList());
		
		return this;
	}

	@Override
	public Event toEntity() {
		Event entity = new Event();
		entity.setId(id);
		entity.setOwner(owner.toEntity());
		entity.setName(name);
		entity.setDate(date);
		entity.setDescription(description);
		entity.setGroups(groups.stream().map(gp -> {
			return gp.toEntity();
		}).collect(Collectors.toList()));
		return entity;
	}

}
