package org.easy.scrum.service;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.easy.scrum.model.SprintBE;

@Stateless
@TransactionAttribute
public class SprintBF extends AbstractFacade<SprintBE, Long> {

    @PersistenceContext
    private EntityManager em;
    
    public SprintBF() {
        super(SprintBE.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SprintBE findMostRecentSprint(Long teamId, boolean fetchDays) {
        Query q = findByTeamIdOrderByEndDate(teamId);
        q.setMaxResults(1); // only the most recent one
        List<SprintBE> resultList = q.getResultList();

        if (resultList.isEmpty()) {
            return null;
        } else {
            SprintBE result = resultList.get(0);
            // no fetch as otherwise we get a, WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
            //if (fetchDays) sprint.fetch(SprintBE_.days);
            if (fetchDays) {
                // manual fetch, which avoids loading all sprint and days into the memory
                result.getDays().size(); 
            }
            return result;
        }
    }

    public List<SprintBE> findSprintByTeamId(Long id) {
        return findByTeamIdOrderByEndDate(id).getResultList();
    }
    
    private Query findByTeamIdOrderByEndDate(Long id) {

        return em.createNamedQuery(SprintBE.Q_BY_TEAM_ID)
                .setParameter("teamId", id);
    }
}
