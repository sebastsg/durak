package com.sgundersen.durak.server.db;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class RecordedMatchDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RecordedMatch> getAllMeta() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecordedMatch> query = builder.createQuery(RecordedMatch.class);
        Root<RecordedMatch> root = query.from(RecordedMatch.class);
        query.multiselect(root.get("id"), root.get("createdAt"), root.get("name"));
        return entityManager.createQuery(query).getResultList();
    }

    public RecordedMatch find(long id) {
        Query query = entityManager.createNamedQuery("RecordedMatch.find", RecordedMatch.class);
        query.setParameter("id", id);
        List results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return (RecordedMatch) results.get(0);
    }

    public void save(RecordedMatch match) {
        entityManager.persist(match);
    }

    public void update(RecordedMatch match) {
        entityManager.merge(match);
    }

    public void remove(RecordedMatch match) {
        entityManager.remove(match);
    }

}
