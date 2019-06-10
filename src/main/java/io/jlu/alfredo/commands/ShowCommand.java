package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ShowCommand implements Command {

    private final Jdbi jdbi;

    public ShowCommand(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();

        try {
            List<String> workouts = this.jdbi.withHandle(handle ->
                    handle.createQuery("SELECT Workout FROM Workouts").mapTo(String.class).list());

            StringBuffer workoutsMessage = new StringBuffer();
            for (int i = 0; i < workouts.size(); i++) {
                workoutsMessage.append("Recorded Workout: " + workouts.get(i) + "\n");
            }

            channel.sendMessage(workoutsMessage).queue();
        } catch (Exception e) {
            channel.sendMessage("Something went wrong but it isn't your fault!").queue();
            e.printStackTrace();
        }
    }
}
