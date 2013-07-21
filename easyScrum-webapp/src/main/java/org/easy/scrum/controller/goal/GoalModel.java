package org.easy.scrum.controller.goal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.easy.jsf.table.AbstractTableModel;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.service.GoalBF;

@Named("goalModel")
@SessionScoped
public class GoalModel extends AbstractTableModel<GoalBE, GoalBF> {

    @EJB
    private GoalBF goalBF;
    @Override
    protected GoalBE newModel() {
        return new GoalBE();
    }
    @Override
    protected GoalBF getFacade() {
        return goalBF;
    }
}
