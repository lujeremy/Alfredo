package io.jlu.alfredo.Datatypes;

public class Workout {
    private final Long id;
    private final Long time;
    private final String workout;
    private final int sets;
    private final int reps;
    private final int weight;

    public Workout(Long id, Long time, String workout, int sets, int reps, int weight) {
        this.id = id;
        this.time = time;
        this.workout = workout;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // Format: Workout: $workout, Sets: $sets, Reps: $reps, Weight: $weight
    public String toString() {
        return "Workout: " + this.workout + ", " +
                "Sets: " + this.sets + ", " +
                "Reps: " + this.reps + ", " +
                "Weight: " + this.weight;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
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
