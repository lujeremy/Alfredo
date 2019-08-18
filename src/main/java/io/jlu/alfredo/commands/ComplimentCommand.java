package io.jlu.alfredo.commands;

import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

public class ComplimentCommand implements Command {

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {

        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        try {
            JSONObject jsonObj = Unirest.get("https://complimentr.com/api").asJson().getBody().getObject();

            String phrase = jsonObj.getString("compliment");

            if (contentRaw.length() < "compliment ".length() + 2) {
                channel.sendMessage("You can't compliment air!").queue();
                return;
            }

            String target = contentRaw.substring("compliment ".length() + 1);
            Member match = AlfredoUtils.getFirstMatchingMember(target, event);

            if (match != null) {
                channel.sendMessage(match.getEffectiveName() + ", " + phrase.toLowerCase()).queue();
            } else {
                channel.sendMessage("No one found").queue();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
