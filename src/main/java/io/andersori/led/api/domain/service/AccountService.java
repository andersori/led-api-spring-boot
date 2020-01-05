package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.exception.DomainException;

public interface AccountService extends Service<AccountDTO> {

	/**
	 * This method should only be acting on upgrade
	 * 
	 * @author andersori
	 * @param data Account already saved to database
	 */
	AccountDTO save(AccountDTO data) throws DomainException;

	AccountDTO register(AccountDTO account, String password) throws DomainException;

	AccountDTO changePasswordByUsername(String username, String password) throws DomainException;

	AccountDTO changePasswordByEmail(String email, String password) throws DomainException;

	AccountDTO find(String username) throws DomainException;

	List<AccountDTO> find(String firstName, int pageNumber, int pageSize);

	String getPassword(String username) throws DomainException;

}
