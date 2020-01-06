package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
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
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;
import io.andersori.led.api.domain.service.EventService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class EventTest {

	private final Logger logger = LoggerFactory.getLogger(EventTest.class);

	@Autowired
	EventService eventService;

	@Autowired
	private AccountService accountService;

	@BeforeAll
	void registerUser() throws DomainException {
		AccountDTO account1 = new AccountDTO();
		account1.setUsername("test");
		account1.setEmail("email@email.com");
		account1.setFirstName("Test");
		account1.setLastName("Temp");

		AccountDTO account2 = new AccountDTO();
		account2.setUsername("test2");
		account2.setEmail("email2@email.com");
		account2.setFirstName("Test");
		account2.setLastName("Temp");

		accountService.register(account1, "1234");
		accountService.register(account2, "1234");
	}
	
	@AfterAll
	void clean() {
			List<AccountDTO> accounts = accountService.findAll().stream().map(a -> new AccountDTO().toDTO(a))
					.collect(Collectors.toList());;
			List<EventDTO> events = eventService.findAll().stream().map(a -> new EventDTO().toDTO(a))
					.collect(Collectors.toList());;
			
			accounts.forEach(a -> {
				try {
					accountService.delete(a.getId());
				} catch (DomainException e) {
					e.printStackTrace();
				}
			});
			
			events.forEach(e -> {
				try {
					eventService.delete(e.getId());
				} catch (DomainException ex) {
					ex.printStackTrace();
				}
			});
			
	}

	@Test
	@Order(1)
	void createEvent1() {
		try {
			EventDTO event = new EventDTO();
			event.setDate(LocalDate.of(2020, 3, 2));
			event.setDescription("desc");
			event.setName("Maratona");
			event.setOwnerUsername("test");

			eventService.save(event);
			assertTrue(true);
		} catch (DomainException e) {
			fail();
		}
	}

	@Test
	@Order(2)
	void getAllEventsOfOneAccount() {
		try {
			assertTrue(eventService.findByUser("test").size() == 1);
		} catch (DomainException e) {
			fail();
		}
	}

	@Test
	@Order(3)
	void updateEvent() {
		try {
			EventDTO event = new EventDTO().toDTO(eventService.findByUser("test").get(0));
			event.setDescription("New desc");
			eventService.save(event);

			assertTrue(eventService.find(event.getId()).getDescription().equals("New desc"));
		} catch (DomainException e) {
			fail();
		}
	}

	@Test
	@Order(4)
	void delete() {
		try {
			EventDTO event = new EventDTO().toDTO(eventService.findByUser("test").get(0));

			eventService.delete(event.getId());

			assertTrue(eventService.findByUser("test").size() == 0);
		} catch (DomainException e) {
			fail();
		}
	}

	@Test
	@Order(5)
	void exception1() {
		try {
			@SuppressWarnings("unused")
			EventDTO event = new EventDTO().toDTO(eventService.find(1L));
			fail();
		} catch (DomainException e) {
			assertTrue(true);
		}
	}

	@Test
	@Order(6)
	void exception2() {
		try {
			EventDTO event = new EventDTO();
			event.setDate(LocalDate.of(2020, 3, 2));
			event.setDescription("desc");
			event.setName("Maratona");
			event.setOwnerUsername("test_1");

			eventService.save(event);
			fail();
		} catch (DomainException e) {
			assertTrue(true);
		}
	}

	@Test
	@Order(7)
	void createEvent2() {
		try {
			EventDTO event1 = new EventDTO();
			event1.setDate(LocalDate.of(2020, 3, 2));
			event1.setDescription("desc");
			event1.setName("Maratona");
			event1.setOwnerUsername("test");

			EventDTO event2 = new EventDTO();
			event2.setDate(LocalDate.of(2020, 3, 2));
			event2.setDescription("desc");
			event2.setName("Maratona");
			event2.setOwnerUsername("test2");

			eventService.save(event1);
			eventService.save(event2);

			assertTrue(eventService.findByUser("test").size() == 1 && eventService.findByUser("test2").size() == 1 && eventService.findAll().size() == 2);
		} catch (DomainException e) {
			logger.info(e.getMessage());
			fail();
		}
	}
	
}
