package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.EventRepository;
import io.andersori.led.api.resource.specification.EventSpec;

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
	public Event save(EventDTO data) throws DomainException {
		try {
			Account owner = accountService.find(SecurityContextHolder.getContext().getAuthentication().getName());
			return eventRepository.save(data.toEntity(owner));
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<Event> event = eventRepository.findById(id);
		if (event.isPresent()) {
			eventRepository.deleteById(id);
			return;
		}
		throw new NotFoundException(EventService.class, "Event with id " + id + " not found.");
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
	public List<Event> findAll(Pageable page, EventDTO filter) {
		return eventRepository.findAll(new EventSpec(filter), page).getContent();
	}

}
