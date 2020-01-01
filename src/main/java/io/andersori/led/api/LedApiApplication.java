package io.andersori.led.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.service.AccountService;

@SpringBootApplication
public class LedApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedApiApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run(AccountService acService) {
		return (String[] args) -> {
			AccountDto account = new AccountDto();
			account.setFirstName("Test");
			account.setLastName("Temp");
			account.setUsername("test");
			
			acService.register(account, "1234");
		};
	}
}
