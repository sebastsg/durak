package com.sgundersen.durak.server.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "PlayerProfile.find", query = "select p from PlayerProfile p where p.googleAccountId = :id"),
        @NamedQuery(name = "PlayerProfile.getAll", query = "select p from PlayerProfile p order by p.createdAt desc"),
        @NamedQuery(name = "PlayerProfile.getTop", query = "select p from PlayerProfile p order by p.ratio desc")
})
public class PlayerProfile {

    @Id
    private String googleAccountId;

    private Instant createdAt;

    private String displayName;
    private int victories;
    private int defeats;
    private float ratio;

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

    public void addVictory() {
        victories++;
        updateRatio();
    }

    public void addDefeat() {
        defeats++;
        updateRatio();
    }

}
