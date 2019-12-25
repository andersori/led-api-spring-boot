package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import io.andersori.led.api.domain.entity.Account;

public interface AccountService {
	
	public Optional<Account> save(Account account);

	public Optional<Account> delete(Account account);

	public Optional<Account> get(Long id);

	public List<Account> get(int pageNumber, int pageSize);

	public List<Account> get(String name, int pageNumber, int pageSize);

	public List<Account> getAll();
	
}
