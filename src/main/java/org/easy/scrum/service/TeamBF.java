package org.easy.scrum.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.easy.scrum.model.TeamBE;

@Stateless
@TransactionAttribute
public class TeamBF extends AbstractFacade<TeamBE> {

    @PersistenceContext
    private EntityManager em;
    
    public TeamBF() {
        super(TeamBE.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
