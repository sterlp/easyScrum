package org.easy.scrum.controller.team;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.easy.jsf.table.AbstractTableModel;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.service.TeamBF;

@Named("teamModel")
@SessionScoped
public class TeamModel extends AbstractTableModel<TeamBE, TeamBF> {

    @EJB
    private TeamBF teamService;
    @Override
    protected TeamBE newModel() {
        return new TeamBE();
    }
    @Override
    protected TeamBF getFacade() {
        return teamService;
    }
}
