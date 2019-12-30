package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.entity.UserLed;
import lombok.Data;

@Data
public class AccountDto implements Dto<Account, AccountDto> {

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private LocalDateTime lastLogin;
	private String email;
	private List<Event> events = Arrays.asList();
	private Set<RoleLed> roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT));

	@Override
	public AccountDto toDto(Account entity) {
		id = entity.getId();
		username = entity.getUser().getUsername();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		lastLogin = entity.getLastLogin();
		email = entity.getEmail();
		events = entity.getEvents();
		roles = entity.getRoles();
		return this;
	}

	@Override
	public Account toEntity() {
		UserLed user = new UserLed();
		user.setId(id);
		user.setUsername(username);
		user.setPassword("undefined");

		Account entity = new Account();
		entity.setId(id);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setLastLogin(lastLogin);
		entity.setEmail(email);
		entity.setEvents(events);
		entity.setRoles(roles);

		entity.setUser(user);
		return entity;
	}

}
