package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

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

	private AccountService accountService;
	private PasswordEncoder enconder;

	@Autowired
	public AccountTest(AccountService accountService, PasswordEncoder enconder) {
		this.accountService = accountService;
		this.enconder = enconder;
	}

	@BeforeAll
	void init() throws DomainException {
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
	void clean() throws DomainException {
		accountService.delete(accountService.find("test3").getId());
		accountService.delete(accountService.find("test2").getId());
	}

	@Test
	@Order(1)
	void find() throws DomainException {
		AccountDTO account = new AccountDTO().toDTO(accountService.find("test"));
		assertTrue(account != null);
	}

	@Test
	@Order(3)
	void update() throws DomainException {
		AccountDTO account = new AccountDTO().toDTO(accountService.find("test"));
		account.setFirstName("Test 2");
		accountService.save(account);

		account = new AccountDTO().toDTO(accountService.find("test"));
		assertTrue(account.getFirstName().equals("Test 2"));
	}

	@Test
	@Order(4)
	void changePasswordByEmail() throws DomainException {
		accountService.changePasswordByEmail("email@email.com", "12345");

		String password = accountService.getPassword("test");
		assertTrue(enconder.matches("12345", password));
	}

	@Test
	@Order(5)
	void changePasswordByUsername() throws DomainException {
		accountService.changePasswordByUsername("test", "123");

		String password = accountService.getPassword("test");
		assertTrue(enconder.matches("123", password));
	}

	@Test
	@Order(6)
	void findWithPages() {
		List<AccountDTO> acs = accountService.find(0, 5).stream().map(a -> new AccountDTO().toDTO(a))
				.collect(Collectors.toList());
		assertTrue(acs.size() == 2);
	}

	@Test
	@Order(7)
	void findPerFirstName() {
		List<AccountDTO> acs = accountService.find("Test", 0, 5).stream().map(a -> new AccountDTO().toDTO(a))
				.collect(Collectors.toList());
		assertTrue(acs.size() == 2);
	}

	@Test
	@Order(8)
	void updateWithException1() {
		logger.warn(assertThrows(DomainException.class, () -> {
			AccountDTO account = new AccountDTO().toDTO(accountService.find("test"));
			account.setUsername("test2");
			accountService.save(account);
		}).getMessage());
	}

	@Test
	@Order(9)
	void updateUsername() throws DomainException {
		AccountDTO account = new AccountDTO().toDTO(accountService.find("test"));
		account.setUsername("test3");
		accountService.save(account);
		assertTrue(accountService.find("test3") != null);

	}

	@Test
	@Order(10)
	void findAll() {
		List<AccountDTO> acs = accountService.findAll().stream().map(a -> new AccountDTO().toDTO(a))
				.collect(Collectors.toList());
		logger.info(acs.toString());
		assertTrue(acs.size() == 2);
	}
}
