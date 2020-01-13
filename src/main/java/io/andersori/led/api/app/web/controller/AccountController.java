package io.andersori.led.api.app.web.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;
import static io.andersori.led.api.app.web.controller.util.PathConfig.ADMIN_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PROTECTED_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.PUBLIC_PATH;

@RestController
@RequestMapping(VERSION)
public class AccountController implements DomainController<AccountDTO> {

	private static final String PATH = "/accounts";

	private AccountService accountService;
	private final Authentication authentication;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	@PostMapping(PUBLIC_PATH + PATH)
	public AccountDTO save(@RequestBody AccountDTO data) throws DomainException {
		if (data.getLastLogin() != null)
			data.setLastLogin(null);

		return new AccountDTO().toDTO(accountService.register(data));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH + "/{id}")
	public AccountDTO find(@PathVariable Long id) throws DomainException {
		return new AccountDTO().toDTO(accountService.find(id));
	}

	@Override
	@GetMapping(PROTECTED_PATH + PATH)
	public List<AccountDTO> findAll(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		if (page == null && size == null) {
			return accountService.findAll().stream().map(a -> new AccountDTO().toDTO(a)).collect(Collectors.toList());
		} else if (page != null && size == null) {
			return accountService.find(page, 5).stream().map(a -> new AccountDTO().toDTO(a))
					.collect(Collectors.toList());
		} else if (page == null && size != null) {
			return accountService.find(0, size).stream().map(a -> new AccountDTO().toDTO(a))
					.collect(Collectors.toList());
		} else {
			return accountService.find(page, size).stream().map(a -> new AccountDTO().toDTO(a))
					.collect(Collectors.toList());
		}
	}

	@Override
	@PutMapping(PROTECTED_PATH + PATH + "/{id}")
	public AccountDTO update(@PathVariable Long id, @RequestBody AccountDTO data) throws DomainException {
		AccountDTO ac = new AccountDTO().toDTO(accountService.find(id));

		if (data.getEmail() != null)
			ac.setEmail(data.getEmail());

		if (data.getUsername() != null)
			ac.setUsername(data.getUsername());

		if (data.getFirstName() != null)
			ac.setFirstName(data.getFirstName());

		if (data.getLastName() != null)
			ac.setLastName(data.getLastName());

		return new AccountDTO().toDTO(accountService.save(ac));
	}

	@PatchMapping(PROTECTED_PATH + PATH + "/{id}/password")
	public void updatePassword(@RequestBody String password, @PathVariable Long id) throws DomainException {
		AccountDTO ac = new AccountDTO().toDTO(accountService.find(id));
		ac.setPassword(password);
		accountService.changePasswordByUsername(ac.getUsername(), password);
	}

	@PatchMapping(PROTECTED_PATH + PATH + "/{id}/roles")
	public AccountDTO updateRoles(@RequestBody Set<RoleLed> roles, @PathVariable Long id) throws DomainException {
		AccountDTO ac = new AccountDTO().toDTO(accountService.find(id));
		if (roles.contains(RoleLed.ADMIN) && !authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_" + RoleLed.ADMIN.name()))) {
			roles.remove(RoleLed.ADMIN);
		}
		ac.setRoles(roles);
		return new AccountDTO().toDTO(accountService.save(ac));
	}

	@Override
	@DeleteMapping(ADMIN_PATH + PATH + "/{id}")
	public void delete(@PathVariable Long id) throws DomainException {
		accountService.delete(id);
	}

}
