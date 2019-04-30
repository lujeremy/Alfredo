package io.jlu.jerbot.commands;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ShowCommand implements Command {

    final private Jdbi jdbi;

    public ShowCommand(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();

        List<String> workouts = this.jdbi.withHandle(handle ->
                handle.createQuery("SELECT Workout FROM Workouts").mapTo(String.class).list());

        for (int i = 0; i < workouts.size(); i++) {
            channel.sendMessage("Recorded Workout: " + workouts.get(i)).queue();
        }
    }
}
