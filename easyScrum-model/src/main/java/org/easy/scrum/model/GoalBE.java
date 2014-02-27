/*
 * Copyright 2013 Paul Sterl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easy.scrum.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.easy.scrum.model.embedded.PersistentPeriod;
import org.easy.validation.html.SafeHtml;
import org.joda.time.LocalDate;

@Entity
@Table(name = "GOAL")
@NamedQueries(
        @NamedQuery(name = GoalBE.Q_GOAL_ALL, query = "SELECT g from GoalBE as g ORDER BY g.period.end DESC")
)
public class GoalBE extends AbstractEntity {

    public static final String Q_GOAL_ALL = "Goal.findAll";

    public static enum GoalAchievement {
        BELOW_TARGET,
        ON_TARGET,
        ABOVE_TARGET,
        SIGNIFICANTELY_ABOVE_TARGET
        ;
        public String getName() {
            return name();
        }
        public String getLocalizationCode() {
            return GoalAchievement.class.getSimpleName() + "_" + name();
        }
    }
   
    public static enum GoalEvaluation {
        DAILY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.toPeriod().getDays();
            }
        },
        WEEKLY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.getWeeksBetween();
            }
        },
        MONTHLY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.getMonthsBetween();
            }
        },
        QUATERLY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.getMonthsBetween() / 4;
            }
        },
        HALF_YEARLY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.getMonthsBetween() / 6;
            }
        },
        
        YEARLY {
            @Override
            public int getPassedTime(PersistentPeriod period) {
                return period.getMonthsBetween() / 12;
            }
        },
        ;
        public String getName() {
            return name();
        }
        public String getLocalizationCode() {
            return GoalEvaluation.class.getSimpleName() + "_" + name();
        }
        /**
         * @param period the period to verify
         * @return the passed evaluation periods, lowest value 0
         */
        public abstract int getPassedTime(PersistentPeriod period);
    }

    @NotNull
    @Size(min = 1, max = 255)
    @SafeHtml
    private String name;
    @Size(min = 0, max = 1024)
    //@SafeHtml(HtmlValidation.Relaxed) -- TODO: is still to restrictive
    @Column(length = 1024)
    private String description;
    /*
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "goal")
    @OrderBy("day DESC")
    private List<GoalViolationBE> violations;
    */
    /**
     * PersistentPeriod in which this goal is evaluated
     */
    @Embedded
    @NotNull
    private PersistentPeriod period = new PersistentPeriod();

    /**
     * The total count of the violations till now
     */
    @Min(0)
    private int violations = 0;
    @Column(name = "violations_allowd_for_significantly")
    @Min(0)
    private int violationsAllowdForSignificantly = 1;
    @Column(name = "violations_allowd_for_above")
    @Min(0)
    private int violationsAllowdForAbove = 4;
    @Column(name = "violations_allowd_for_on")
    @Min(0)
    private int violationsAllowdForOn = 8;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "goal_evaluation")
    private GoalEvaluation goalEvaluation = GoalEvaluation.WEEKLY;
    /** Current Goal Achivement */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "goal_achievement")
    private GoalAchievement goalAchievement = GoalAchievement.SIGNIFICANTELY_ABOVE_TARGET;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "team_id")
    private TeamBE team;
    
    public GoalBE() {
        super();
        if (this.period.getStart() == null) {
            this.period.setStart(new LocalDate().plusDays(1).toDate());
        }
        if (this.period.getEnd() == null) {
            this.period.setEnd(new LocalDate(this.period.getStart()).plusYears(1).toDate());
        }
    }
    public GoalBE(String name, Date start, Date end,
            GoalEvaluation goalEvaluation,
            int violationsAllowdForSignificantly,
            int violationsAllowdForAbove,
            int violationsAllowdForOn) {
        this.name = name;
        this.goalEvaluation = goalEvaluation;
        this.period = new PersistentPeriod(start, end);
        this.violationsAllowdForSignificantly = violationsAllowdForSignificantly;
        this.violationsAllowdForAbove = violationsAllowdForAbove;
        this.violationsAllowdForOn = violationsAllowdForOn;
    }
    public int getGoalViolationMultipyFactor() {
        int total = this.goalEvaluation.getPassedTime(period);
        // not in the past
        if (this.period.getEnd().getTime() > System.currentTimeMillis()) {
            ++total;
            int current = this.goalEvaluation.getPassedTime(
                    new PersistentPeriod(period.getStart(), new Date())) + 1;
            total = Math.min(total, current);
        }
        return total;
    }
    
    /**
     * Adds a goal violation to this goal and recalcualted the current status.
     * 
     * @return the current Goal Status
     */
    public GoalAchievement addViolations() {
        return addViolations(1);
    }
    /**
     * Adds a goal violation to this goal and recalcualted the current status.
     * 
     * @param violationCount the count of violations
     * @return the current Goal Status
     */
    public GoalAchievement addViolations(int violationCount) {
        this.violations += violationCount;
        this.setGoalAchievement(calcualteGoalAchievement());
        return this.goalAchievement;
    }
    
    
    GoalAchievement calcualteGoalAchievement() {
        int goalViolationMultipyFactor = getGoalViolationMultipyFactor();
        
        if ((violationsAllowdForSignificantly * goalViolationMultipyFactor) >= this.violations) {
            return GoalAchievement.SIGNIFICANTELY_ABOVE_TARGET;
        } else if (violationsAllowdForAbove * goalViolationMultipyFactor >= this.violations) {
            return GoalAchievement.ABOVE_TARGET;
        } else if (violationsAllowdForOn * goalViolationMultipyFactor >= this.violations) {
            return GoalAchievement.ON_TARGET;
        } else {
            return GoalAchievement.BELOW_TARGET;
        }
    }
    
    @PrePersist
    @PreUpdate
    void beforeSave() {
        this.goalAchievement = calcualteGoalAchievement();
    }
    
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public GoalEvaluation getGoalEvaluation() {
        return goalEvaluation;
    }

    public void setGoalEvaluation(GoalEvaluation goalEvaluation) {
        this.goalEvaluation = goalEvaluation;
    }

    public GoalAchievement getGoalAchievement() {
        return goalAchievement;
    }

    public void setGoalAchievement(GoalAchievement goalAchievement) {
        this.goalAchievement = goalAchievement;
    }
    
    public TeamBE getTeam() {
        return team;
    }
    public void setTeam(TeamBE team) {
        this.team = team;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PersistentPeriod getPeriod() {
        return period;
    }

    public void setPeriod(PersistentPeriod period) {
        this.period = period;
    }

    public int getViolationsAllowdForSignificantly() {
        return violationsAllowdForSignificantly;
    }

    public void setViolationsAllowdForSignificantly(int violationsAllowdForSignificantly) {
        this.violationsAllowdForSignificantly = violationsAllowdForSignificantly;
    }

    public int getViolationsAllowdForAbove() {
        return violationsAllowdForAbove;
    }

    public void setViolationsAllowdForAbove(int violationsAllowdForAbove) {
        this.violationsAllowdForAbove = violationsAllowdForAbove;
    }

    public int getViolationsAllowdForOn() {
        return violationsAllowdForOn;
    }

    public void setViolationsAllowdForOn(int violationsAllowdForOn) {
        this.violationsAllowdForOn = violationsAllowdForOn;
    }

    public int getViolations() {
        return violations;
    }

    public void setViolations(int violations) {
        this.violations = violations;
    }
}