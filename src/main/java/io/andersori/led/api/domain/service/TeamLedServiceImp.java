package io.andersori.led.api.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.HelperFacade;
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

			if (data.getName() == null)
				data.setName(Faker.instance().witcher().monster());

			for (int i = 0; !teamLedRepository.findAll(new TeamSpec(data), PageRequest.of(0, 1)).isEmpty(); i++) {
				if (i < 100) {
					data.setName(Faker.instance().witcher().monster());
				} else {
					data.setName(Faker.instance().witcher().monster());
					break;
				}
			}

			return teamLedRepository.save(data.toEntity(group, event));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DomainException(TeamLedService.class, e.getCause() != null ? e.getCause() : e);
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

	@Override
	@Transactional
	public TeamLed updateGroup(Long id, Long idGroup, String secret) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			if (team.get().getSecret().equals(secret)) {
				teamLedRepository.changeGroup(id, idGroup);
				teamLedRepository.flush();
				return teamLedRepository.findById(id).get();
			}
			throw new DomainException(TeamLedService.class, "The secret doesn't check.");
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public TeamLed updateVerified(Long id, Boolean verified, String secret) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			if (team.get().getSecret().equals(secret)) {
				teamLedRepository.changeVerified(id, verified);
				return teamLedRepository.findById(id).get();
			}
			throw new DomainException(TeamLedService.class, "The secret doesn't check.");
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	@Transactional
	public TeamLed random(Long id, String secret) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			if (team.get().getSecret().equals(secret)) {
				if (team.get().getGroup() == null) {
					HelperFacade.groupSelector(new TeamDTO().toDTO(team.get()),
							new EventDTO().toDTO(team.get().getEvent()));
					return teamLedRepository.findById(id).get();
				}
				throw new DomainException(ParticipantService.class, "You are not allowed to change your group.");
			}
			throw new DomainException(TeamLedService.class, "The secret doesn't check.");
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public List<TeamLed> shuffle(Long idEvent) throws DomainException {
		List<TeamLed> response = setNull(idEvent);
		List<TeamLed> res = new ArrayList<TeamLed>();
		for (TeamLed team : response) {
			res.add(random(team.getId(), team.getSecret()));
		}
		return res;
	}

	@Transactional
	private List<TeamLed> setNull(Long idEvent) {
		List<TeamLed> response = new ArrayList<TeamLed>();
		for (TeamLed team : teamLedRepository.findByEventId(idEvent).stream().map(t -> {
			t.setGroup(null);
			return teamLedRepository.saveAndFlush(t);
		}).collect(Collectors.toList())) {
			response.add(team);
		}
		return response;
	}

	@Override
	public TeamLed findWithSecret(Long id, String secret) throws DomainException {
		Optional<TeamLed> team = teamLedRepository.findById(id);
		if (team.isPresent()) {
			if (secret != null) {
				if (team.get().getSecret().equals(secret)) {
					return team.get();
				}
				throw new DomainException(TeamLedService.class, "The secret doesn't check.");
			} else {
				TeamLed teamEntity = team.get();
				teamEntity.setSecret(null);
				return teamEntity;
			}
		}
		throw new NotFoundException(TeamLedService.class, "Team with id " + id + " not found.");
	}

	@Override
	public List<TeamLed> reverseShuffle(Long idEvent) throws DomainException {
		List<TeamLed> response = new ArrayList<TeamLed>();
		for (TeamLed team : teamLedRepository.findByEventId(idEvent).stream().map(t -> {
			t.setGroup(null);
			return teamLedRepository.saveAndFlush(t);
		}).collect(Collectors.toList())) {

			response.add(team);
		}
		return response;
	}

}
