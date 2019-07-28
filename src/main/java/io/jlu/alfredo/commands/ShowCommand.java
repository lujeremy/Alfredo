package io.jlu.alfredo.commands;

import io.jlu.alfredo.Datatypes.Workout;
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
            List<Workout> workouts = this.jdbi.withHandle(handle ->
                    handle.createQuery("SELECT * FROM Workouts")
                            .map((rs, ctx) -> new Workout(rs.getLong("id"), rs.getLong("time"),
                                    rs.getString("workout"), rs.getInt("sets"),
                                    rs.getInt("reps"), rs.getInt("weight")))
                            .list());

            EmbedBuilder workoutsEmbed = new EmbedBuilder();

            //TODO: This stuff needs to ideally go into some config file somewhere
            Member umaru = event.getGuild().getMemberById(482570873002262538L);
            workoutsEmbed.setTitle("Recorded Workouts <:TohruPoint:507804401436459008>");
            workoutsEmbed.setThumbnail(event.getAuthor().getAvatarUrl());
            workoutsEmbed.setAuthor(umaru.getNickname(), "", umaru.getUser().getAvatarUrl());
            workoutsEmbed.setColor(new Color(126, 249, 255));

            if (workouts.size() == 0) {
                workoutsEmbed.setTitle("Something went wrong");
            }

            workouts.forEach((workout) -> workoutsEmbed.addField("", workout.toString(), false));
            channel.sendMessage(workoutsEmbed.build()).queue();
        } catch (Exception e) {
            channel.sendMessage("Something went wrong but it isn't your fault!").queue();
            e.printStackTrace();
        }
    }
}
