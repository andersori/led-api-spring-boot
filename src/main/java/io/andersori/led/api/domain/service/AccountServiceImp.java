package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.UserLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.AccountRepository;
import io.andersori.led.api.resource.repository.UserLedRepository;

@Service
public class AccountServiceImp implements AccountService {

	private AccountRepository accountRepository;
	private UserLedRepository userLedRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AccountServiceImp(AccountRepository accountRepository, UserLedRepository userLedRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.userLedRepository = userLedRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Account save(AccountDTO data) throws DomainException {
		try {
			Optional<Account> accountSaved = accountRepository.findById(data.getId());
			if (accountSaved.isPresent()) {
				if (!data.getUsername().equals(accountSaved.get().getUser().getUsername())) {
					if (accountRepository.findByUserUsername(data.getUsername()).isPresent()) {
						throw new ConflictException(AccountService.class, "This username is in use.");
					}
				}

				Account acEntity = data.toEntity(accountSaved.get().getUser());
				acEntity.getUser().setPassword(accountSaved.get().getUser().getPassword());
				return accountRepository.save(acEntity);
			} else {
				throw new NotFoundException(AccountService.class,
						"Account with username " + data.getUsername() + " not found.");
			}
		} catch (NotFoundException | ConflictException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		Optional<Account> account = accountRepository.findById(id);
		if (account.isPresent()) {
			accountRepository.deleteById(id);
			return;
		}
		throw new NotFoundException(AccountService.class, "Account with id " + id + " not found.");
	}

	@Override
	public Account find(Long id) throws DomainException {
		Optional<Account> account = accountRepository.findById(id);
		if (account.isPresent()) {
			return account.get();
		}
		throw new NotFoundException(AccountService.class, "Account with id " + id + " not found.");
	}

	@Override
	public List<Account> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return accountRepository.findAll(page).getContent();
	}

	@Override
	public List<Account> find(String firstName, int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return accountRepository.findByFirstNameContaining(firstName, page).getContent();
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account find(String username) throws DomainException {
		Optional<Account> account = accountRepository.findByUserUsername(username);
		if (account.isPresent()) {
			return account.get();
		}
		throw new NotFoundException(AccountService.class, "Account with username " + username + " not found.");
	}

	@Override
	public Account changePasswordByUsername(String username, String password) throws DomainException {
		Optional<UserLed> user = userLedRepository.findByUsername(username);
		if (user.isPresent()) {
			if (userLedRepository.changePassword(username, passwordEncoder.encode(password)) != 0) {
				return accountRepository.findByUserUsername(username).get();
			}
		}
		throw new NotFoundException(AccountService.class, "Account with username " + username + " not found.");
	}

	@Override
	public Account changePasswordByEmail(String email, String password) throws DomainException {
		Optional<Account> account = accountRepository.findByEmail(email);
		if (account.isPresent()) {
			if (userLedRepository.changePassword(account.get().getUser().getUsername(),
					passwordEncoder.encode(password)) != 0) {
				return accountRepository.findByUserUsername(account.get().getUser().getUsername()).get();
			}
		}
		throw new NotFoundException(AccountService.class, "Account with email " + email + " not found.");
	}

	@Override
	public Account register(AccountDTO account, String password) throws DomainException {
		Optional<UserLed> user = userLedRepository.findByUsername(account.getUsername());
		if (user.isEmpty()) {
			try {
				UserLed userEntity = new UserLed();
				userEntity.setUsername(account.getUsername());
				userEntity.setPassword(passwordEncoder.encode(password));
				Account acEntity = account.toEntity(userEntity);
				return accountRepository.save(acEntity);
			} catch (Exception e) {
				throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
			}
		}
		throw new ConflictException(AccountService.class, "The username entered is already in use");
	}

	@Override
	public String getPassword(String username) throws DomainException {
		try {
			return userLedRepository.getPassword(username);
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

}
