package io.andersori.led.api.test.service;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
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
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.HelperFacade;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;
import io.andersori.led.api.domain.service.EventService;
import io.andersori.led.api.domain.service.GroupLedService;
import io.andersori.led.api.domain.service.TeamLedService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class TeamTest {

	private final Logger logger = LoggerFactory.getLogger(GroupTest.class);

	private final AccountService accountService;
	private final EventService eventService;
	private final GroupLedService groupService;
	private final TeamLedService teamService;

	private final AccountDTO owner = new AccountDTO();
	private final EventDTO mainEvent = new EventDTO();

	@Autowired
	public TeamTest(AccountService accountService, EventService eventService, GroupLedService groupService,
			TeamLedService teamService) {
		this.accountService = accountService;
		this.eventService = eventService;
		this.groupService = groupService;
		this.teamService = teamService;
	}

	@BeforeAll
	void init() throws DomainException {
		AccountDTO account = new AccountDTO();
		account.setUsername("test");
		account.setEmail("email@email.com");
		account.setFirstName("Test");
		account.setLastName("Temp");

		owner.toDTO(accountService.register(account, "1234"));

		EventDTO event = new EventDTO();
		event.setDate(LocalDate.of(2020, 3, 2));
		event.setDescription("desc");
		event.setName("Maratona");
		event.setOwnerUsername("test");

		mainEvent.toDTO(eventService.save(event));

		GroupDTO group1 = new GroupDTO();
		group1.setEventId(mainEvent.getId());
		group1.setName("TEMP 1");

		GroupDTO group2 = new GroupDTO();
		group2.setEventId(mainEvent.getId());
		group2.setName("TEMP 2");

		groupService.save(group1);
		groupService.save(group2);
	}

	@Test
	@Order(1)
	void create() throws DomainException {
		TeamDTO team = new TeamDTO();
		team.setName("Anonymous 1");
		team.setEventId(mainEvent.getId());
		team.setParticipants(Arrays.asList("Unnamed 1", "Unnamed 2", "Unnamed 3"));
		logger.info(new TeamDTO().toDTO(teamService.save(team)).toString());
		assertTrue(true);
	}

	@Test
	@Order(2)
	void find() throws DomainException {
		TeamDTO team = new TeamDTO().toDTO(teamService.find(mainEvent).get(0));
		assertTrue(team.getParticipants().size() == 3);
	}

	@Test
	@Order(3)
	void update() throws DomainException {
		TeamDTO team = new TeamDTO().toDTO(teamService.find(mainEvent).get(0));
		HelperFacade.groupSelector(team, mainEvent);

		team = new TeamDTO().toDTO(teamService.find(team.getId()));
		logger.info(team.toString());

		assertTrue(team.getGroupId() != null);
	}

	@Test
	@Order(4)
	void selector1() throws DomainException {

		TeamDTO team2 = new TeamDTO();
		team2.setName("Anonymous 2");
		team2.setEventId(mainEvent.getId());

		TeamDTO team3 = new TeamDTO();
		team3.setName("Anonymous 3");
		team3.setEventId(mainEvent.getId());

		TeamDTO team4 = new TeamDTO();
		team4.setName("Anonymous 4");
		team4.setEventId(mainEvent.getId());

		team2.toDTO(teamService.save(team2));
		team3.toDTO(teamService.save(team3));
		team4.toDTO(teamService.save(team4));

		HelperFacade.groupSelector(Arrays.asList(team2, team3, team4), mainEvent);

		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		List<TeamDTO> teams = teamService.find(mainEvent).stream().map(g -> new TeamDTO().toDTO(g))
				.collect(Collectors.toList());

		logger.info(teams.toString());

		assertTrue(teams.stream().filter(t -> t.getGroupId() == groups.get(0).getId()).collect(Collectors.toList())
				.size() == 2
				&& teams.stream().filter(t -> t.getGroupId() == groups.get(1).getId()).collect(Collectors.toList())
						.size() == 2);
	}

	@Test
	@Order(5)
	void seletor2() throws DomainException {
		TeamDTO team5 = new TeamDTO();
		team5.setName("Anonymous 5");
		team5.setEventId(mainEvent.getId());

		team5.toDTO(teamService.save(team5));

		HelperFacade.groupSelector(team5, mainEvent);

		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		List<TeamDTO> teams = teamService.find(mainEvent).stream().map(g -> new TeamDTO().toDTO(g))
				.collect(Collectors.toList());

		boolean test1 = teams.stream().filter(t -> t.getGroupId() == groups.get(0).getId()).collect(Collectors.toList())
				.size() > 2
				&& teams.stream().filter(t -> t.getGroupId() == groups.get(1).getId()).collect(Collectors.toList())
						.size() == 2;

		boolean test2 = teams.stream().filter(t -> t.getGroupId() == groups.get(0).getId()).collect(Collectors.toList())
				.size() == 2
				&& teams.stream().filter(t -> t.getGroupId() == groups.get(1).getId()).collect(Collectors.toList())
						.size() > 2;

		teams.forEach(t -> {
			logger.info(Long.toString(t.getGroupId()));
		});

		assertTrue((test1 && !test2) || (!test1 && test2));
	}
}
