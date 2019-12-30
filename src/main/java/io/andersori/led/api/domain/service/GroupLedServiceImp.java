package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.resource.repository.GroupLedRepository;

@Service
public class GroupLedServiceImp implements GroupLedService {

	private GroupLedRepository groupLedRepository;

	@Autowired
	public GroupLedServiceImp(GroupLedRepository groupLedRepository) {
		this.groupLedRepository = groupLedRepository;
	}

	@Override
	public Optional<GroupLed> save(GroupLed entity) {
		try {
			return Optional.of(groupLedRepository.save(entity));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public void delete(Long id) {
		groupLedRepository.deleteById(id);
	}

	@Override
	public Optional<GroupLed> find(Long id) {
		return groupLedRepository.findByEventId(id);
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
	public Optional<GroupLed> find(String name) {
		return groupLedRepository.findByName(name);
	}

	@Override
	public Optional<GroupLed> find(Event event) {
		return groupLedRepository.findByEventId(event.getId());
	}

}
