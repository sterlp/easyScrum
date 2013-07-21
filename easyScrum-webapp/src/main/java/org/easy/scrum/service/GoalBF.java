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
package org.easy.scrum.service;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalViolationBE;

@Stateless
@TransactionAttribute
public class GoalBF extends AbstractFacade<GoalBE, Long> {

    @PersistenceContext
    private EntityManager em;
    
    public GoalBF() {
        super(GoalBE.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    void recalcualteGoal(GoalBE goal, Collection<GoalViolationBE> violations) {
        int multi = 0;
        //goal.getGoalEvaluation().
    }
}
