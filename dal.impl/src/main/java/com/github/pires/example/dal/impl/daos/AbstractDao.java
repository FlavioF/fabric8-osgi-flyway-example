package com.github.pires.example.dal.impl.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

/**
 * Abstract data access object.
 * 
 * @param <T>
 *          the entity type to be managed.
 */
public class AbstractDao<T> {

  protected Class<T> entityClass;
  private EntityManager em;

  public AbstractDao(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public EntityManager getEntityManager() {
    return this.em;
  }

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  /**
   * Retrieves the meta-model for a certain entity.
   * 
   * @return the meta-model of a certain entity.
   */
  protected EntityType<T> getMetaModel() {
    return getEntityManager().getMetamodel().entity(entityClass);
  }

  public void persist(T entity) {
    getEntityManager().persist(entity);
  }

  public T merge(T entity) {
    return getEntityManager().merge(entity);
  }

  public void remove(Object entityId) {
    T entity = find(entityId);
    if (entity != null)
      getEntityManager().remove(entity);
  }

  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public List<T> findAll() {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(
        entityClass);
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  public int count() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<T> root = cq.from(entityClass);
    cq.select(cb.count(root));
    return getEntityManager().createQuery(cq).getSingleResult().intValue();
  }

}