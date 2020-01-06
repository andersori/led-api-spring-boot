package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.TeamLed;

public interface TeamLedService extends Service<TeamLed, TeamDTO> {
	
	List<TeamLed> find(GroupLed group);
	
	List<TeamLed> find(Event event);
	
}
