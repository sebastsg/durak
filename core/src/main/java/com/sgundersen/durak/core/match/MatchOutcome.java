package com.sgundersen.durak.core.match;

public enum MatchOutcome {

    // Match is still ongoing
    NotYetDecided,

    // Relative to client.
    Victory,
    Defeat,

    // When the defender has no cards left after defeating the attacking cards.
    Draw

}
