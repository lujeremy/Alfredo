package io.jlu.jerbot.commands;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
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
        User author = event.getAuthor();

        String info = contentRaw.substring("roast ".length() + 1);
        String[] parsedInfo = info.split(" ");

        if (parsedInfo.length == 0) {
            return;
        }

        String workoutName = "";
        int reps = 0;
        int weight = 0;

        for (int i = 0; i < parsedInfo.length; i++) {

            try {
                int num = Integer.parseInt(parsedInfo[i]);
                if (num <= 14) {
                    reps = num;
                } else {
                    weight = num;
                }
            } catch (NumberFormatException e) {
                workoutName += parsedInfo[i];
            }
        }

        final String workoutNameCopy = workoutName;
        final int repsCopy = reps;
        final int weightCopy = weight;
        final long time = System.currentTimeMillis();

        channel.sendMessage("Recorded Workout: " + workoutName + ", " + "Reps: " + reps + ", Weight: " + weight + ".").queue();

        this.jdbi.useHandle(handle -> {
            handle.execute("create table if not exists Workouts (ID int NOT NULL AUTO_INCREMENT primary key, Time long, Workout varchar(100), Reps int, Weight int)");
            handle.execute("insert into Workouts (Time, Workout, Reps, Weight) values (?, ?, ?, ?)", time, workoutNameCopy, repsCopy, weightCopy);
        });
    }
}
