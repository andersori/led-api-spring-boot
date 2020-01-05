package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.entity.Event;

public interface GroupLedService extends Service<GroupDTO> {
	
	List<GroupDTO> find(String name);
	
	List<GroupDTO> find(Event event);
}
