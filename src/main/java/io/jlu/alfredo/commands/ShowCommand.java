package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jdbi.v3.core.Jdbi;

import java.awt.*;
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

            EmbedBuilder workoutsEmbed = new EmbedBuilder();
            Member umaru = event.getGuild().getMemberById(482570873002262538L);

            workoutsEmbed.setTitle("Recorded Workouts <:TohruPoint:507804401436459008>");
            workoutsEmbed.setThumbnail(event.getAuthor().getAvatarUrl());
            workoutsEmbed.setAuthor(umaru.getNickname(), "",umaru.getUser().getAvatarUrl());
            workoutsEmbed.setColor(new Color(126, 249, 255));
            if (workouts.size() == 0) workoutsEmbed.setTitle("Something went wrong");

            for (int i = 0; i < workouts.size(); i++) {
              workoutsEmbed.addField("", workouts.get(i), false);
            }

            channel.sendMessage(workoutsEmbed.build()).queue();
        } catch (Exception e) {
            channel.sendMessage("Something went wrong but it isn't your fault!").queue();
            e.printStackTrace();
        }
    }
}
