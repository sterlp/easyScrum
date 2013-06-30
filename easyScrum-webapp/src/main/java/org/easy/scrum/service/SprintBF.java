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
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintBE_;
import org.easy.scrum.model.TeamBE_;

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
        CriteriaQuery<SprintBE> cq = findByTeamIdOrderByEndDate(teamId);
        TypedQuery<SprintBE> createQuery = em.createQuery(cq);
        createQuery.setMaxResults(1);
        List<SprintBE> resultList = createQuery.getResultList();

        if (resultList.isEmpty()) {
            return null;
        } else {
            SprintBE result = resultList.get(0);
            if (fetchDays) {
                // manual fetch, which avoids loading all sprint and days into the memory
                result.getDays().size(); 
            }
            return result;
        }
    }

    public List<SprintBE> findSprintByTeamId(Long id) {
        TypedQuery<SprintBE> createQuery = em.createQuery(findByTeamIdOrderByEndDate(id));
        return createQuery.getResultList();
    }
    
    private CriteriaQuery<SprintBE> findByTeamIdOrderByEndDate(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SprintBE> cq = cb.createQuery(SprintBE.class);
        Root sprint = cq.from(SprintBE.class);
        
        // no fetch as otherwise we get a, WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
        //if (fetchDays) sprint.fetch(SprintBE_.days);
        
        Path<Long> sprintTeamId = sprint.get(SprintBE_.team).get(TeamBE_.id);
        
        cq.select(sprint).where(
            cb.equal(sprintTeamId, id)
        ).orderBy(cb.desc(sprint.get(SprintBE_.end)));
        return cq;
    }
}
