package io.andersori.led.api.app.web.controller;

import static io.andersori.led.api.app.web.controller.util.PathConfig.ADMIN_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PROTECTED_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PUBLIC_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.ParticipantService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(VERSION)
public class ParticipantController implements DomainController<ParticipantDTO> {

	private static final String PATH = "/participant";

	private final ParticipantService service;

	@Override
	@PostMapping(PROTECTED_PATH + PATH)
	public ParticipantDTO save(@RequestBody ParticipantDTO data) throws DomainException {
		return new ParticipantDTO().toDTO(service.save(data));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH + "/{id}")
	public ParticipantDTO find(@PathVariable Long id) throws DomainException {
		return new ParticipantDTO().toDTO(service.find(id));
	}

	@GetMapping(PUBLIC_PATH + PATH + "/{id}")
	public ParticipantDTO find(@PathVariable Long id, @RequestParam(required = true) String secret)
			throws DomainException {
		return new ParticipantDTO().toDTO(service.findWithSecret(id, secret));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH)
	public List<ParticipantDTO> findAll(Pageable page, @ModelAttribute ParticipantDTO filter) {
		return service.findAll(page, filter).stream().map(parti -> new ParticipantDTO().toDTO(parti))
				.collect(Collectors.toList());
	}

	@Override
	@PutMapping(PROTECTED_PATH + PATH + "/{id}")
	public ParticipantDTO update(@PathVariable Long id, @RequestBody ParticipantDTO data) throws DomainException {
		return new ParticipantDTO().toDTO(service.updateName(id, data));
	}

	@PutMapping(ADMIN_PATH + PATH + "/{id}/team/null")
	public ParticipantDTO updateAdmin(@PathVariable Long id) throws DomainException {
		return new ParticipantDTO().toDTO(service.setTeamNull(id));
	}

	@Override
	@DeleteMapping(ADMIN_PATH + PATH + "/{id}")
	public void delete(@PathVariable Long id) throws DomainException {
		service.delete(id);
	}

	@PostMapping(PUBLIC_PATH + PATH + "/{id}/random")
	public ParticipantDTO random(@PathVariable Long id, @RequestParam(required = true) String secret)
			throws DomainException {
		return new ParticipantDTO().toDTO(service.random(id, secret));
	}

	@PostMapping(ADMIN_PATH + PATH + "/shuffle")
	public List<ParticipantDTO> shuffle(@RequestParam(required = true) Long idEvent) throws DomainException {
		return service.shuffle(idEvent).stream().map(p -> new ParticipantDTO().toDTO(p)).collect(Collectors.toList());
	}

}
