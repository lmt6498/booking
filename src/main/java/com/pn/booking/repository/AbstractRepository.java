package com.pn.booking.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class AbstractRepository {

  @PersistenceContext
  protected EntityManager entityManager;

  public Query query(String sql) {
    return entityManager.createQuery(sql);
  }

  public Query query(String sql, Class<?> clazz) {
    return entityManager.createQuery(sql, clazz);
  }

  public Query nativeQuery(String sql) {
    return entityManager.createNativeQuery(sql);
  }

  public Query nativeQuery(String sql, Class<?> clazz) {
    return entityManager.createNativeQuery(sql, clazz);
  }
}
