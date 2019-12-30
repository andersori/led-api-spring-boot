package io.andersori.led.api.test.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.service.AccountService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class AccountDtoTest {
	
	private Logger logger = LoggerFactory.getLogger(AccountDtoTest.class);

	@Autowired
	private AccountService accountService;
	
	@BeforeAll
	void registerUser() {
		AccountDto account1 = new AccountDto();
		account1.setUsername("test");
		account1.setEmail("email@email.com");
		account1.setFirstName("Test");
		account1.setLastName("Temp");
		
		AccountDto account2 = new AccountDto();
		account2.setUsername("test2");
		account2.setEmail("email2@email.com");
		account2.setFirstName("Testing");
		account2.setLastName("Temp");

		accountService.register(account1, "1234");
		accountService.register(account2, "1234");
	}
	
	@Test
	void find() {
		Optional<AccountDto> account = accountService.find("test");
		
		logger.info(account.toString());
		assertTrue(account.isPresent() && account.get().getUsername() == "test");
	}
	
	@Test
	void autenticate() {
		Optional<AccountDto> account = accountService.autenticate("test", "1234");
		
		assertTrue(account.isPresent());
	}
	
	@Test
	void update() {
		Optional<AccountDto> account = accountService.find("test");
		if(account.isPresent()) {
			account.get().setFirstName("Test 2");
			accountService.save(account.get());
			
			account = accountService.find("test");
			Optional<AccountDto> accountAuth = accountService.autenticate("test", "1234");
			assertTrue(account.isPresent() && accountAuth.isPresent() && account.get().getFirstName().equals("Test 2"));
		} else {
			fail();
		}
	}
	
	@Test
	void changePasswordByEmail() {
		accountService.changePasswordByEmail("email@email.com", "12345");
		
		Optional<AccountDto> account = accountService.autenticate("test", "12345");
		assertTrue(account.isPresent());
	}
	
	@Test
	void changePasswordByUsername() {
		accountService.changePasswordByUsername("test", "123");
		Optional<AccountDto> account = accountService.autenticate("test", "123");
		assertTrue(account.isPresent());
	}
	
	@Test
	void findWithPages() {
		List<AccountDto> acs = accountService.find(0, 5);
		assertTrue(acs.size() == 2);
	}
	
	@Test
	void findAll() {
		List<AccountDto> acs = accountService.findAll();
		assertTrue(acs.size() == 2);
	}
	
	@Test
	void findPerFirstName() {
		List<AccountDto> acs = accountService.find("Test", 0, 5);
		assertTrue(acs.size() == 2);
	}
	
}
