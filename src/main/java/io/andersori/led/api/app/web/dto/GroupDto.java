package io.andersori.led.api.app.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class GroupDto {
	
	private Long id;
	private EventDto event;
	private String name;
	private List<TeamDto> teams;
	
}
