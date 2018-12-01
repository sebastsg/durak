package com.sgundersen.durak.server.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity(name = "player")
@NamedQueries({
        @NamedQuery(name = "player.findById", query = "select p from player p where p.id = :id"),
        @NamedQuery(name = "player.findByGoogleId", query = "select p from player p where p.googleAccountId = :id"),
        @NamedQuery(name = "player.getTop", query = "select p from player p order by p.ratio desc")
})
public class PlayerEntity {

    @Id
    @GeneratedValue
    private long id;

    private String googleAccountId;
    private Instant createdAt;
    private String displayName;
    private int victories;
    private int defeats;
    private float ratio;
    private long matchId;

    @PrePersist
    protected void onBeingCreated() {
        createdAt = Instant.now();
        updateRatio();
    }

    @PreUpdate
    protected void onBeingUpdated() {
        updateRatio();
    }

    private void updateRatio() {
        ratio = (float) victories / Math.max(1.0f, (float) defeats);
    }

    public void onVictory() {
        matchId = 0;
        victories++;
        updateRatio();
    }

    public void onDefeat() {
        matchId = 0;
        defeats++;
        updateRatio();
    }

}
