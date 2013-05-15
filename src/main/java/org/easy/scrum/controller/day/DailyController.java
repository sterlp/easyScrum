package org.easy.scrum.controller.day;

import java.io.Serializable;
import java.util.List;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    private SprintOverview overview;
    
    public void reload() {
        teamModel.realod();
        teamSprints.realod();
        sprintDays.realod();

        sprintDays.updateSprint(teamSprints.getElements());
    }
    
    public void pageRender(ComponentSystemEvent event) {
        recalcualteBurndown(teamSprints.getTeam(), sprintDays.getSprint(), sprintDays.getElements());

        sprintDays.updateSprint(teamSprints.getElements());
    }
    public void onSelectedTeamChange() {
        LOG.debug("*** onSelectedTeamChange ***");
        teamSprints.realod();
        sprintDays.setSprint(teamSprints.getFirstElement());
        onSelectedSprintChange();
    }
    public void onSelectedSprintChange() {
        LOG.debug("*** onSelectedSprintChange ***");
        sprintDays.realod();
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
            sprint.setDays(days);
            overview = new SprintOverview(team, sprint);
        } else {
            overview = null;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("recalcualteBurndown:\n{}", 
                    overview != null ? ToStringBuilder.reflectionToString(overview.getBurnDown(), ToStringStyle.MULTI_LINE_STYLE) : null);
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