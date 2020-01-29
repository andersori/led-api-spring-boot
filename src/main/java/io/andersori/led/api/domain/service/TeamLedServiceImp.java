package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.TeamLed;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.TeamLedRepository;
import io.andersori.led.api.resource.specification.TeamSpec;

@Service
public class TeamLedServiceImp implements TeamLedService {

	private EventService eventService;
	private GroupLedService groupService;
	private TeamLedRepository teamLedRepository;

	@Autowired
	public TeamLedServiceImp(EventService eventService, GroupLedService groupService,
			TeamLedRepository teamLedRepository) {
		this.eventService = eventService;
		this.groupService = groupService;
		this.teamLedRepository = teamLedRepository;
	}

	@Override
	public TeamLed save(TeamDTO data) throws DomainException {
		try {
			Event event = eventService.find(data.getEventId());
			GroupLed group = null;
			if (data.getGroupId() != null) {
				group = groupService.find(data.getGroupId());
			}
			System.out.println(data.toString());
			return teamLedRepository.save(data.toEntity(group, event));
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			teamLedRepository.deleteById(id);
			return;
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
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
	public void updateGroup(TeamDTO team, Long idGroup) {
		teamLedRepository.changeGroup(team.getId(), idGroup);
	}

	@Override
	public List<TeamLed> findAll(Pageable page, TeamDTO filter) {
		return teamLedRepository.findAll(new TeamSpec(filter), page).getContent();
	}

	@Override
	public List<TeamLed> find(GroupDTO group) throws DomainException {
		return teamLedRepository.findByGroupId(group.getId());
	}

	@Override
	public List<TeamLed> find(EventDTO event) throws DomainException {
		return teamLedRepository.findByEventId(event.getId());
	}

}
