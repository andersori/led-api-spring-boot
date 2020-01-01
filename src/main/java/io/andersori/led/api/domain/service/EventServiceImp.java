package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.EventRepository;

@Service
public class EventServiceImp implements EventService {

	private EventRepository eventRepository;

	@Autowired
	public EventServiceImp(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@Override
	public Event save(Event entity) throws DomainException {
		try {
			return eventRepository.save(entity);
		} catch (Exception e) {
			throw new DomainException(EventService.class, e.getMessage(), e.getCause());
		}
	}

	@Override
	public void delete(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public Event find(Long id) throws DomainException {
		Optional<Event> event = eventRepository.findById(id);
		if (event.isPresent()) {
			return event.get();
		}
		throw new NotFoundException(EventService.class, "Event with id " + id + " not found.");
	}

	@Override
	public List<Event> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("event_id"));
		return eventRepository.findAll(page).getContent();
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	public Optional<Event> find(String name) {
		return eventRepository.findByName(name);
	}

}
