package org.easy.scrum.service;

import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.easy.scrum.model.ConfigBE;

@Stateless
@TransactionAttribute
public class ConfigBF extends AbstractFacade<ConfigBE, String> {

    @PersistenceContext
    private EntityManager em;
    
    public ConfigBF() {
        super(ConfigBE.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Returns the configuration of the given User ID,
     * this method acts as login.
     * 
     * @return the user configuration, never null
     */
    @Override
    public ConfigBE find(String id) {
        ConfigBE find = super.find(id);
        if (find == null) {
            find = new ConfigBE();
            find.setId(id);
        } else {
            // update login date
            find.setLastLogin(new Date());
        }
        return find;
    }
}
