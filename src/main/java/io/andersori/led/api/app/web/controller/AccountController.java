package io.andersori.led.api.app.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.controller.util.PathConfig;
import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@RestController
@RequestMapping(PathConfig.VERSION)
public class AccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private static final String PATH = "/accounts";
	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping(PathConfig.PROTECTED_PATH + PATH + "/{id}")
	public AccountDTO getAccount(@PathVariable Long id) throws DomainException {
		return new AccountDTO().toDTO(accountService.find(id));
	}

	@GetMapping(PathConfig.PROTECTED_PATH + PATH)
	public List<AccountDTO> getAccounts() {
		return accountService.findAll().stream().map(a -> new AccountDTO().toDTO(a)).collect(Collectors.toList());
	}
	
	@PostMapping(PathConfig.PUBLIC_PATH + PATH)
	public AccountDTO register(@RequestBody AccountDTO account) throws DomainException {
		try {
			return new AccountDTO().toDTO(accountService.register(account));
		} catch(Exception e) {
			LOGGER.info(e.getMessage());
			throw e;
		}
	}
}
