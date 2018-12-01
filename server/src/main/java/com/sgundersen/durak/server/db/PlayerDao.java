package com.sgundersen.durak.server.db;

import com.sgundersen.durak.core.net.player.LoginAttempt;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PlayerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PlayerEntity> getTop(int limit) {
        TypedQuery<PlayerEntity> query = entityManager.createNamedQuery("player.getTop", PlayerEntity.class);
        return query.setMaxResults(limit).getResultList();
    }

    private PlayerEntity findBy(String queryName, Object value) {
        TypedQuery<PlayerEntity> query = entityManager.createNamedQuery(queryName, PlayerEntity.class);
        query.setParameter("id", value);
        List<PlayerEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public PlayerEntity findByGoogleId(String googleAccountId) {
        return findBy("player.findByGoogleId", googleAccountId);
    }

    public PlayerEntity findById(long id) {
        return findBy("player.findById", id);
    }

    public void save(PlayerEntity player) {
        if (player.getId() == 0) {
            entityManager.persist(player);
        } else {
            entityManager.merge(player);
        }
    }

    public PlayerEntity createIfNew(LoginAttempt loginAttempt) {
        PlayerEntity player = findByGoogleId(loginAttempt.getAccountId());
        if (player != null) {
            return player;
        }
        player = new PlayerEntity();
        player.setGoogleAccountId(loginAttempt.getAccountId());
        player.setDisplayName(loginAttempt.getName());
        save(player);
        return player;
    }

}
