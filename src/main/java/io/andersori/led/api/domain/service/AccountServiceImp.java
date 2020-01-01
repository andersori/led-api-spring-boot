package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.UserLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.resource.repository.AccountRepository;
import io.andersori.led.api.resource.repository.UserLedRepository;

@Service
public class AccountServiceImp implements AccountService {

	private Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);

	private AccountRepository accountRepository;

	private UserLedRepository userLedRepository;

	@Autowired
	public AccountServiceImp(AccountRepository accountRepository, UserLedRepository userLedRepository) {
		this.accountRepository = accountRepository;
		this.userLedRepository = userLedRepository;
	}

	@Override
	public AccountDto save(AccountDto data) throws DomainException {
		try {
			Optional<Account> accountSaved = accountRepository.findById(data.getId());
			if (accountSaved.isPresent()) {
				if (!data.getUsername().equals(accountSaved.get().getUser().getUsername())) {
					if (accountRepository.findByUserUsername(data.getUsername()).isPresent()) {
						throw new ConflictException(AccountService.class, "This username is in use.");
					}
				}

				Account acEntity = data.toEntity();
				acEntity.getUser().setPassword(accountSaved.get().getUser().getPassword());

				return new AccountDto().toDto(accountRepository.save(acEntity));
			} else {
				throw new NotFoundException(AccountService.class,
						"Account with username " + data.getUsername() + " not found.");
			}
		} catch (NotFoundException | ConflictException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(AccountService.class, e.getCause().getMessage(), e.getCause());
		}
	}

	@Override
	public void delete(Long id) {
		accountRepository.deleteById(id);
	}

	@Override
	public AccountDto find(Long id) throws DomainException {
		Optional<Account> account = accountRepository.findById(id);
		if (account.isPresent()) {
			return new AccountDto().toDto(account.get());
		}
		throw new NotFoundException(AccountService.class, "Account with id " + id + " not found.");
	}

	@Override
	public List<AccountDto> find(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return accountRepository.findAll(page).getContent().stream().map(ac -> {
			return new AccountDto().toDto(ac);
		}).collect(Collectors.toList());
	}

	@Override
	public List<AccountDto> find(String firstName, int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return accountRepository.findByFirstNameContaining(firstName, page).getContent().stream().map(ac -> {
			return new AccountDto().toDto(ac);
		}).collect(Collectors.toList());
	}

	@Override
	public List<AccountDto> findAll() {
		return accountRepository.findAll().stream().map(ac -> {
			return new AccountDto().toDto(ac);
		}).collect(Collectors.toList());
	}

	@Override
	public AccountDto find(String username) throws DomainException {
		Optional<Account> account = accountRepository.findByUserUsername(username);
		if (account.isPresent()) {
			return new AccountDto().toDto(account.get());
		}
		throw new NotFoundException(AccountService.class, "Account with username " + username + " not found.");
	}

	@Override
	public Optional<AccountDto> autenticate(String username, String password) {
		Optional<UserLed> user = userLedRepository.findByUsernameAndPassword(username, password);
		if (user.isPresent()) {
			return Optional.of(new AccountDto().toDto(accountRepository.findById(user.get().getId()).get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AccountDto> changePasswordByUsername(String username, String password) {
		Optional<UserLed> user = userLedRepository.findByUsername(username);
		if (user.isPresent()) {
			if (userLedRepository.changePassword(username, password) != 0) {
				return Optional.of(new AccountDto().toDto(accountRepository.findByUserUsername(username).get()));
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<AccountDto> changePasswordByEmail(String email, String password) {
		Optional<Account> account = accountRepository.findByEmail(email);
		if (account.isPresent()) {
			if (userLedRepository.changePassword(account.get().getUser().getUsername(), password) != 0) {
				return Optional.of(new AccountDto()
						.toDto(accountRepository.findByUserUsername(account.get().getUser().getUsername()).get()));
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<AccountDto> register(AccountDto account, String password) {
		try {
			Account acEntity = account.toEntity();
			acEntity.getUser().setPassword(password);

			accountRepository.save(acEntity);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Optional.empty();
		}
		return Optional.of(account);
	}

	@Override
	public String getPassword(String username) throws DomainException {
		try {
			return userLedRepository.getPassword(username);
		} catch(Exception e) {
			throw new DomainException(AccountService.class, e.getCause().getMessage(), e.getCause());
		}
	}

}
