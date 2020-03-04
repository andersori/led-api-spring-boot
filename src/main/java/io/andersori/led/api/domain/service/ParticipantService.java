package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.domain.entity.Participant;

public interface ParticipantService extends Service<Participant, ParticipantDTO>{
	
	List<Participant> find(EventDTO event);
}
