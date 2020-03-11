package io.andersori.led.api.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.andersori.led.api.app.web.dto.EventDTO;
import io.andersori.led.api.app.web.dto.GroupDTO;
import io.andersori.led.api.app.web.dto.ParticipantDTO;
import io.andersori.led.api.app.web.dto.TeamDTO;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.service.GroupLedService;
import io.andersori.led.api.domain.service.ParticipantService;
import io.andersori.led.api.domain.service.TeamLedService;

public abstract class HelperFacade {

	private static final Random RAND = new Random();

	private static final GroupLedService GROUP_SERVICE;
	private static final TeamLedService TEAM_SERVICE;
	private static final ParticipantService PARTICIPANT_SERVICE;

	static {
		GROUP_SERVICE = BeanUtil.getBean(GroupLedService.class);
		TEAM_SERVICE = BeanUtil.getBean(TeamLedService.class);
		PARTICIPANT_SERVICE = BeanUtil.getBean(ParticipantService.class);
	}

	public static String secretGenerator() {
		final String LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

		StringBuilder secret = new StringBuilder();
		while (secret.toString().length() != 6) {
			secret.append(LEXICON.charAt(RAND.nextInt(LEXICON.length())));
		}
		return secret.toString();
	}

	public static synchronized void teamSelector(ParticipantDTO participant, EventDTO event) throws DomainException {
		try {
			if (participant.getId() != null) {
				List<TeamDTO> teams = TEAM_SERVICE.find(event).stream().map(team -> new TeamDTO().toDTO(team))
						.collect(Collectors.toList());

				for (TeamDTO t : teams) {
					if (t.getGroupId() == null) {
						throw new DomainException(HelperFacade.class,
								"Team " + t.getName() + " is not yet part of a group.");
					}
				}

				List<ParticipantDTO> participants = PARTICIPANT_SERVICE.find(event).stream()
						.map(parti -> new ParticipantDTO().toDTO(parti)).collect(Collectors.toList());
				
				int maxPerTeam = (int) Math.floor(participants.size() / teams.size());
				
				maxPerTeam = maxPerTeam == 0 ? maxPerTeam + 1 : maxPerTeam;
				
				List<TeamDTO> allowedTeams = new ArrayList<TeamDTO>();
				for (TeamDTO t : teams) {
					List<ParticipantDTO> partis = participants.stream()
							.filter(p -> p.getIdTeam() != null && p.getIdTeam() == t.getId())
							.collect(Collectors.toList());

					if (partis.size() < maxPerTeam) {
						allowedTeams.add(t);
					}

				}

				TeamDTO team = null;
				if (allowedTeams.size() > 0) {
					team = allowedTeams.get(RAND.nextInt(allowedTeams.size()));
				} else {
					team = teams.get(RAND.nextInt(teams.size()));
				}
				PARTICIPANT_SERVICE.updateTeam(participant, team);
			} else {
				throw new NotFoundException(HelperFacade.class, "Participant " + participant.getName()
						+ " unregistered, register before attempting to define a team.");
			}
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainException(HelperFacade.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	public static synchronized void groupSelector(TeamDTO team, EventDTO event) throws DomainException {
		try {
			if (team.getId() != null) {
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
