package com.sgundersen.durak.server.db;

import com.sgundersen.durak.core.net.match.MatchClientState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
        @NamedQuery(name = "RecordedMatch.find", query = "select m from RecordedMatch m where m.id = :id")
})
public class RecordedMatch {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Snapshot {
        private List<MatchClientState> clientStates = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SnapshotList {
        private List<Snapshot> snapshots = new ArrayList<>();
    }

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Id
    private long id;

    private Instant createdAt;
    private String name;

    @Lob
    private String snapshots;

    public RecordedMatch(long id, Instant createdAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        snapshots = "";
    }

    @PrePersist
    protected void onBeingCreated() {
        createdAt = Instant.now();
    }

    private SnapshotList convertedSnapshots() {
        if (snapshots == null || snapshots.isEmpty()) {
            return new SnapshotList();
        }
        return jsonb.fromJson(snapshots, SnapshotList.class);
    }

    public void addSnapshot(List<MatchClientState> snapshot) {
        SnapshotList snapshotList = convertedSnapshots();
        snapshotList.getSnapshots().add(new Snapshot(snapshot));
        snapshots = jsonb.toJson(snapshotList);
    }

}
