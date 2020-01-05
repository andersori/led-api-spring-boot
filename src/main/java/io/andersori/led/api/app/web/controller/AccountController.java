package io.andersori.led.api.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.controller.util.PathConfig;
import io.andersori.led.api.app.web.dto.AccountDTO;
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
	public AccountDTO getAccount(@PathVariable Long id) throws DomainException {
		AccountDTO account = accountService.find(id); 
		return account;
	}
	
	@GetMapping(PathConfig.PROTECTED_PATH + PATH)
	public List<AccountDTO> getAccounts() {
		return accountService.findAll();
	}
}
