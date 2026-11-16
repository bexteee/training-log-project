package edu.augustana.traininglog.model;

// small record that identifies one best time, the key
// for the map: athlete, stroke, distance, kind. BestTimes will be the lookup that holds the map.
public record BestKey(String athleteId, StrokeType stroke, double distance, BestKind kind) { }
