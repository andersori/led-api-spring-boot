package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.exception.DomainException;

public interface AccountService extends Service<AccountDto> {

	/**
	 * This method should only be acting on upgrade
	 * 
	 * @author andersori
	 * @param data Account already saved to database
	 */
	AccountDto save(AccountDto data) throws DomainException;

	Optional<AccountDto> register(AccountDto account, String password);

	Optional<AccountDto> autenticate(String username, String password);

	Optional<AccountDto> changePasswordByUsername(String username, String password);

	Optional<AccountDto> changePasswordByEmail(String email, String password);

	AccountDto find(String username) throws DomainException;

	List<AccountDto> find(String firstName, int pageNumber, int pageSize);

	String getPassword(String username) throws DomainException;

}
