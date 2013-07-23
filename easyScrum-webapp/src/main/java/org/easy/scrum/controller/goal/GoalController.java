package org.easy.scrum.controller.goal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.converter.AbstractConverter;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalBE.GoalEvaluation;
import org.easy.scrum.model.GoalViolationBE;
import org.easy.scrum.service.GoalBF;
import org.easy.scrum.service.GoalViolationBF;

@Named
@SessionScoped
public class GoalController implements Serializable {
    
    @Inject GoalModel goalModel;
    @EJB GoalViolationBF goalViolationBF;
    
    private GoalViolationBE goalViolation = new GoalViolationBE();
    
    private GoalBE violationsGoal;
    private List<GoalViolationBE> violations = new ArrayList<GoalViolationBE>();
    
    public void addGoalViolation(GoalBE goal) {
        this.goalViolation = new GoalViolationBE();
        this.goalViolation.setGoal(goal);
    }
    
    public void saveGoalViolation() {
        goalViolation = goalViolationBF.create(goalViolation);
        this.goalModel.realod();
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("'" + goalViolation.getName() + "' added to Goal " + goalViolation.getGoal().getName() + "."));
    }
    
    public void loadViolations(GoalBE goal) {
        this.violationsGoal = goal;
        violations = goalViolationBF.findAllViolationsForGoal(violationsGoal.getId());
    }
    
    public void deleteGoalViolation(Long violationId) {
        GoalViolationBE remove = goalViolationBF.remove(violationId);
        this.goalModel.realod();
        violations = goalViolationBF.findAllViolationsForGoal(violationsGoal.getId());
        FacesContext context = FacesContext.getCurrentInstance(); 
        if (remove != null) {
            context.addMessage(null, new FacesMessage("'" + goalViolation.getName() + "' deleted."));
        } else {
            context.addMessage(null, new FacesMessage("Already deleted (not found)."));
        }
    }

    public GoalModel getGoalModel() {
        return goalModel;
    }

    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
    }
    
    public List<GoalEvaluation> getGoalEvaluations() {
        return Arrays.asList(GoalBE.GoalEvaluation.values());
    }

    public GoalViolationBE getGoalViolation() {
        return goalViolation;
    }
    public void setGoalViolation(GoalViolationBE goalViolation) {
        this.goalViolation = goalViolation;
    }

    public List<GoalViolationBE> getViolations() {
        return violations;
    }

    public GoalBE getViolationsGoal() {
        return violationsGoal;
    }

    public void setViolationsGoal(GoalBE violationsGoal) {
        this.violationsGoal = violationsGoal;
    }
    
    public void setViolations(List<GoalViolationBE> violations) {
        this.violations = violations;
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
