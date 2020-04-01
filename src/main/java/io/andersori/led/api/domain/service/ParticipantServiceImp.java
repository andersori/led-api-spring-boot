package io.andersori.led.api.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.HelperFacade;
import io.andersori.led.api.domain.entity.Participant;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.ParticipantRepository;
import io.andersori.led.api.resource.specification.ParticipantSpec;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipantServiceImp implements ParticipantService {

	private final ParticipantRepository repo;
	private final TeamLedService teamService;
	private final EventService eventService;

	@Override
	public Participant save(ParticipantDTO data) throws DomainException {
		return repo.save(data.toEntity(null, eventService.find(data.getIdEvent())));
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<Participant> participant = repo.findById(id);
		if (participant.isPresent()) {
			repo.deleteById(id);
			return;
		}
		throw new NotFoundException(GroupLedService.class, "Participant with id " + id + " not found.");
	}

	@Override
	public Participant find(Long id) throws DomainException {
		Optional<Participant> participant = repo.findById(id);
		if (participant.isPresent()) {
			return participant.get();
		}
		throw new NotFoundException(EventService.class, "Participant with id " + id + " not found.");
	}

	@Override
	public List<Participant> findAll(Pageable page, ParticipantDTO filter) {
		return repo.findAll(new ParticipantSpec(filter), page).getContent();
	}

	@Override
	public List<Participant> find(EventDTO event) {
		return repo.findByEventId(event.getId());
	}

	@Override
	public void updateTeam(ParticipantDTO parti, TeamDTO team) throws DomainException {
		Optional<Participant> participant = repo.findById(parti.getId());
		if (participant.isPresent()) {
			participant.get().setTeam(teamService.find(team.getId()));
			repo.saveAndFlush(participant.get());
		} else {
			throw new NotFoundException(EventService.class, "Participant with id " + parti.getId() + " not found.");
		}
	}

	@Override
	public List<Participant> shuffle(Long idEvent) throws DomainException {
		List<Participant> response = setNull(idEvent);
		List<Participant> res = new ArrayList<Participant>();
		for (Participant team : response) {
			res.add(random(team.getId(), team.getSecret()));
		}
		return res;
	}
	
	@Transactional
	private List<Participant> setNull(Long idEvent){
		List<Participant> response = new ArrayList<Participant>();
		for (Participant team : repo.findByEventId(idEvent).stream().map(p -> {
			p.setTeam(null);
			return repo.saveAndFlush(p);
		}).collect(Collectors.toList())) {
			response.add(team);
		}
		return response;
	}

	@Override
	@Transactional
	public Participant random(Long id, String secret) throws DomainException {
		Optional<Participant> parti = repo.findById(id);
		if (parti.isPresent()) {
			if (parti.get().getSecret().equals(secret)) {
				if (parti.get().getTeam() == null) {
					HelperFacade.teamSelector(new ParticipantDTO().toDTO(parti.get()),
							new EventDTO().toDTO(parti.get().getEvent()));
					return repo.findById(id).get();
				}
				throw new DomainException(ParticipantService.class, "You are not allowed to change your team.");
			}
			throw new DomainException(ParticipantService.class, "The secret doesn't check.");
		}
		throw new NotFoundException(ParticipantService.class, "Participant with id " + id + " not found.");
	}

	@Override
	public Participant findWithSecret(Long id, String secret) throws DomainException {
		Optional<Participant> participant = repo.findById(id);
		if (participant.isPresent()) {
			if (participant.get().getSecret().equals(secret)) {
				return participant.get();
			}
			throw new DomainException(EventService.class, "The secret doesn't check.");
		} else {
			throw new NotFoundException(EventService.class, "Participant with id " + id + " not found.");
		}
	}

	@Override
	public Participant setTeamNull(Long id) throws DomainException {
		Optional<Participant> participant = repo.findById(id);
		if (participant.isPresent()) {
			Participant p = participant.get();
			p.setTeam(null);
			repo.save(p);
			return p;
		}
		throw new NotFoundException(GroupLedService.class, "Participant with id " + id + " not found.");
	}

	@Override
	public Participant updateName(Long id, ParticipantDTO parti) throws DomainException {
		Optional<Participant> participant = repo.findById(id);
		if (participant.isPresent()) {
			if (parti.getName() != null) {
				participant.get().setName(parti.getName());
				return repo.save(participant.get());
			}
			return participant.get();
		}
		throw new NotFoundException(GroupLedService.class, "Participant with id " + id + " not found.");
	}

	@Override
	public List<Participant> reverseShuffle(Long idEvent) throws DomainException {
		List<Participant> response = new ArrayList<Participant>();
		for (Participant team : repo.findByEventId(idEvent).stream().map(p -> {
			p.setTeam(null);
			return repo.saveAndFlush(p);
		}).collect(Collectors.toList())) {

			response.add(team);
		}
		return response;
	}

}
