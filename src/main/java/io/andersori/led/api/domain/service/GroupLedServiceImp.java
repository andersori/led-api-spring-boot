package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.GroupLedRepository;

@Service
public class GroupLedServiceImp implements GroupLedService {

	private GroupLedRepository groupLedRepository;
	private EventService eventService;

	@Autowired
	public GroupLedServiceImp(GroupLedRepository groupLedRepository, EventService eventService,
			AccountService accountService) {
		this.groupLedRepository = groupLedRepository;
		this.eventService = eventService;
	}

	@Override
	public GroupLed save(GroupDTO data) throws DomainException {
		try {
			Event event = eventService.find(data.getEventId());
			return groupLedRepository.save(data.toEntity(event));
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<GroupLed> group = groupLedRepository.findById(id);
		if (group.isPresent()) {
			groupLedRepository.deleteById(id);
		}
		throw new NotFoundException(GroupLedService.class, "Group with id " + id + " not found.");
	}

	@Override
	public GroupLed find(Long id) throws DomainException {
		Optional<GroupLed> group = groupLedRepository.findById(id);
		if (group.isPresent()) {
			return group.get();
		}
		throw new NotFoundException(GroupLedService.class, "Group with id " + id + " not found.");
	}

	@Override
	public List<GroupLed> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("group_led_id"));
		return groupLedRepository.findAll(page).getContent();
	}

	@Override
	public List<GroupLed> findAll() {
		return groupLedRepository.findAll();
	}

	@Override
	public List<GroupLed> find(String name) {
		return groupLedRepository.findByName(name);
	}

	@Override
	public List<GroupLed> find(EventDTO event) {
		return groupLedRepository.findByEventId(event.getId());
	}

}
