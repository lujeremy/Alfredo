package io.jlu.alfredo.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jlu.alfredo.utils.AlfredoUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RoastCommand implements Command {

    public RoastCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        try {
            HttpResponse<String> jsonResponse = Unirest.get("https://insult.mattbas.org/api/insult").asString();
            String phrase = jsonResponse.getBody();

            if (contentRaw.length() < "roast ".length() + 2) {
                channel.sendMessage("You can't roast air!").queue();
                return;
            }

            String target = contentRaw.substring("roast ".length() + 1);
            Member match = AlfredoUtils.getFirstMatchingMember(target, event);

            if (match != null) {
                channel.sendMessage(match.getEffectiveName() + ", " + phrase.toLowerCase()).queue();
            } else {
                channel.sendMessage("No one found").queue();
            }
        } catch (UnirestException e) {
            channel.sendMessage("Something went wrong but it isn't your fault!").queue();
            e.printStackTrace();
        }
    }
}