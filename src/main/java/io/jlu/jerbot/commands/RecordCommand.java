package io.jlu.jerbot.commands;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jdbi.v3.core.Jdbi;

public class RecordCommand implements Command {

    final private Jdbi jdbi;

    public RecordCommand(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        String info = contentRaw.substring("roast ".length() + 1);
        String[] parsedInfo = info.split(" ");

        if (parsedInfo.length == 0) {
            return;
        }

        String workoutName = "";
        int sets = 0;
        int reps = 0;
        int weight = 0;

        for (int i = 0; i < parsedInfo.length; i++) {
            try {
                int num = Integer.parseInt(parsedInfo[i]);
                // First num you see will be sets, then reps, then weight. Any more will be silently ignored
                if (sets == 0) {
                    sets = num;
                } else if (reps == 0) {
                    reps = num;
                } else if (weight == 0){
                    weight = num;
                }
            } catch (NumberFormatException e) {
                workoutName += parsedInfo[i];
            }
        }

        workoutName = workoutName.toLowerCase();
        final String workoutNameCopy = workoutName;
        final int setsCopy = sets;
        final int repsCopy = reps;
        final int weightCopy = weight;
        final long time = System.currentTimeMillis();

        try {
            this.jdbi.useHandle(handle -> {
                handle.execute("create table if not exists Workouts (ID int NOT NULL AUTO_INCREMENT primary key, Time long, Workout varchar(100), Sets int, Reps int, Weight int)");
                handle.execute("insert into Workouts (Time, Workout, Reps, Weight) values (?, ?, ?, ?)", time, workoutNameCopy, setsCopy, repsCopy, weightCopy);
            });

            channel.sendMessage("Recorded Workout: " + workoutName + ", Sets: " + sets + ", Reps: " + reps + ", Weight: " + weight + ".").queue();
        } catch (Exception e) {
            channel.sendMessage("That didn't quite hit the spot, something went wrong with the database.").queue();
            e.printStackTrace();
        }

    }
}
