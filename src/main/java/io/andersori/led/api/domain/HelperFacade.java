package io.andersori.led.api.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.service.GroupLedService;
import io.andersori.led.api.domain.service.TeamLedService;

public abstract class HelperFacade {

	private static final Random RAND = new Random();

	private static final GroupLedService GROUP_SERVICE;
	private static final TeamLedService TEAM_SERVICE;

	static {
		GROUP_SERVICE = BeanUtil.getBean(GroupLedService.class);
		TEAM_SERVICE = BeanUtil.getBean(TeamLedService.class);
	}

	public static String secretGenerator() {
		final String LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

		StringBuilder secret = new StringBuilder();
		while (secret.toString().length() != 6) {
			secret.append(LEXICON.charAt(RAND.nextInt(LEXICON.length())));
		}
		return secret.toString();
	}

	public static synchronized void groupSelector(TeamDTO team, EventDTO event) throws DomainException {
		try {
			if (team.getId() != null) {
				if (!team.isVerified()) {
					throw new DomainException(HelperFacade.class, "Team " + team.getName() + " unverified.");
				}

				List<GroupDTO> groups = GROUP_SERVICE.find(event).stream().map(g -> new GroupDTO().toDTO(g))
						.collect(Collectors.toList());

				List<TeamDTO> teams = TEAM_SERVICE.find(event).stream().map(g -> new TeamDTO().toDTO(g))
						.collect(Collectors.toList());

				int maxPerGroup = (int) Math.floor(teams.size() / groups.size());

				maxPerGroup = maxPerGroup == 0 ? maxPerGroup + 1 : maxPerGroup;

				List<GroupDTO> allowedGroups = new ArrayList<GroupDTO>();
				for (GroupDTO g : groups) {
					List<TeamDTO> participatingTeams = teams.stream()
							.filter(t -> t.getGroupId() != null && t.getGroupId() == g.getId())
							.collect(Collectors.toList());

					if (participatingTeams.size() < maxPerGroup) {
						allowedGroups.add(g);
					}

				}

				GroupDTO group = null;
				if (allowedGroups.size() > 0) {
					group = allowedGroups.get(RAND.nextInt(allowedGroups.size()));
				} else {
					group = groups.get(RAND.nextInt(groups.size()));
				}
				TEAM_SERVICE.updateGroup(team.getId(), group.getId(), team.getSecret());
			} else {
				throw new NotFoundException(HelperFacade.class,
						"Team " + team.getName() + " unregistered, register before attempting to define a group.");
			}
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(HelperFacade.class, e.getCause() != null ? e.getCause() : e);
		}
	}
}
