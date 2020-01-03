package io.andersori.led.api.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.controller.util.PathConfig;
import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@RestController
@RequestMapping(PathConfig.VERSION)
public class AccountController {
	
	private final String PATH = "/accounts";
	private AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping(PathConfig.ADMIN_PATH + PATH + "/{id}")
	public AccountDto getAccount(@PathVariable Long id) throws DomainException {
		AccountDto account = accountService.find(id); 
		return account;
	}
	
	@GetMapping(PathConfig.PROTECTED_PATH + PATH)
	public List<AccountDto> getAccounts() {
		return accountService.findAll();
	}
}
