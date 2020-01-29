package io.andersori.led.api.app.web.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class EventDTO implements DTO<Event, EventDTO> {

	@Setter(AccessLevel.PRIVATE)
	private Long id;
	private String ownerUsername;
	private String name;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date;
	private String description;
	@Setter(AccessLevel.PRIVATE)
	private List<GroupDTO> groups = new ArrayList<GroupDTO>();

	public EventDTO(String ownerUsername, String name, LocalDate date, String description) {
		this.ownerUsername = ownerUsername;
		this.name = name;
		this.date = date;
		this.description = description;
	}

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
