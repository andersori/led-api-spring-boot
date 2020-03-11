package io.andersori.led.api.app.web.controller;

import static io.andersori.led.api.app.web.controller.util.PathConfig.ADMIN_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PROTECTED_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PUBLIC_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.TeamLedService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(VERSION)
public class TeamController implements DomainController<TeamDTO> {

	private static final String PATH = "/teams";

	private final TeamLedService teamService;

	public TeamController(TeamLedService teamService) {
		this.teamService = teamService;
	}

	@Override
	@PostMapping(PROTECTED_PATH + PATH)
	public TeamDTO save(@RequestBody TeamDTO data) throws DomainException {
		data.setGroupId(null);
		return new TeamDTO().toDTO(teamService.save(data));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH + "/{id}")
	public TeamDTO find(@PathVariable Long id) throws DomainException {
		return new TeamDTO().toDTO(teamService.find(id));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH)
	public List<TeamDTO> findAll(Pageable page, @ModelAttribute TeamDTO filter) {
		return teamService.findAll(page, filter).stream().map(t -> new TeamDTO().toDTO(t)).collect(Collectors.toList());
	}

	@Override
	@PutMapping(PROTECTED_PATH + PATH + "/{id}")
	public TeamDTO update(@PathVariable Long id, @RequestBody TeamDTO data) throws DomainException {
		TeamDTO tm = new TeamDTO().toDTO(teamService.find(id));

		if (data.getEventId() != null)
			throw new DomainException(TeamController.class, "It is not possible to change the event id.");
		if (data.getName() != null)
			tm.setName(data.getName());

		return new TeamDTO().toDTO(teamService.save(tm));
	}

	@PatchMapping(value = PROTECTED_PATH + PATH + "/{id}/verified", consumes = MediaType.TEXT_PLAIN_VALUE)
	public TeamDTO updateVerified(@PathVariable Long id, @RequestBody(required = true) String verified,
			@RequestParam(required = true) String secret) throws DomainException {
		return new TeamDTO().toDTO(teamService.updateVerified(id, Boolean.parseBoolean(verified), secret));
	}

	@PatchMapping(value = PROTECTED_PATH + PATH + "/{id}/groupId", consumes = MediaType.TEXT_PLAIN_VALUE)
	public TeamDTO updateGroup(@PathVariable Long id, @RequestBody(required = true) String idGroup,
			@RequestParam(required = true) String secret) throws DomainException {
		Long idGroupLong = null;
		try {
			idGroupLong = Long.parseLong(idGroup);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new TeamDTO().toDTO(teamService.updateGroup(id, idGroupLong, secret));
	}

	@Override
	@DeleteMapping(ADMIN_PATH + PATH + "/{id}")
	public void delete(@PathVariable Long id) throws DomainException {
		teamService.delete(id);
	}

	@PostMapping(PUBLIC_PATH + PATH + "/{id}/random")
	public TeamDTO randomGroup(@PathVariable Long id, @RequestParam(required = true) String secret)
			throws DomainException {
		return new TeamDTO().toDTO(teamService.random(id, secret));
	}

	@PostMapping(ADMIN_PATH + PATH + "/shuffle")
	public List<TeamDTO> shuffleAll(@RequestParam(required = true) Long idEvent) throws DomainException {
		return teamService.shuffle(idEvent).stream().map(team -> new TeamDTO().toDTO(team))
				.collect(Collectors.toList());
	}

}
