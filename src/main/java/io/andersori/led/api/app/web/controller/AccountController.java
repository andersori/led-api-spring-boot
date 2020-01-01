package io.andersori.led.api.app.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.service.AccountService;

@RestController
@RequestMapping("accounts")
public class AccountController {
	
	private AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping("/{id}")
	public AccountDto getAccount(@PathVariable Long id) {
		
		Optional<AccountDto> account = accountService.find(id); 
		return account.isPresent() ? account.get() : null;
	}
	
	@GetMapping()
	public List<AccountDto> getAccounts() {
		return accountService.findAll();
	}
}
