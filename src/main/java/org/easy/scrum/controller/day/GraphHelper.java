package org.easy.scrum.controller.day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.easy.jsf.d3js.burndown.IterationBurndown;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.joda.time.LocalDate;

public class GraphHelper {
    public IterationBurndown recalcualteBurndown(SprintBE sprint, List<SprintDayBE> days) {
        IterationBurndown burnDown = null;
        if (sprint != null && days != null) {
            burnDown = new IterationBurndown(
                new LocalDate(sprint.getStart()), 
                new LocalDate(sprint.getEnd()), 
                sprint.getPlannedHours());

            List<SprintDayBE> elements = new ArrayList<SprintDayBE>(days);
            Collections.reverse(elements);

            for (SprintDayBE day : elements) {
                if (day.getUpscalling() > 0) {
                    burnDown.addDay(
                            new LocalDate(day.getDay()), 
                            day.getUpscalling() * (-1), day.getReasonForUpscalling());
                }
                burnDown.addDay(
                            new LocalDate(day.getDay()), 
                            day.getBurnDown(), day.getComment());
            }
        }
        return burnDown;
    }
}
