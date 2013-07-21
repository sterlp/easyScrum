package org.easy.scrum.controller.goal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.converter.AbstractConverter;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalBE.GoalEvaluation;
import org.easy.scrum.service.GoalBF;

@Named
@SessionScoped
public class GoalController implements Serializable {
    
    @Inject GoalModel goalModel;

    public GoalModel getGoalModel() {
        return goalModel;
    }

    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
    }
    
    public List<GoalEvaluation> getGoalEvaluations() {
        return Arrays.asList(GoalBE.GoalEvaluation.values());
    }
    
    @FacesConverter(forClass = GoalEvaluation.class)
    public static class GoalEvaluationConverter extends EnumConverter {
        public GoalEvaluationConverter() {
            super(GoalEvaluation.class);
        }
    }
    @FacesConverter(forClass = GoalBE.class)
    public static class GoalConverter extends AbstractConverter<GoalBE, GoalBF> {
        private transient GoalModel goalModel = null;
        public GoalConverter() {
            super(GoalBE.class);
        }
        @Override
        protected GoalBF getFacade(FacesContext facesContext) {
            return getGoalModel(facesContext).getFacade();
        }
        @Override
        protected List<GoalBE> getLocalCache(FacesContext facesContext) {
            return getGoalModel(facesContext).getElements();
        }
        private GoalModel getGoalModel(FacesContext facesContext) {
            if (goalModel == null) goalModel = (GoalModel)getBeanFromJsfContex(facesContext, "goalModel");
            return goalModel;
        }
    }
}
