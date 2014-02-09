package org.easy.scrum.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.scrum.controller.config.ConfigModel;
import org.easy.scrum.controller.sprint.SprintOverview;
import org.easy.scrum.controller.team.TeamModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.service.SprintBF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
public class IndexController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
    @Inject
    private TeamModel teamModel;
    @Inject
    private ConfigModel configModel;
    @EJB
    private SprintBF sprintBF;
    
    private List<SprintOverview> overviews = new ArrayList<SprintOverview>();
    
    public void buildBurndown() {
        LOG.debug("buildBurndown...");
        overviews.clear();
        List<TeamBE> teams = teamModel.getElements();
        for (TeamBE team : teams) {
            SprintBE currentSprint = sprintBF.findMostRecentSprint(team.getId(), true);
            if (currentSprint != null) {
                overviews.add(new SprintOverview(team, currentSprint, currentSprint.getDays(), configModel.getUserConfig().getBurnDownType()));
            }
        }
    }

    public List<SprintOverview> getOverviews() {
        return overviews;
    }

    public void setOverviews(List<SprintOverview> overviews) {
        this.overviews = overviews;
    }
}
