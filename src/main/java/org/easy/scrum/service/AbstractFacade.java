package org.easy.scrum.service;

import java.util.List;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public T find(IdType id) {
        return getEntityManager().find(entityClass, id);
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
