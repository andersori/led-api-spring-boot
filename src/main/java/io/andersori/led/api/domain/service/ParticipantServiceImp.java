package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.ParticipantDTO;
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
		return repo.save(data.toEntity(teamService.find(data.getIdTeam()), eventService.find(data.getIdEvent())));
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
		throw new NotFoundException(EventService.class, "Event with id " + id + " not found.");
	}

	@Override
	public List<Participant> findAll(Pageable page, ParticipantDTO filter) {
		return repo.findAll(new ParticipantSpec(filter), page).getContent();
	}

	@Override
	public List<Participant> find(EventDTO event) {
		return repo.findByEventId(event.getId());
	}

}
