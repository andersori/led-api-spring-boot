package io.andersori.led.api.test.service;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
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
import io.andersori.led.api.test.util.ThreadUtil;

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

		GroupDTO group1 = new GroupDTO();
		group1.setEventId(mainEvent.getId());
		group1.setName("TEMP 1");

		GroupDTO group2 = new GroupDTO();
		group2.setEventId(mainEvent.getId());
		group2.setName("TEMP 2");

		groupService.save(group1);
		groupService.save(group2);
	}
	
	@AfterAll
	void clean() throws DomainException {
		List<TeamDTO> teams = teamService.find(mainEvent).stream().map(g -> new TeamDTO().toDTO(g))
				.collect(Collectors.toList());
		for(TeamDTO t : teams) {
			teamService.delete(t.getId());
		}
		
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
		TeamDTO team = new TeamDTO();
		team.setName("Anonymous 1");
		team.setVerified(true);
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
		team2.setVerified(true);
		team2.setEventId(mainEvent.getId());

		TeamDTO team3 = new TeamDTO();
		team3.setName("Anonymous 3");
		team3.setVerified(true);
		team3.setEventId(mainEvent.getId());

		TeamDTO team4 = new TeamDTO();
		team4.setName("Anonymous 4");
		team4.setVerified(true);
		team4.setEventId(mainEvent.getId());

		team2.toDTO(teamService.save(team2));
		team3.toDTO(teamService.save(team3));
		team4.toDTO(teamService.save(team4));

		HelperFacade.groupSelector(team2, mainEvent);
		HelperFacade.groupSelector(team3, mainEvent);
		HelperFacade.groupSelector(team4, mainEvent);

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
		team5.setVerified(true);
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

		assertTrue((test1 && !test2) || (!test1 && test2));
	}

	@Test
	@Order(6)
	void seletor3() throws DomainException {
		List<TeamDTO> teamsSaved = teamService.find(mainEvent).stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());

		for (TeamDTO t : teamsSaved) {
			t.setGroupId(null);
			teamService.save(t);
		}

		assertTrue(teamService.find(mainEvent).stream().filter(t -> t.getGroup() == null).collect(Collectors.toList())
				.size() == teamsSaved.size());
	}

	@Test
	@Order(7)
	void selectorWithThreads() throws DomainException {
		TeamDTO team6 = new TeamDTO();
		team6.setName("Anonymous 6");
		team6.setVerified(true);
		team6.setEventId(mainEvent.getId());

		TeamDTO team7 = new TeamDTO();
		team7.setName("Anonymous 7");
		team7.setVerified(true);
		team7.setEventId(mainEvent.getId());

		TeamDTO team8 = new TeamDTO();
		team8.setName("Anonymous 8");
		team8.setVerified(true);
		team8.setEventId(mainEvent.getId());

		TeamDTO team9 = new TeamDTO();
		team9.setName("Anonymous 9");
		team9.setVerified(true);
		team9.setEventId(mainEvent.getId());

		TeamDTO team10 = new TeamDTO();
		team10.setName("Anonymous 10");
		team10.setVerified(true);
		team10.setEventId(mainEvent.getId());

		TeamDTO team11 = new TeamDTO();
		team11.setName("Anonymous 11");
		team11.setVerified(true);
		team11.setEventId(mainEvent.getId());

		teamService.save(team6);
		teamService.save(team7);
		teamService.save(team8);
		teamService.save(team9);
		teamService.save(team10);
		teamService.save(team11);

		List<TeamDTO> teamsSaved = teamService.find(mainEvent).stream().map(t -> {
			return new TeamDTO().toDTO(t);
		}).collect(Collectors.toList());

		ExecutorService executor = Executors.newFixedThreadPool(3);

		List<Runnable> updates = new ArrayList<Runnable>();
		for (TeamDTO t : teamsSaved) {
			updates.add(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(new Random().nextInt(200));
					HelperFacade.groupSelector(t, mainEvent);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (DomainException e) {
					e.printStackTrace();
				}
			});
		}

		for (Runnable r : updates) {
			executor.execute(r);
		}

		ThreadUtil.awaitTerminationAfterShutdown(executor, 4);

		List<GroupDTO> groups = groupService.find(mainEvent).stream().map(g -> new GroupDTO().toDTO(g))
				.collect(Collectors.toList());

		boolean test1 = teamsSaved.stream().filter(t -> t.getGroupId() == groups.get(0).getId())
				.collect(Collectors.toList()).size() > 5
				&& teamsSaved.stream().filter(t -> t.getGroupId() == groups.get(1).getId()).collect(Collectors.toList())
						.size() == 5;

		boolean test2 = teamsSaved.stream().filter(t -> t.getGroupId() == groups.get(0).getId())
				.collect(Collectors.toList()).size() == 5
				&& teamsSaved.stream().filter(t -> t.getGroupId() == groups.get(1).getId()).collect(Collectors.toList())
						.size() > 5;

		teamsSaved.forEach(t -> {
			logger.info(Long.toString(t.getGroupId()));
		});

		assertTrue((test1 && !test2) || (!test1 && test2));
	}

	@Test
	@Order(8)
	void exception1() {
		TeamDTO team12 = new TeamDTO();
		team12.setName("Anonymous 12");
		team12.setEventId(mainEvent.getId());

		logger.warn(assertThrows(DomainException.class, () -> {
			HelperFacade.groupSelector(team12, mainEvent);
		}).toString());
	}

	@Test
	@Order(8)
	void exception2() throws DomainException {
		TeamDTO team13 = new TeamDTO();
		team13.setName("Anonymous 13");
		team13.setEventId(mainEvent.getId());

		team13.toDTO(teamService.save(team13));

		logger.warn(assertThrows(DomainException.class, () -> {
			HelperFacade.groupSelector(team13, mainEvent);
		}).toString());
	}
}
