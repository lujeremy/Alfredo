package io.jlu.jerbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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

        try {
            HttpResponse<String> jsonResponse = Unirest.get("https://insult.mattbas.org/api/insult").asString();
            String phrase = jsonResponse.getBody();
//            System.out.println(phrase);

            if (contentRaw.length() < "roast ".length() + 2) {
                channel.sendMessage("You can't roast air!").queue();
                return;
            }

            String target = contentRaw.substring("roast ".length() + 1);
            Member match = JerBotUtils.getFirstMatchingMember(target, event);

            if (match != null) {
                channel.sendMessage(match.getEffectiveName() + ", " + phrase.toLowerCase()).queue();
            } else {
                channel.sendMessage("No one found").queue();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            return;
        }
    }
}
