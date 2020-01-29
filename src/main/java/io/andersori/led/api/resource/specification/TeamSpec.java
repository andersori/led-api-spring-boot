package io.andersori.led.api.resource.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.entity.Event_;
import io.andersori.led.api.domain.entity.GroupLed_;
import io.andersori.led.api.domain.entity.TeamLed;
import io.andersori.led.api.domain.entity.TeamLed_;

public class TeamSpec implements Specification<TeamLed> {

	private static final long serialVersionUID = 1L;

	private final TeamDTO filter;

	public TeamSpec(TeamDTO filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<TeamLed> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filter.getName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(TeamLed_.name)),
					"%" + filter.getName().toLowerCase() + "%"));
		if (filter.getScore() != null)
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TeamLed_.score), filter.getScore()));
		if (filter.getEventId() != null)
			predicates.add(criteriaBuilder.equal(root.join(TeamLed_.event, JoinType.INNER).get(Event_.id),
					filter.getEventId()));
		if (filter.getGroupId() != null)
			predicates.add(criteriaBuilder.equal(root.join(TeamLed_.group, JoinType.INNER).get(GroupLed_.id),
					filter.getGroupId()));

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
