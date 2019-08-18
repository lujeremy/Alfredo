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
        commandDefinitions.put("pokemon", "Generates a random pokemon, or a pokemon of your choice." +
                " e.g. !pokemon; !pokemon clefable");
        commandDefinitions.put("nature", "Shows a list of natures or how a given nature affects a Pokemon's stat growth." +
                " e.g. !nature; !nature bold");
        commandDefinitions.put("record", "Record your workout. Format is 'workout sets reps weight,'" +
                " e.g. Bench press 4 5 100");
        commandDefinitions.put("show", "Shows all of the workouts you've recorded thus far");
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        EmbedBuilder helpEmbed = new EmbedBuilder()
            .setTitle("Alfredo's Abilities <:TohruGun:605162460479094804>")
            .setThumbnail(event.getAuthor().getAvatarUrl())
            .setColor(new Color(255, 105, 18));

        commandDefinitions
                .forEach((name, definition) -> helpEmbed.addField("!" + name + ":", definition, false));

        event.getChannel().sendMessage(helpEmbed.build()).queue();
    }
}
