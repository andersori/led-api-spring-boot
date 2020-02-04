package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;
import io.andersori.led.api.domain.service.EventService;
import io.andersori.led.api.domain.service.GroupLedService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class GroupTest {
/*
	private final Logger logger = LoggerFactory.getLogger(GroupTest.class);

	private final AccountService accountService;
	private final EventService eventService;
	private final GroupLedService groupService;

	private final AccountDTO owner = new AccountDTO();
	private final EventDTO mainEvent = new EventDTO();

	@Autowired
	public GroupTest(AccountService accountService, EventService eventService, GroupLedService groupService) {
		this.accountService = accountService;
		this.eventService = eventService;
		this.groupService = groupService;
	}

	@BeforeAll
	void init() throws DomainException {
		AccountDTO account = new AccountDTO();
		account.setUsername("test");
		account.setPassword("1234");
		account.setEmail("email@email.com");
		account.setFirstName("Test");
		account.setLastName("Temp");

		owner.toDTO(accountService.register(account));

		EventDTO event = new EventDTO();
		event.setDate(LocalDate.of(2020, 3, 2));
		event.setDescription("desc");
		event.setName("Maratona");
		event.setOwnerUsername("test");

		mainEvent.toDTO(eventService.save(event));
	}

	@AfterAll
	void clean() throws DomainException {
		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		for (GroupDTO g : groups) {
			groupService.delete(g.getId());
		}

		eventService.delete(mainEvent.getId());
		accountService.delete(owner.getId());
	}

	@Test
	@Order(1)
	void create() throws DomainException {
		GroupDTO group1 = new GroupDTO();
		group1.setEventId(mainEvent.getId());
		group1.setName("TEMP 1");

		GroupDTO group2 = new GroupDTO();
		group2.setEventId(mainEvent.getId());
		group2.setName("TEMP 2");

		GroupDTO group3 = new GroupDTO();
		group3.setEventId(mainEvent.getId());
		group3.setName("TEMP 3");

		groupService.save(group1);
		groupService.save(group2);
		groupService.save(group3);

		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		assertTrue(groups.size() == 3);
	}

	@Test
	@Order(2)
	void find() throws DomainException {
		List<String> groups = groupService.find(mainEvent).stream().map(g -> g.getName()).collect(Collectors.toList());

		assertTrue(groups.containsAll(Arrays.asList("TEMP 1", "TEMP 2", "TEMP 2")));
	}

	@Test
	@Order(3)
	void update() throws DomainException {
		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		GroupDTO temp = groups.get(0);
		temp.setName("TEMP 4");
		groupService.save(temp);

		assertTrue(groupService.find(temp.getId()).getName().equals("TEMP 4"));
	}

	@Test
	@Order(4)
	void remove() throws DomainException {
		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		groupService.delete(groups.get(0).getId());
		groupService.delete(groups.get(1).getId());

		groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g)).collect(Collectors.toList());

		assertTrue(groups.size() == 1);
	}

	@Test
	@Order(5)
	void exception1() {
		logger.warn(assertThrows(DomainException.class, () -> {
			groupService.delete(10L);
		}).getMessage());
	}
	*/
}
