package io.andersori.led.api.app.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeamMin {
	private String name;
	private List<String> participants;
}
