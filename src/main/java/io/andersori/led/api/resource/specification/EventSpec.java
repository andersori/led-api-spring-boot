package io.andersori.led.api.resource.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.domain.entity.Account_;
import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.Event_;
import io.andersori.led.api.domain.entity.UserLed_;

public class EventSpec implements Specification<Event> {

	private static final long serialVersionUID = 1L;

	private final EventDTO filter;

	public EventSpec(EventDTO filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filter.getDate() != null)
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Event_.date), filter.getDate()));
		if (filter.getDescription() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.description)),
					"%" + filter.getDescription().toLowerCase() + "%"));
		if (filter.getName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.name)),
					"%" + filter.getName().toLowerCase() + "%"));
		if (filter.getOwnerUsername() != null)
			predicates
					.add(criteriaBuilder.like(
							criteriaBuilder.lower(root.join(Event_.owner, JoinType.INNER)
									.join(Account_.user, JoinType.INNER).get(UserLed_.username)),
							"%" + filter.getOwnerUsername() + "%"));

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
