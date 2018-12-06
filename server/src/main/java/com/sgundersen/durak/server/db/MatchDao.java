package com.sgundersen.durak.server.db;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        query.orderBy(builder.desc(root.get("createdAt")));
        return entityManager.createQuery(query).setMaxResults(100).getResultList();
    }

    public MatchEntity find(long id) {
        return entityManager.find(MatchEntity.class, id);
    }

    public void save(MatchEntity match) {
        if (match.getId() == 0) {
            entityManager.persist(match);
        } else {
            entityManager.merge(match);
        }
    }

}
