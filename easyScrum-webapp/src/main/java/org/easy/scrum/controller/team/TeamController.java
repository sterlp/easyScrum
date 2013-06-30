package org.easy.scrum.controller.team;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.jsf.converter.AbstractConverter;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.service.TeamBF;
import org.jsoup.Jsoup;

@Named
@SessionScoped
public class TeamController implements Serializable {
    
    @Inject 
    private TeamModel teamModel;

    public TeamModel getTeamModel() {
        return teamModel;
    }
    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }
    
    @FacesConverter(forClass = TeamBE.class)
    public static class TeamConverter extends AbstractConverter<TeamBE, TeamBF> {
        private transient TeamModel teamModel = null;
        public TeamConverter() {
            super(TeamBE.class);
        }
        @Override
        protected TeamBF getFacade(FacesContext facesContext) {
            return getTeamModel(facesContext).getFacade();
        }
        @Override
        protected List<TeamBE> getLocalCache(FacesContext facesContext) {
            return getTeamModel(facesContext).getElements();
        }
        private TeamModel getTeamModel(FacesContext facesContext) {
            if (teamModel == null) teamModel = (TeamModel)getBeanFromJsfContex(facesContext, "teamModel");
            return teamModel;
        }
    }
}
