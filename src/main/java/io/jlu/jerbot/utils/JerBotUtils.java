package io.jlu.jerbot.utils;

import io.jlu.jerbot.commands.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JerBotUtils {

    public static List<Member> getMatchingMembers(String target, MessageReceivedEvent event) {
        List<Member> memberList = new ArrayList<>();

        if (event.getGuild() != null) {
            memberList = event.getGuild().getMembersByName(target, true);

            if (memberList.isEmpty()) {
                memberList = event.getGuild().getMembersByNickname(target,true);
            }
        }

        return memberList;
    }

    // Get first matching members method goes here
    public static Member getFirstMatchingMember(String target, MessageReceivedEvent event) {
        List<Member> memberList = getMatchingMembers(target, event);
        if (!memberList.isEmpty()) {
            return memberList.get(0);
        }

        return null;
    }

    public static Map<String, String> defineCommands(Map<String, Command> commandMap) {
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
                case "show":

                default:
            }
            System.out.println("I just added: " + name + definition);
            commandDefinitions.put(name, definition);
        }

        return commandDefinitions;
    }
}
