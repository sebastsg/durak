package com.sgundersen.durak.server.db;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class RecordedMatchDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RecordedMatch> getAll() {
        return entityManager.createNamedQuery("RecordedMatch.getAll", RecordedMatch.class).getResultList();
    }

    public RecordedMatch find(long id) {
        Query query = entityManager.createNamedQuery("RecordedMatch.findOne", RecordedMatch.class);
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
