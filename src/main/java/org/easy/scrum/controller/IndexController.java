package org.easy.scrum.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.scrum.controller.sprint.SprintOverview;
import org.easy.scrum.controller.team.TeamModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.service.SprintBF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ViewScoped
public class IndexController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
    @Inject
    private TeamModel teamModel;
    @EJB
    private SprintBF sprintBF;
    
    private List<SprintOverview> overviews = new ArrayList<SprintOverview>();
    
    @PostConstruct
    protected void afterCreate() {
        LOG.debug("afterCreate...");
        List<TeamBE> teams = teamModel.getElements();
        for (TeamBE team : teams) {
            SprintBE currentSprint = sprintBF.findMostRecentSprint(team.getId(), true);
            if (currentSprint != null) {
                overviews.add(new SprintOverview(team, currentSprint));
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
