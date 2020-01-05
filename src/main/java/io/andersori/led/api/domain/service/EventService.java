package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.domain.exception.DomainException;

public interface EventService extends Service<EventDTO> {

	List<EventDTO> find(String name, int pageNumber, int pageSize) throws DomainException;
}
