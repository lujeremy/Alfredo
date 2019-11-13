package io.jlu.alfredo.commands;

import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class ComplimentCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        String target = contentRaw.substring("compliment ".length() + 1);
        Member match = AlfredoUtils.getFirstMatchingMember(target, event);
        String message;

        if (contentRaw.length() < "compliment ".length() + 2 || match == null) {
            message = "No one found";
        } else {
            message = makeCompliment(match);
        }

        channel.sendMessage(message).queue();
    }

    String makeCompliment(Member member) {
        try {
            JSONObject jsonObj = Unirest.get("https://complimentr.com/api").asJson().getBody().getObject();
            String phrase = jsonObj.getString("compliment");
            return member.getEffectiveName() + ", " + phrase.toLowerCase();
        } catch (UnirestException e) {
            LOGGER.error(e.getMessage());
            return "An upstream error has occurred";
        }
    }

}
