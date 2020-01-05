package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.TeamDTO;
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
	public TeamDTO save(TeamDTO data) throws DomainException {
		try {
			return new TeamDTO().toDTO(teamLedRepository.save(data.toEntity()));
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			teamLedRepository.deleteById(id);
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public TeamDTO find(Long id) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			return new TeamDTO().toDTO(team.get());
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public List<TeamDTO> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("team_led_id"));
		return teamLedRepository.findAll(page).getContent().stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());
	}

	@Override
	public List<TeamDTO> findAll() {
		return teamLedRepository.findAll().stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());
	}

	@Override
	public List<TeamDTO> find(GroupLed group) {
		return teamLedRepository.findByGroupId(group.getId()).stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());
	}

	@Override
	public List<TeamDTO> find(Event event) {
		return teamLedRepository.findByEventId(event.getId()).stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());
	}

}
