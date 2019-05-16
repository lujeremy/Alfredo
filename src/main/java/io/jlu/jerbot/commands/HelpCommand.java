package io.jlu.jerbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {

    Map<String, String> commandDefinitions;

    public HelpCommand() {
        this.commandDefinitions = defineCommands();
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

    public Map<String, String> defineCommands() {
        Map<String, String> mapDefinitions = new HashMap<>();

        mapDefinitions.put("help", "Repeats this message");
        mapDefinitions.put("compliment", "Sends a compliment to a user. e.g. !compliment Potato");
        mapDefinitions.put("roast", "Roasts a user. e.g. !roast Potato");
        mapDefinitions.put("ahnee", "frick");
        mapDefinitions.put("hi", "Say hello!");
        mapDefinitions.put("record", "Record your workout. Format is 'workout reps weight,' e.g. Bench press 5 100");
        mapDefinitions.put("show", "Shows all of the workouts you've recorded thus far");

        return mapDefinitions;
    }
}
