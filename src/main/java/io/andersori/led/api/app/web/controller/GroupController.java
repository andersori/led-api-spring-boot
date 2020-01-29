package io.andersori.led.api.app.web.controller;

import static io.andersori.led.api.app.web.controller.util.PathConfig.PROTECTED_PATH;
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
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.GroupLedService;

@RestController
@RequestMapping(VERSION)
public class GroupController implements DomainController<GroupDTO> {

	private static final String PATH = "/groups";

	private final GroupLedService groupService;

	public GroupController(GroupLedService groupService) {
		this.groupService = groupService;
	}

	@Override
	@PostMapping(PROTECTED_PATH + PATH)
	public GroupDTO save(@RequestBody GroupDTO data) throws DomainException {
		return new GroupDTO().toDTO(groupService.save(data));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH + "/{id}")
	public GroupDTO find(@PathVariable Long id) throws DomainException {
		return new GroupDTO().toDTO(groupService.find(id));
	}

	@Override
	@PutMapping(PROTECTED_PATH + PATH + "/{id}")
	public GroupDTO update(@PathVariable Long id, @RequestBody GroupDTO data) throws DomainException {
		GroupDTO gp = new GroupDTO().toDTO(groupService.find(id));
		if (data.getEventId() != null)
			gp.setEventId(data.getEventId());
		if (data.getName() != null)
			gp.setName(data.getName());
		return new GroupDTO().toDTO(groupService.save(gp));
	}

	@Override
	@DeleteMapping(PROTECTED_PATH + PATH + "/{id}")
	public void delete(@PathVariable Long id) throws DomainException {
		groupService.delete(id);
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH)
	public List<GroupDTO> findAll(Pageable page, @ModelAttribute GroupDTO filter) {
		return groupService.findAll(page, filter).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());
	}

}
