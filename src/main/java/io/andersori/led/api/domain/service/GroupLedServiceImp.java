package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.GroupLedRepository;

@Service
public class GroupLedServiceImp implements GroupLedService {

	private GroupLedRepository groupLedRepository;

	@Autowired
	public GroupLedServiceImp(GroupLedRepository groupLedRepository) {
		this.groupLedRepository = groupLedRepository;
	}

	@Override
	public GroupDTO save(GroupDTO data) throws DomainException {
		try {
			return new GroupDTO().toDTO(groupLedRepository.save(data.toEntity()));
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
	public GroupDTO find(Long id) throws DomainException {
		Optional<GroupLed> group = groupLedRepository.findById(id);
		if (group.isPresent()) {
			return new GroupDTO().toDTO(group.get());
		}
		throw new NotFoundException(GroupLedService.class, "Group with id " + id + " not found.");
	}

	@Override
	public List<GroupDTO> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("group_led_id"));
		return groupLedRepository.findAll(page).getContent().stream().map(gp -> {
			return new GroupDTO().toDTO(gp);
		}).collect(Collectors.toList());
	}

	@Override
	public List<GroupDTO> findAll() {
		return groupLedRepository.findAll().stream().map(gp -> {
			return new GroupDTO().toDTO(gp);
		}).collect(Collectors.toList());
	}

	@Override
	public List<GroupDTO> find(String name) {
		return groupLedRepository.findByName(name).stream().map(gp -> {
			return new GroupDTO().toDTO(gp);
		}).collect(Collectors.toList());
	}

	@Override
	public List<GroupDTO> find(Event event) {
		return groupLedRepository.findByEventId(event.getId()).stream().map(gp -> {
			return new GroupDTO().toDTO(gp);
		}).collect(Collectors.toList());
	}

}
