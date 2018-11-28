package com.sgundersen.durak.server.db;

import com.sgundersen.durak.core.net.match.MatchClientState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "RecordedMatch.findOne", query = "select m from RecordedMatch m where m.id = :id"),
        @NamedQuery(name = "RecordedMatch.getAll", query = "select m from RecordedMatch m order by m.createdAt desc")
})
public class RecordedMatch {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Snapshot {
        private List<MatchClientState> clientStates = new ArrayList<>();
    }

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Id
    @GeneratedValue
    private long id;

    private Instant createdAt;

    private String name;
    private String snapshotsAsJson;

    @PrePersist
    protected void onBeingCreated() {
        createdAt = Instant.now();
    }

    public List<Snapshot> getSnapshots() {
        return jsonb.fromJson(snapshotsAsJson, new ArrayList<Snapshot>(){}.getClass().getGenericSuperclass());
    }

    public void addSnapshot(List<MatchClientState> snapshot) {
        List<Snapshot> snapshots = getSnapshots();
        snapshots.add(new Snapshot(snapshot));
        snapshotsAsJson = jsonb.toJson(snapshots);
    }

}
