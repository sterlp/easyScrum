package org.easy.scrum.controller.day;

import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.table.AbstractTableModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.service.SprintDayBF;
import org.joda.time.LocalDate;

@Named
@SessionScoped
public class SprintDayTableModel extends AbstractTableModel<SprintDayBE, SprintDayBF> {

    /** Sprint selected */
    protected SprintBE sprint;
    
    @Inject
    protected SprintDayBF sprintDayBF;

    public SprintDayTableModel() {
        super();
    }

    @Override
    protected SprintDayBE newModel() {
        SprintDayBE result = new SprintDayBE();
        result.setSprint(sprint);
        // set the first date if possbile :)
        if (this.elements.isEmpty()) {
            if (sprint != null) {
                result.setDay(new LocalDate(sprint.getStart()).plusDays(1).toDate());
            }
        } else {
            result.setDay(new LocalDate(elements.get(0).getDay()).plusDays(1).toDate());
        }
        return result;
    }
    @Override
    protected List<SprintDayBE> findAll() {
        List<SprintDayBE> result = Collections.emptyList();
        if (sprint != null) result = sprintDayBF.findAllBySprintId(sprint.getId());
        return result;
    }

    public SprintBE getSprint() {
        return sprint;
    }
    public void setSprint(SprintBE sprint) {
        this.sprint = sprint;
    }

    @Override
    protected SprintDayBF getFacade() {
        return sprintDayBF;
    }

    /** repalces the selected sprint using the list, if not found null will be selected */
    void updateSprint(List<SprintBE> elements) {
        SprintBE current = getSprint();
        setSprint(null);
        if (current != null) {
            for (SprintBE s : elements) {
                if (current.getId().equals(s.getId())) {
                    setSprint(s);
                    break;
                }
            }
        }
    }
}
