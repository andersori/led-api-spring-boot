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
	public Optional<AccountDto> save(AccountDto data) {
		Optional<Account> accountSaved = accountRepository.findByUserUsername(data.getUsername());
		if(accountSaved.isPresent()) {
			Account acEntity = data.toEntity();
			acEntity.getUser().setPassword(accountSaved.get().getUser().getPassword());
			
			return Optional.of(new AccountDto().toDto(accountRepository.save(acEntity)));
		}
		return Optional.empty();
	}

	@Override
	public void delete(Long id) {
		accountRepository.deleteById(id);
	}

	@Override
	public Optional<AccountDto> find(Long id) {
		Optional<Account> account = accountRepository.findById(id);
		return account.isPresent() ? Optional.of(new AccountDto().toDto(account.get())) : Optional.empty();
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
	public Optional<AccountDto> find(String username) {
		Optional<Account> account = accountRepository.findByUserUsername(username);
		return account.isPresent() ? Optional.of(new AccountDto().toDto(account.get())) : Optional.empty();
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

}
