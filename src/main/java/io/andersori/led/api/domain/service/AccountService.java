package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import io.andersori.led.api.domain.entity.Account;

public interface AccountService extends Service<Account> {
	
	public Optional<Account> get(String username);

	public List<Account> get(String firstName, int pageNumber, int pageSize);
	
}
