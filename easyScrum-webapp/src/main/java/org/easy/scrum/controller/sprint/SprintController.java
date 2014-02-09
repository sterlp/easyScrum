package org.easy.scrum.controller.sprint;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.converter.AbstractConverter;
import org.easy.scrum.controller.team.TeamModel;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.service.SprintBF;

@Named
@SessionScoped
public class SprintController implements Serializable {
    @Inject
    private SprintTableModel sprintModel;
    
    @Inject
    private TeamModel teamModel;
    
    public void selectedTeamChanged() {
        sprintModel.realod();
    }

    public SprintTableModel getSprintModel() {
        return sprintModel;
    }

    public void setSprintModel(SprintTableModel sprintModel) {
        this.sprintModel = sprintModel;
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }

    @FacesConverter(forClass = SprintBE.class)
    public static final class SprintConverter extends AbstractConverter<SprintBE, SprintBF> {
        private transient SprintTableModel sprintTableModel;
        public SprintConverter() {
            super(SprintBE.class);
        }
        @Override
        protected SprintBF getFacade(FacesContext facesContext) {
            return getModel(facesContext).getFacade();
        }

        @Override
        protected List<SprintBE> getLocalCache(FacesContext facesContext) {
            return getModel(facesContext).getElements();
        }
        
        private SprintTableModel getModel(FacesContext facesContext) {
            if (sprintTableModel == null) sprintTableModel = (SprintTableModel)getBeanFromJsfContex(facesContext, "sprintTableModel");
            return sprintTableModel;
        }
    }
}
