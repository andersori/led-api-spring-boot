package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.andersori.led.api.domain.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Page<Account> findByFirstNameContaining(String name, Pageable pageable);
	
	@Query(value = "SELECT c FROM Account AS c WHERE c.user.username = :username")
	Optional<Account> findByUsername(String username);
}
