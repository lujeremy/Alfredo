package io.jlu.alfredo.utils;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AlfredoUtils {

    /**
     * Gets the first matching member of in a server
     * @param target name String to match members list against
     * @param event event trigger
     * @return a member that matches the String, null otherwise
     */
    public static Member getFirstMatchingMember(String target, MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            Member matchingMember = event.getGuild().getMembers()
                    .stream()
                    .filter((member) -> target.equalsIgnoreCase(member.getUser().getName()) || target.equalsIgnoreCase(member.getNickname()))
                    .findFirst()
                    .orElse(null);
            return matchingMember;
        }

        return null;
    }
}
