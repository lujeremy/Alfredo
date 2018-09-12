package io.jlu.jerbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jlu.jerbot.utils.JerBotUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GiveTaskCommand implements Command {

    public GiveTaskCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {

        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("https://corporatebs-generator.sameerkumar.website/").asJson();
            String phrase = jsonResponse.getBody().getObject().getString("phrase");
            String target = contentRaw.substring("givetask ".length() + 1);
            Member match = JerBotUtils.getFirstMatchingMember(target, event);

            if (match != null) {
                channel.sendMessage(match.getEffectiveName() + ", please " + phrase.toLowerCase()).queue();
            } else {
                channel.sendMessage("No one found").queue();
            }
        } catch (UnirestException e) {
            return;
        }
    }
}
