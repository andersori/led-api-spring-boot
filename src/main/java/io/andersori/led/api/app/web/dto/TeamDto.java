package io.andersori.led.api.app.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeamDto {
	
	private Long id;
	private String name;
	private List<String> participants;
	private Integer score;
	private String secret;
	private GroupDto group;
	private EventDto event;
	
}
