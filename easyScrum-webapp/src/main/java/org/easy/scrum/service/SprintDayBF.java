package org.easy.scrum.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.easy.scrum.model.*;
import org.easy.scrum.model.SprintDayBE;

@Stateless
@TransactionAttribute
public class SprintDayBF extends AbstractFacade<SprintDayBE, Long> {

    @PersistenceContext
    private EntityManager em;
    
    public SprintDayBF() {
        super(SprintDayBE.class);
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SprintDayBE> findAllBySprintId(Long id) {
        return em.createNamedQuery(SprintDayBE.Q_BY_SPRINT_ID)
                .setParameter("sprintId", id).getResultList();
    }
}
