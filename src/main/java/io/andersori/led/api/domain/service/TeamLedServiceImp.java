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
import io.andersori.led.api.domain.entity.TeamLed;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.TeamLedRepository;

@Service
public class TeamLedServiceImp implements TeamLedService {

	private TeamLedRepository teamLedRepository;

	@Autowired
	public TeamLedServiceImp(TeamLedRepository teamLedRepository) {
		this.teamLedRepository = teamLedRepository;
	}

	@Override
	public TeamLed save(TeamLed entity) throws DomainException {
		try {
			return teamLedRepository.save(entity);
		} catch (Exception e) {
			throw new DomainException(TeamLedService.class, e.getMessage(), e.getCause());
		}
	}

	@Override
	public void delete(Long id) {
		teamLedRepository.deleteById(id);
	}

	@Override
	public TeamLed find(Long id) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			return team.get();
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public List<TeamLed> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("team_led_id"));
		return teamLedRepository.findAll(page).getContent();
	}

	@Override
	public List<TeamLed> findAll() {
		return teamLedRepository.findAll();
	}

	@Override
	public Optional<TeamLed> find(GroupLed group) {
		return teamLedRepository.findByGroupId(group.getId());
	}

	@Override
	public Optional<TeamLed> find(Event event) {
		return teamLedRepository.findByEventId(event.getId());
	}

}