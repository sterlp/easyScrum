/*
 * Copyright 2013 Johanna.
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
package org.easy.scrum.service;

import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalBE.GoalEvaluation;
import org.easy.scrum.model.embedded.PersistentPeriod;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoalsTest {
    
    @Test
    public void testGoalEvaluationTimeTwoDays() {
        PersistentPeriod p = new PersistentPeriod(
                new LocalDate().minusDays(4),
                new LocalDate());

        assertEquals(4,
            GoalEvaluation.DAILY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.WEEKLY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.MONTHLY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.QUATERLY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.HALF_YEARLY.getPassedTime(p));
    }
    
    @Test
    public void testGoalEvaluationTime9Weeks() {
        PersistentPeriod p = new PersistentPeriod(
                new LocalDate().minusWeeks(9),
                new LocalDate());

        System.err.println(p);
        assertEquals(9,
            GoalEvaluation.WEEKLY.getPassedTime(p));
        assertEquals(2,
            GoalEvaluation.MONTHLY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.QUATERLY.getPassedTime(p));
        assertEquals(0,
            GoalEvaluation.HALF_YEARLY.getPassedTime(p));
    }
    @Test
    public void testGoalEvaluationTime2Quarter() {
        PersistentPeriod p = new PersistentPeriod(
                new LocalDate().minusMonths(6).minusDays(1),
                new LocalDate());

        System.err.println(p);
        assertEquals(6,
            GoalEvaluation.MONTHLY.getPassedTime(p));
        assertEquals(1,
            GoalEvaluation.QUATERLY.getPassedTime(p));
        assertEquals(1,
            GoalEvaluation.HALF_YEARLY.getPassedTime(p));
    }
    
    @Test
    public void testPastGoal() {
        GoalBE g = new GoalBE(
                "Verify CI Builds",
                new DateMidnight(2011, 12, 31).toDate(),
                new DateMidnight(2012, 12, 31).toDate(),
                GoalBE.GoalEvaluation.MONTHLY,
                2, 10, 20);
        assertEquals(12, g.getGoalViolationMultipyFactor());
        
        g = new GoalBE(
                "Verify CI Builds",
                new LocalDate(2012, 1, 1).toDate(),
                new LocalDate(2012, 12, 31).toDate(),
                GoalBE.GoalEvaluation.WEEKLY,
                2, 10, 20);
        assertEquals(52, g.getGoalViolationMultipyFactor());
        
        g = new GoalBE(
                "Verify CI Builds",
                new DateMidnight(2011, 6, 30).toDate(),
                new DateMidnight(2012, 6, 30).toDate(),
                GoalBE.GoalEvaluation.MONTHLY,
                2, 10, 20);
        assertEquals(12, g.getGoalViolationMultipyFactor());
    }
    
    @Test
    public void testRunningGoal() {
        GoalBE g = new GoalBE(
                "Verify CI Builds",
                new DateMidnight().minusMonths(2).toDate(),
                new DateMidnight().plusMonths(2).toDate(),
                GoalBE.GoalEvaluation.MONTHLY,
                2, 10, 20);
        assertEquals(3, g.getGoalViolationMultipyFactor());
        
        g = new GoalBE(
                "Verify CI Builds",
                new DateMidnight().minusWeeks(2).toDate(),
                new DateMidnight().plusMonths(2).toDate(),
                GoalBE.GoalEvaluation.WEEKLY,
                2, 10, 20);
        assertEquals(3, g.getGoalViolationMultipyFactor());
    }
}