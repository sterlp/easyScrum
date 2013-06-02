package org.easy.scrum.controller.sprint;

import java.io.Serializable;
import java.util.List;
import org.easy.jsf.d3js.burndown.IterationBurndown;
import org.easy.scrum.controller.day.GraphHelper;
import org.easy.scrum.model.BurnDownType;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.model.TeamBE;

public class SprintOverview implements Serializable {
    private static final GraphHelper GH = new GraphHelper();
    private IterationBurndown burnDown;
    private TeamBE team;
    private SprintBE sprint;
    
    public SprintOverview() {
        super();
    }

    public SprintOverview(TeamBE team, SprintBE sprint, List<SprintDayBE> days, BurnDownType burnDownType) {
        this();
        this.team = team;
        this.sprint = sprint;
        burnDown = GH.recalcualteBurndown(sprint, days, burnDownType);
    }

    public IterationBurndown getBurnDown() {
        return burnDown;
    }

    public void setBurnDown(IterationBurndown burnDown) {
        this.burnDown = burnDown;
    }

    public TeamBE getTeam() {
        return team;
    }

    public void setTeam(TeamBE team) {
        this.team = team;
    }

    public SprintBE getSprint() {
        return sprint;
    }

    public void setSprint(SprintBE sprint) {
        this.sprint = sprint;
    }
}
