package io.andersori.led.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.andersori.led.api.domain.entity.UserLed;
import io.andersori.led.api.domain.service.UserLedService;

@SpringBootTest
@ActiveProfiles("test")
class UserLedTests {
	
	@Autowired
	private UserLedService userService;

	@Test
	void registerrecord() {
		UserLed user = new UserLed();
		user.setUsername("test");
		user.setPassword("1234");
		userService.save(user);
		
		UserLed user2 = userService.get("test").get();
		assertEquals(user.getUsername(), user2.getUsername());
	}

}
