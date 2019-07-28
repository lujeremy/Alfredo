package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {

    private static Map<String, String> commandDefinitions;

    static {
        defineCommands();
    }

    public HelpCommand() {

    }

    private static void defineCommands() {
        commandDefinitions = new HashMap<>();

        commandDefinitions.put("help", "Repeats this message");
        commandDefinitions.put("compliment", "Sends a compliment to a user. e.g. !compliment Potato");
        commandDefinitions.put("roast", "Roasts a user. e.g. !roast Potato");
        commandDefinitions.put("ahnee", "frick");
        commandDefinitions.put("hi", "Say hello!");
        commandDefinitions.put("record", "Record your workout. Format is 'workout sets reps weight,' e.g. Bench press 4 5 100");
        commandDefinitions.put("show", "Shows all of the workouts you've recorded thus far");
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        EmbedBuilder helpEmbed = new EmbedBuilder();

        //TODO: This stuff needs to ideally go into some config file somewhere
        helpEmbed.setTitle("Alfredo's Abilities");
        helpEmbed.setThumbnail(event.getAuthor().getAvatarUrl());
        helpEmbed.setColor(new Color(255, 105, 18));

        commandDefinitions
                .forEach((name, definition) -> helpEmbed.addField("!" + name + ":", definition, false));

        event.getChannel().sendMessage(helpEmbed.build()).queue();
    }
}
