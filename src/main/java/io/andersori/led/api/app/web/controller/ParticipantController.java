package io.andersori.led.api.app.web.controller;

import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.ParticipantService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(VERSION)
public class ParticipantController implements DomainController<ParticipantDTO> {

	private final ParticipantService service;

	@Override
	public ParticipantDTO save(ParticipantDTO data) throws DomainException {
		return new ParticipantDTO().toDTO(service.save(data));
	}

	@Override
	public ParticipantDTO find(Long id) throws DomainException {
		return new ParticipantDTO().toDTO(service.find(id));
	}

	@Override
	public List<ParticipantDTO> findAll(Pageable page, ParticipantDTO filter) {
		return service.findAll(page, filter).stream().map(parti -> new ParticipantDTO().toDTO(parti))
				.collect(Collectors.toList());
	}

	@Override
	public ParticipantDTO update(Long id, ParticipantDTO data) throws DomainException {
		ParticipantDTO participant = new ParticipantDTO().toDTO(service.find(id));

		if (participant.getName() != data.getName())
			participant.setName(data.getName());
		if (participant.getIdTeam() != data.getIdTeam())
			participant.setIdTeam(data.getIdTeam());

		return new ParticipantDTO().toDTO(service.save(participant));
	}

	@Override
	public void delete(Long id) throws DomainException {
		service.delete(id);
	}

}
