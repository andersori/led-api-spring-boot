package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.entity.UserLed;
import lombok.Data;

@Data
public class AccountDTO implements DTO<Account, AccountDTO> {

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private LocalDateTime lastLogin;
	private String email;
	private Set<RoleLed> roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT));

	@Override
	public AccountDTO toDTO(Account entity) {
		id = entity.getId();
		username = entity.getUser().getUsername();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		lastLogin = entity.getLastLogin();
		email = entity.getEmail();
		roles = entity.getRoles();
		return this;
	}

	public Account toEntity(UserLed user) {
		Account entity = new Account();
		user.setUsername(username);
		entity.setId(id);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setLastLogin(lastLogin);
		entity.setEmail(email);
		entity.setRoles(roles);
		entity.setUser(user);
		entity.setEvents(Arrays.asList());
		return entity;

	}

}
