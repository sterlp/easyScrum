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
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.GoalBE_;
import org.easy.scrum.model.embedded.PersistentPeriod_;

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

    @Override
    public List<GoalBE> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GoalBE> cq = cb.createQuery(GoalBE.class);
        Root goal = cq.from(GoalBE.class);
        
        cq.select(goal).orderBy(
            cb.desc(goal.get(GoalBE_.period).get(PersistentPeriod_.end))
        );
        
        return em.createQuery(cq).getResultList();
    }
}
