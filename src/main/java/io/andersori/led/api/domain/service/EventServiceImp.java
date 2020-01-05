package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.EventRepository;

@Service
public class EventServiceImp implements EventService {

	private EventRepository eventRepository;
	private AccountService accountService;

	@Autowired
	public EventServiceImp(EventRepository eventRepository, AccountService accountService) {
		this.eventRepository = eventRepository;
		this.accountService = accountService;
	}

	@Override
	public EventDTO save(EventDTO data) throws DomainException {
		try {
			AccountDTO owner = accountService.find(data.getOwnerUsername());
			Event event = data.toEntity(owner.toEntity());
			return new EventDTO().toDTO(eventRepository.save(event));
		} catch(DomainException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<Event> event = eventRepository.findById(id);
		if(event.isPresent()) {
			eventRepository.deleteById(id);
			return;
		}
		throw new NotFoundException(EventService.class, "Event with id " + id + " not found.");
	}

	@Override
	public EventDTO find(Long id) throws DomainException {
		Optional<Event> event = eventRepository.findById(id);
		if (event.isPresent()) {
			return new EventDTO().toDTO(event.get());
		}
		throw new NotFoundException(EventService.class, "Event with id " + id + " not found.");
	}

	@Override
	public List<EventDTO> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("event_id"));
		return eventRepository.findAll(page).getContent().stream().map(ev -> {
			return new EventDTO().toDTO(ev);
		}).collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> findAll() {
		return eventRepository.findAll().stream().map(ev -> {
			return new EventDTO().toDTO(ev);
		}).collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> find(String name,int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("event_id"));
		return eventRepository.findByNameContaining(name, page).getContent().stream().map(ev -> {
			return new EventDTO().toDTO(ev);
		}).collect(Collectors.toList());
	}

}
