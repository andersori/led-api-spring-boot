package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class AccountTest {

	private final Logger logger = LoggerFactory.getLogger(AccountTest.class);

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PasswordEncoder enconder;

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
		account2.setFirstName("Testing");
		account2.setLastName("Temp");

		accountService.register(account1, "1234");
		accountService.register(account2, "1234");
	}
	
	@AfterAll
	void cleanUser() {
		try {
			accountService.delete(accountService.find("test3").getId());
			accountService.delete(accountService.find("test2").getId());
			
			System.out.println(accountService.findAll().size());
		} catch (DomainException e) {
			System.out.println("error");
		}
		
	}

	@Test
	@Order(1)
	void find() {
		try {
			AccountDTO account;
			account = accountService.find("test");
			logger.info(account.toString());
			assertTrue(account != null);
		} catch (DomainException e) {
			fail();
		}
	}

	@Test
	@Order(3)
	void update() {
		try {
			AccountDTO account = accountService.find("test");
			account.setFirstName("Test 2");
			accountService.save(account);
			
			account = accountService.find("test");
			assertTrue(account.getFirstName().equals("Test 2"));
		} catch(DomainException e) {
			fail();
		}
	}

	@Test
	@Order(4)
	void changePasswordByEmail() {
		try {
			accountService.changePasswordByEmail("email@email.com", "12345");

			String password = accountService.getPassword("test");
			assertTrue(enconder.matches("12345", password));
		} catch(DomainException e) {
			fail();
		}
	}

	@Test
	@Order(5)
	void changePasswordByUsername() {
		try {
			accountService.changePasswordByUsername("test", "123");

			String password = accountService.getPassword("test");
			assertTrue(enconder.matches("123", password));
		} catch(DomainException e) {
			fail();
		}
	}

	@Test
	@Order(6)
	void findWithPages() {
		List<AccountDTO> acs = accountService.find(0, 5);
		assertTrue(acs.size() == 2);
	}

	@Test
	@Order(7)
	void findAll() {
		List<AccountDTO> acs = accountService.findAll();
		assertTrue(acs.size() == 2);
	}

	@Test
	@Order(8)
	void findPerFirstName() {
		List<AccountDTO> acs = accountService.find("Test", 0, 5);
		assertTrue(acs.size() == 2);
	}

	@Test
	@Order(9)
	void updateWithException1() {
		try {
			AccountDTO account = accountService.find("test");
			account.setUsername("test2");
			accountService.save(account);
		} catch(DomainException e) {
			logger.info(e.getMessage());
			assertTrue(e.getHttpStatusCode() == HttpStatus.CONFLICT_409);
		}
	}

	@Test
	@Order(10)
	void updateUsername() throws DomainException {
		try {
			AccountDTO account = accountService.find("test");
			account.setUsername("test3");
			accountService.save(account);
			assertTrue(accountService.find("test3") != null);
		} catch(DomainException e) {
			fail();
		}
	}
}
