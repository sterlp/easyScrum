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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.easy.validation.date.InDateRange;
import org.easy.validation.html.HtmlValidation;
import org.easy.validation.html.SafeHtml;

@Entity
@Table(name = "GOAL_VIOLATION")
@NamedQueries({
    @NamedQuery(name = GoalViolationBE.Q_ALL, query = "SELECT v FROM GoalViolationBE AS v ORDER BY v.day DESC"),
    @NamedQuery(name = GoalViolationBE.Q_BY_GOAL_ID, query = "SELECT v FROM GoalViolationBE AS v WHERE v.goal.id = :id ORDER BY v.day DESC")
})

public class GoalViolationBE extends AbstractEntity {

    public static final String Q_ALL = "GoalViolationBE.all";
    public static final String Q_BY_GOAL_ID = "GoalViolationBE.byGoalId";
    
    @NotNull
    @Size(min = 1, max = 1024)
    @SafeHtml(HtmlValidation.Relaxed)
    private String name;
    
    @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "goal_id", nullable = false, updatable = false)
    @NotNull 
    private GoalBE goal;
    
    @NotNull
    @InDateRange
    @Column(name = "violation_day")
    @Temporal(TemporalType.DATE)
    private Date day = new Date();

    public GoalViolationBE() {
        super();
    }
    
    public GoalViolationBE(String name, GoalBE goal, Date day) {
        this();
        this.name = name;
        this.goal = goal;
        this.day = day;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public GoalBE getGoal() {
        return goal;
    }

    public void setGoal(GoalBE goal) {
        this.goal = goal;
    }
}
