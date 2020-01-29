package io.andersori.led.api.resource.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.andersori.led.api.app.web.dto.AccountDTO;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.Account_;
import io.andersori.led.api.domain.entity.UserLed_;

public class AccountSpec implements Specification<Account> {

	private static final long serialVersionUID = 1L;

	private final AccountDTO filter;

	public AccountSpec(AccountDTO filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filter.getEmail() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Account_.email)),
					"%" + filter.getEmail().toLowerCase() + "%"));

		if (filter.getFirstName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Account_.firstName)),
					"%" + filter.getFirstName().toLowerCase() + "%"));

		if (filter.getLastName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Account_.lastName)),
					"%" + filter.getLastName().toLowerCase() + "%"));

		if (filter.getUsername() != null) {
			predicates.add(criteriaBuilder.like(
					criteriaBuilder.lower(root.join(Account_.user, JoinType.INNER).get(UserLed_.username)),
					"%" + filter.getUsername().toLowerCase() + "%"));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
