package io.jlu.jerbot.commands;

import io.jlu.jerbot.utils.JerBotUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RoastCommand implements Command {

    public RoastCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();
        User author = event.getAuthor();

        String target = contentRaw.substring("roast ".length() + 1);
        Member match = JerBotUtils.getFirstMatchingMember(target, event);

        if (match != null) {
            channel.sendMessage(author.getName() + " -insert msg- " + match.getEffectiveName()).queue();
        } else {
            channel.sendMessage("No one found").queue();
        }
    }

}
