package io.jlu.jerbot.utils;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

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
}
