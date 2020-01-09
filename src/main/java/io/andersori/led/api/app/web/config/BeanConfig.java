package io.andersori.led.api.app.web.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@Configuration
public class BeanConfig {

	@Bean
	public PasswordEncoder passwordEnconder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public CommandLineRunner insertUser(AccountService service) throws DomainException {
		return (String[] args) -> {
			AccountDTO ac = new AccountDTO();
			ac.setFirstName("Test");
			ac.setLastName("Temp");
			ac.setUsername("test");
			
			service.register(ac,"1234");
		};
	}
}
