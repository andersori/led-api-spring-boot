package io.andersori.led.api.resource.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.domain.entity.Event_;
import io.andersori.led.api.domain.entity.GroupLed;
import io.andersori.led.api.domain.entity.GroupLed_;

public class GroupSpec implements Specification<GroupLed> {

	private static final long serialVersionUID = 1L;

	private final GroupDTO filter;

	public GroupSpec(GroupDTO filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<GroupLed> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filter.getName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(GroupLed_.name)),
					"%" + filter.getName().toLowerCase() + "%"));
		if (filter.getEventId() != null)
			predicates.add(criteriaBuilder.equal(root.join(GroupLed_.event).get(Event_.id), filter.getEventId()));

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
