package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.entity.GroupLed;

public interface GroupLedService extends Service<GroupLed, GroupDTO> {
	
	List<GroupLed> find(String name);
	
	List<GroupLed> find(EventDTO event);
}
