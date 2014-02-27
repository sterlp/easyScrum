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

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalViolationBE;
import org.easy.scrum.service.exception.EasyScrumException.GoalNotFoundException;

@Stateless
@TransactionAttribute
public class GoalViolationBF  extends AbstractFacade<GoalViolationBE, Long> {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private GoalBF goalBF;
    
    public GoalViolationBF() {
        super(GoalViolationBE.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Adds the violation and updates the coresponding goal.
     * @throws GoalNotFoundException if the goal was not found
     */
    @Override
    public GoalViolationBE create(@Valid GoalViolationBE goalViolation) {
        GoalBE goal = goalBF.find(goalViolation.getGoal().getId());
        if (goal == null) throw new GoalNotFoundException();
        goal.addViolations();
        //goalViolation.setGoal(goal);
        return super.create(goalViolation);
    }

    @Override
    public void remove(GoalViolationBE entity) {
        GoalViolationBE v = this.find(entity.getId());
        if (v != null) {
            v.getGoal().addViolations(-1);
            em.remove(v);
        }
    }

    @Override
    public GoalViolationBE remove(Long id) {
        GoalViolationBE v = find(id);
        if (v != null) {
            remove(v);
        }
        return v;
    }

    public List<GoalViolationBE> findAll() {
        return em.createNamedQuery(GoalViolationBE.Q_ALL).getResultList();
    }

    public List<GoalViolationBE> findAllViolationsForGoal(Long goalId) {
        return em.createNamedQuery(GoalViolationBE.Q_BY_GOAL_ID)
                .setParameter("id", goalId)
                .getResultList();
    }
}
