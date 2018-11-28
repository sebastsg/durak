package com.sgundersen.durak.server.db;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PlayerProfileDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PlayerProfile> getAll() {
        return entityManager.createNamedQuery("PlayerProfile.getAll", PlayerProfile.class).getResultList();
    }

    public List<PlayerProfile> getTop(int limit) {
        return entityManager.createNamedQuery("PlayerProfile.getTop", PlayerProfile.class).setMaxResults(limit).getResultList();
    }

    public PlayerProfile find(String googleAccountId) {
        Query query = entityManager.createNamedQuery("PlayerProfile.findOne", PlayerProfile.class);
        query.setParameter("id", googleAccountId);
        List results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return (PlayerProfile) results.get(0);
    }

    public void save(PlayerProfile profile) {
        entityManager.persist(profile);
    }

    public void update(PlayerProfile profile) {
        entityManager.merge(profile);
    }

    public void remove(PlayerProfile profile) {
        entityManager.remove(profile);
    }

}
