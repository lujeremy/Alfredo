package io.jlu.alfredo.datatypes;

import java.time.Instant;

public class Workout {
    private final long id;
    private final Instant time;
    private final String workout;
    private final int sets;
    private final int reps;
    private final int weight;

    public Workout(long id, long milli, String workout, int sets, int reps, int weight) {
        this.id = id;
        this.time = Instant.ofEpochMilli(milli);
        this.workout = workout;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // Format: Workout: $workout, Time: $time, Sets: $sets, Reps: $reps, Weight: $weight
    @Override
    public String toString() {
        return "Workout: " + this.workout + ", " +
                "Time: " + this.time + ", " +
                "Sets: " + this.sets + ", " +
                "Reps: " + this.reps + ", " +
                "Weight: " + this.weight;
    }

    // Format: Sets: $sets, Reps: $reps, Weight: $weight
    public String getDetailString() {
        return "Sets: " + this.sets + ", " +
                "Reps: " + this.reps + ", " +
                "Weight: " + this.weight;
    }

    public Long getId() {
        return id;
    }

    public Instant getTime() {
        return time;
    }

    public String getWorkout() {
        return workout;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }
}
