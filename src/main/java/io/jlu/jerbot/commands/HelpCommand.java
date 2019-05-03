package io.jlu.jerbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {

    static Map<String, Command> commandMap;

    public HelpCommand(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        Map<String, String> commandDefintions = defineCommands();

        StringBuffer message = new StringBuffer("```");
        for (String name : commandDefintions.keySet()) {
            String definition = commandDefintions.get(name);
            message.append("\n!" + name + ": " + definition);
        }
        event.getChannel().sendMessage(message.append("```").toString()).queue();
    }

    public static Map<String, String> defineCommands() {
        Map<String, String> commandDefinitions = new HashMap<>();

        for (String name : commandMap.keySet()) {
            String definition = "";
            switch(name) {
                case "help":
                    definition = "Repeats this message";
                    break;
                case "compliment":
                    definition = "Sends a compliment to a user. e.g. !compliment Potato";
                    break;
                case "roast":
                    definition = "Roasts a user. e.g. !roast Potato";
                    break;
                case "ahnee":
                    definition = "frick";
                    break;
                case "hi":
                    definition = "Say hello!";
                    break;
                case "record":
                    definition = "Record your workout. Format is 'workout reps weight,' e.g. Bench press 5 100";
                    break;
                case "show":
                    definition = "Shows all of the workouts you've recorded thus far";
                    break;
            }
//            System.out.println("I just added: " + name + definition);
            commandDefinitions.put(name, definition);
        }

        return commandDefinitions;
    }
}
