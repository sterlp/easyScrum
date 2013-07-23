package org.easy.scrum.service;

import java.util.List;
import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.validation.Valid;
import org.easy.scrum.model.IEntity;
import org.easy.util.logging.LoggingInterceptor;

@Interceptors(LoggingInterceptor.class)
public abstract class AbstractFacade<T extends IEntity<IdType>, IdType> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
    
    public T create(@Valid T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public T edit(@Valid T entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }
    
    /**
     * Removes the given object by it's ID and returns it if it was still in the DB
     * @return the deleted element or null if already deleted
     */
    public T remove(IdType id) {
        T toDelete = find(id);
        if (toDelete != null) {
            getEntityManager().remove(toDelete);
        }
        return toDelete;
    }

    /**
     * Searches the by the given ID, if the ID is not null
     * @param id the id to search for
     * @return the found element or null if not found or id = null
     */
    public T find(IdType id) {
        if (id != null) {
            return getEntityManager().find(entityClass, id);
        } else {
            return null;
        }
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    protected Query addRange(Query query, Integer startPosition, Integer maxResult) {
        if (startPosition != null) {
            query.setFirstResult(startPosition.intValue());
        }
        if (maxResult != null) {
            query.setMaxResults(maxResult.intValue());
        }
        return query;
    }
}
