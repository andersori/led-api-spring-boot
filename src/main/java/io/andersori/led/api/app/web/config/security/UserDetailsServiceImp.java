package io.andersori.led.api.app.web.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
	
	private AccountService accountService;
	
	@Autowired
	public UserDetailsServiceImp(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AccountDto account = accountService.find(username);
			String password = accountService.getPassword(username);
			return new User(account.getUsername(), password, account.getRoles());
		} catch(DomainException e) {
			throw new UsernameNotFoundException(username, e);
		}
	}

}
