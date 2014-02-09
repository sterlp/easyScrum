package org.easy.scrum.controller.day;

import java.io.Serializable;
import java.util.List;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.easy.scrum.controller.config.ConfigModel;
import org.easy.scrum.controller.sprint.SprintOverview;
import org.easy.scrum.controller.sprint.SprintTableModel;
import org.easy.scrum.controller.team.TeamModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.model.TeamBE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@javax.enterprise.context.SessionScoped
public class DailyController implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(DailyController.class);
    
    @Inject
    private SprintTableModel teamSprints;
    @Inject
    private SprintDayTableModel sprintDays;
    @Inject 
    private TeamModel teamModel;
    @Inject
    private ConfigModel configModel;

    private SprintOverview overview;
    
    private String selectedTeam;
    private String selectedSprint;
    
    public void reload() {
        teamModel.realod();
        teamSprints.realod();
        sprintDays.realod();

        sprintDays.updateSprint(teamSprints.getElements());
    }
    
    public void pageRender(ComponentSystemEvent event) {
        LOG.debug("pageRender... ");
        // select first the team
        if (selectedTeam != null) {
            LOG.debug("Selecting team {}.", selectedTeam);
            for (TeamBE t : teamModel.getElements()) {
                if (String.valueOf(t.getId()).equals(selectedTeam)
                        || t.getName().equals(selectedTeam)) {
                    
                    // realod sprints of team if needed
                    if (teamSprints.getTeam() == null
                            || !teamSprints.getTeam().getId().equals(t.getId())) {
                        teamSprints.setTeam(t);
                        teamSprints.realod(); // load the correct sprints
                    }
                }
            }
        }
        // select the sprint
        if (selectedSprint != null) {
            LOG.debug("Selecting Sprint {}.", selectedSprint);
            for (SprintBE s : teamSprints.getElements()) {
                if (String.valueOf(s.getId()).equals(selectedSprint)
                        || s.getName().equals(selectedSprint)) {
                    
                    if (sprintDays.getSprint() == null
                            || !sprintDays.getSprint().getId().equals(s.getId())) {
                        sprintDays.setSprint(s);
                        sprintDays.realod();
                    }
                    teamSprints.setSelected(s);
                }
            }
        } else {
            sprintDays.updateSprint(teamSprints.getElements());
        }
        
        
        if (teamSprints.getTeam() != null) this.selectedTeam = String.valueOf(teamSprints.getTeam().getId());
        else this.selectedTeam = null;
        if (sprintDays.getSprint() != null) this.selectedSprint = String.valueOf(sprintDays.getSprint().getId());
        else this.selectedSprint = null;
        LOG.debug("Deep link team: {} sprint {}.", this.selectedTeam, this.selectedSprint);
        
        recalcualteBurndown(teamSprints.getTeam(), sprintDays.getSprint(), sprintDays.getElements());
        LOG.debug("pageRender... success.");
    }
    public void onSelectedTeamChange() {
        LOG.debug("*** onSelectedTeamChange {} ***", teamSprints.getTeam());
        teamSprints.realod();
        sprintDays.setSprint(teamSprints.getFirstElement());
        if (teamSprints.getTeam() != null) this.selectedTeam = String.valueOf(teamSprints.getTeam().getId());
        else this.selectedTeam = null;
        onSelectedSprintChange();
    }
    public void onSelectedSprintChange() {
        LOG.debug("*** onSelectedSprintChange {} ***", this.sprintDays.getSprint());
        if (sprintDays.getSprint() != null) this.selectedSprint = String.valueOf(sprintDays.getSprint().getId());
        else this.selectedSprint = null;
        sprintDays.realod();
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    // direct link
    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = StringUtils.trimToNull(selectedTeam);
    }
    public String getSelectedSprint() {
        return selectedSprint;
    }
    public void setSelectedSprint(String selectedSprint) {
        this.selectedSprint = StringUtils.trimToNull(selectedSprint);
    }
    public SprintTableModel getTeamSprints() {
        return teamSprints;
    }
    public void setTeamSprints(SprintTableModel teamSprints) {
        this.teamSprints = teamSprints;
    }

    public SprintDayTableModel getSprintDays() {
        return sprintDays;
    }

    public void setSprintDays(SprintDayTableModel sprintDays) {
        this.sprintDays = sprintDays;
    }

    private void recalcualteBurndown(TeamBE team, SprintBE sprint, List<SprintDayBE> days) {
        if (sprint != null && days != null) {
            overview = new SprintOverview(team, sprint, days, configModel.getUserConfig().getBurnDownType());
        } else {
            overview = null;
        }
        if (LOG.isDebugEnabled()) {
            //LOG.debug("recalcualteBurndown:\n{}", overview != null ? ToStringBuilder.reflectionToString(overview.getBurnDown(), ToStringStyle.MULTI_LINE_STYLE) : null);
        }
    }

    public SprintOverview getOverview() {
        return overview;
    }

    public void setOverview(SprintOverview overview) {
        this.overview = overview;
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }
}