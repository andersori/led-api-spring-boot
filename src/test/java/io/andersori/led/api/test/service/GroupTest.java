package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

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
	
	private final Logger logger = LoggerFactory.getLogger(GroupTest.class);

	private final AccountService accountService;
	private final EventService eventService;
	private final GroupLedService groupService;

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
		account.setEmail("email@email.com");
		account.setFirstName("Test");
		account.setLastName("Temp");
		
		accountService.register(account, "1234");
		
		EventDTO event = new EventDTO();
		event.setDate(LocalDate.of(2020, 3, 2));
		event.setDescription("desc");
		event.setName("Maratona");
		event.setOwnerUsername("test");
		
		eventService.save(event);
	}
	
	@Test
	@Order(1)
	void create() {
		try {
			
			EventDTO myEvent = new EventDTO().toDTO(eventService.findByUser("test").get(0));
			
			GroupDTO group1 = new GroupDTO();
			group1.setEventId(myEvent.getId());
			group1.setName("TEMP 1");
			
			GroupDTO group2 = new GroupDTO();
			group2.setEventId(myEvent.getId());
			group2.setName("TEMP 2");
			
			GroupDTO group3 = new GroupDTO();
			group3.setEventId(myEvent.getId());
			group3.setName("TEMP 3");
			
			groupService.save(group1);
			groupService.save(group2);
			groupService.save(group3);
			
			assertTrue(groupService.find(myEvent).size() == 3);
		} catch (DomainException e) {
			logger.info(e.getMessage());
			fail();
		}
		
	}
}
