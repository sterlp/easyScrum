/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easy.scrum.controller.sprint;

import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.table.AbstractTableModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.service.SprintBF;
import org.joda.time.LocalDate;
import org.joda.time.Period;

@Named("sprintTableModel")
@SessionScoped
public class SprintTableModel extends AbstractTableModel<SprintBE, SprintBF> {
    
    @Inject
    private SprintBF sprintBF;
            
    protected TeamBE team;

    public SprintTableModel() {
        super();
    }
    
    @Override
    protected SprintBE newModel() {
        SprintBE result = new SprintBE();
        result.setTeam(team);
        if (!this.elements.isEmpty()) {
            result.setStart(
                new LocalDate(this.elements.get(0).getEnd()).plusDays(1).toDate());
            result.newEndDate(Period.weeks(2));
        }
        return result;
    }

    @Override
    protected List<SprintBE> findAll() {
        List<SprintBE> result = Collections.emptyList();
        if (team != null) result = sprintBF.findSprintByTeamId(team.getId());
        return result;
    }

    public TeamBE getTeam() {
        return team;
    }

    public void setTeam(TeamBE team) {
        this.team = team;
    }

    @Override
    protected SprintBF getFacade() {
        return sprintBF;
    }
}
