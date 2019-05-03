package io.jlu.jerbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jlu.jerbot.utils.JerBotUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class ComplimentCommand implements Command {

    public ComplimentCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {

        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        try {
            HttpResponse<String> jsonResponse = Unirest.get("https://complimentr.com/api").asString();
            String json = jsonResponse.getBody();
            JSONObject jsonObj = new JSONObject(json);
            String phrase = jsonObj.getString("compliment");
//            System.out.println(phrase);

            if (contentRaw.length() < "compliment ".length() + 2) {
                channel.sendMessage("You can't compliment air!").queue();
                return;
            }

            String target = contentRaw.substring("compliment ".length() + 1);
            Member match = JerBotUtils.getFirstMatchingMember(target, event);

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
