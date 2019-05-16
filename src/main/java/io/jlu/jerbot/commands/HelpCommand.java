package io.jlu.jerbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {

    private static Map<String, String> commandDefinitions;

    static {
        defineCommands();
    }

    public HelpCommand() {}

    private static void defineCommands() {
        commandDefinitions = new HashMap<>();

        commandDefinitions.put("help", "Repeats this message");
        commandDefinitions.put("compliment", "Sends a compliment to a user. e.g. !compliment Potato");
        commandDefinitions.put("roast", "Roasts a user. e.g. !roast Potato");
        commandDefinitions.put("ahnee", "frick");
        commandDefinitions.put("hi", "Say hello!");
        commandDefinitions.put("record", "Record your workout. Format is 'workout reps weight,' e.g. Bench press 5 100");
        commandDefinitions.put("show", "Shows all of the workouts you've recorded thus far");
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        StringBuffer message = new StringBuffer("```");
        for (String name : commandDefinitions.keySet()) {
            String definition = commandDefinitions.get(name);
            message.append("\n!" + name + ": " + definition);
        }
        event.getChannel().sendMessage(message.append("```").toString()).queue();
    }
}
