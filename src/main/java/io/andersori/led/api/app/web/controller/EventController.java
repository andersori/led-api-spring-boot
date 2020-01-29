package io.andersori.led.api.app.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.EventService;

import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;
import static io.andersori.led.api.app.web.controller.util.PathConfig.ADMIN_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PROTECTED_PATH;

@RestController
@RequestMapping(VERSION)
public class EventController implements DomainController<EventDTO> {

	private static final String PATH = "/events";

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@Override
	@PostMapping(PROTECTED_PATH + PATH)
	public EventDTO save(@RequestBody EventDTO data) throws DomainException {
		return new EventDTO().toDTO(eventService.save(data));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH + "/{id}")
	public EventDTO find(@PathVariable Long id) throws DomainException {
		return new EventDTO().toDTO(eventService.find(id));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH)
	public List<EventDTO> findAll(Pageable page, @ModelAttribute EventDTO filter) {
		return eventService.findAll(page, filter).stream().map(e -> new EventDTO().toDTO(e))
				.collect(Collectors.toList());
	}

	@Override
	@PutMapping(PROTECTED_PATH + PATH + "/{id}")
	public EventDTO update(@PathVariable Long id, @RequestBody EventDTO data) throws DomainException {
		EventDTO ev = new EventDTO().toDTO(eventService.find(id));

		if (data.getName() != null)
			ev.setName(data.getName());

		if (data.getDescription() != null)
			ev.setDescription(data.getDescription());

		if (data.getDate() != null)
			ev.setDate(data.getDate());

		return new EventDTO().toDTO(eventService.save(ev));
	}

	@Override
	@DeleteMapping(ADMIN_PATH + PATH + "/{id}")
	public void delete(@PathVariable Long id) throws DomainException {
		eventService.delete(id);
	}

}
