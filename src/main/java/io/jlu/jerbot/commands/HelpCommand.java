package io.jlu.jerbot.commands;

import io.jlu.jerbot.utils.JerBotUtils;
import net.dv8tion.jda.core.entities.MessageChannel;
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
        Map<String, String> commandDefintions = JerBotUtils.defineCommands(commandMap);
        StringBuffer message = new StringBuffer("```");
        for (String name : commandDefintions.keySet()) {
            String definition = commandDefintions.get(name);
            message.append("\n!" + name + ": " + definition);
        }

        event.getChannel().sendMessage(message.append("```").toString()).queue();
    }
}
