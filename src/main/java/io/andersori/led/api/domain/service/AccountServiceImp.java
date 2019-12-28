package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.resource.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {
	
	private AccountRepository accountRepository;
	
	@Autowired
	public AccountServiceImp(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public Optional<Account> save(Account account) {
		try{
			return Optional.of(this.accountRepository.save(account));
		} catch (Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<Account> delete(Account account) {
		try{
			this.accountRepository.delete(account);
			return Optional.of(account);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Account> get(Long id) {
		return this.accountRepository.findById(id);
	}

	@Override
	public List<Account> get(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return this.accountRepository.findAll(page).getContent();
	}

	@Override
	public List<Account> get(String firstName, int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return this.accountRepository.findByFirstNameContaining(firstName, page).getContent();
	}

	@Override
	public List<Account> getAll() {
		return this.accountRepository.findAll();
	}

}
