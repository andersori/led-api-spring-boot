package io.andersori.led.api.resource.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.domain.entity.Participant;
import io.andersori.led.api.domain.entity.Participant_;
import io.andersori.led.api.domain.entity.TeamLed_;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParticipantSpec implements Specification<Participant> {
	private static final long serialVersionUID = -2242668118200803597L;

	private final ParticipantDTO filter;

	@Override
	public Predicate toPredicate(Root<Participant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filter.getIdTeam() != null)
			predicates.add(criteriaBuilder.equal(root.join(Participant_.team).get(TeamLed_.id), filter.getIdTeam()));
		if (filter.getName() != null)
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Participant_.name)),
					"%" + filter.getName().toLowerCase() + "%"));

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
