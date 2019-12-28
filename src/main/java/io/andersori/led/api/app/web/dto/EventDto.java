package io.andersori.led.api.app.web.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EventDto {
	
	private Long id;
	private AccountDto owner;
	private String name;
	private LocalDate date;
	private String description;
	private List<GroupDto> groups;
	
}
