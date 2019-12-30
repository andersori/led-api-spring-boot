package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.resource.repository.EventRepository;

@Service
public class EventServiceImp implements EventService {

	private EventRepository eventRepository;

	@Autowired
	public EventServiceImp(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@Override
	public Optional<Event> save(Event entity) {
		try {
			return Optional.of(eventRepository.save(entity));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public void delete(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public Optional<Event> find(Long id) {
		return eventRepository.findById(id);
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
