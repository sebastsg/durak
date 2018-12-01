package com.sgundersen.durak.server.db;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class MatchDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MatchEntity> getAllMeta() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MatchEntity> query = builder.createQuery(MatchEntity.class);
        Root<MatchEntity> root = query.from(MatchEntity.class);
        query.multiselect(root.get("id"), root.get("createdAt"), root.get("name"));
        return entityManager.createQuery(query).getResultList();
    }

    public MatchEntity find(long id) {
        TypedQuery<MatchEntity> query = entityManager.createNamedQuery("match.findById", MatchEntity.class);
        query.setParameter("id", id);
        List<MatchEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public void save(MatchEntity match) {
        if (match.getId() == 0) {
            entityManager.persist(match);
        } else {
            entityManager.merge(match);
        }
    }

}
