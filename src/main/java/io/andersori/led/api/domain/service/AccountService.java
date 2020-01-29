package io.andersori.led.api.domain.service;

import java.time.LocalDateTime;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.DomainException;

public interface AccountService extends Service<Account, AccountDTO> {

	/**
	 * This method should only be acting on upgrade
	 * 
	 * @author andersori
	 * @param data Account already saved to database
	 */
	Account save(AccountDTO data) throws DomainException;

	Account register(AccountDTO account) throws DomainException;

	Account changePasswordByUsername(String username, String password) throws DomainException;

	Account changePasswordByEmail(String email, String password) throws DomainException;

	Account changeLastLogin(String username, LocalDateTime lastLogin) throws DomainException;

	Account find(String username) throws DomainException;

	String getPassword(String username) throws DomainException;

}
