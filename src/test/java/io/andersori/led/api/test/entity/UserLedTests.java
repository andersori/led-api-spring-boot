package io.andersori.led.api.test.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.UserLed;
import io.andersori.led.api.domain.service.AccountService;
import io.andersori.led.api.domain.service.UserLedService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class UserLedTests {

	@Autowired
	private UserLedService userService;

	@Autowired
	private AccountService accountService;

	@BeforeAll
	void registerUser() {
		UserLed user = new UserLed();
		user.setUsername("test");
		user.setPassword("1234");

		Account account = new Account();
		account.setEmail("email@email.com");
		account.setFirstName("Test");
		account.setLastName("Temp");
		account.setUser(user);

		accountService.save(account);

	}

	@Test
	void findAccount() {
		assertTrue(accountService.getAll().size() > 0);
	}

	@Test
	void checkId() {
		Account acco = accountService.get("test").get();
		UserLed user = userService.get("test").get();
		assertTrue(acco.getId() == user.getId());
	}

	@Test
	void autenticate() {
		Optional<UserLed> test = userService.get("test", "1234");
		assertTrue(test.isPresent());
	}
	
	@Test
	void update() {
		Account account = accountService.get("test").get();
		account.setFirstName("Test 2");
		accountService.save(account);
		
		assertTrue(account.getFirstName().equals(accountService.get("test").get().getFirstName()));
	}
	
	@Test
	void delete() {
		UserLed user = new UserLed();
		user.setUsername("test_2");
		user.setPassword("1234");

		Account account = new Account();
		account.setEmail("email@email.com");
		account.setFirstName("Test");
		account.setLastName("Temp");
		account.setUser(user);

		accountService.save(account);
		accountService.delete(account.getId());
		
		
		assertTrue(accountService.get("test_2").isEmpty());
	}

}
