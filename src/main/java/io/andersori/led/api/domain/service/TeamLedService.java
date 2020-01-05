package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;

public interface TeamLedService extends Service<TeamDTO> {
	
	List<TeamDTO> find(GroupLed group);
	
	List<TeamDTO> find(Event event);
	
}
