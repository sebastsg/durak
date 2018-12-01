package com.sgundersen.durak.server.db;

import com.sgundersen.durak.core.net.match.FinishedMatch;
import com.sgundersen.durak.core.net.match.MatchSnapshot;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity(name = "match")
@NamedQueries({
        @NamedQuery(name = "match.findById", query = "select m from match m where m.id = :id")
})
public class MatchEntity {

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Id
    @GeneratedValue
    private long id;

    private Date createdAt;
    private String name;

    @Lob
    private String snapshots;

    public MatchEntity(long id, Date createdAt, String name){
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
    }

    @PrePersist
    protected void onBeingCreated() {
        createdAt = Date.from(Instant.now());
    }

    private FinishedMatch getFinishedMatch() {
        FinishedMatch finishedMatch = new FinishedMatch();
        if (snapshots != null && !snapshots.isEmpty()) {
            return jsonb.fromJson(snapshots, FinishedMatch.class);
        }
        return finishedMatch;
    }

    public void addSnapshot(MatchSnapshot snapshot) {
        FinishedMatch finishedMatch = getFinishedMatch();
        finishedMatch.getSnapshots().add(snapshot);
        snapshots = jsonb.toJson(finishedMatch);
    }

}
