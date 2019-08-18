package io.jlu.alfredo.commands;

import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NatureCommand implements Command {

    private Map<String, JSONObject> natureCache = new HashMap<>();
    private final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        String contentRaw = event.getMessage().getContentRaw().toLowerCase();

        if (contentRaw.length() < "nature ".length() + 2) {
            listAllNatures(event);
        } else {
            describeTargetNature(event, contentRaw);
        }
    }

    private void listAllNatures(MessageReceivedEvent event) {
        JSONObject responseObject = natureCache.get("");

        // If not in cache, try to fetch from api and insert into cache
        if (responseObject == null) {
            try {
                responseObject = Unirest.get("https://pokeapi.co/api/v2/nature/").asJson().getBody().getObject();
                natureCache.put("", responseObject);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Cache used!");
        }

        // If fetching is successful, process and build embed. If it failed, do nothing.
        if (responseObject != null) {
            JSONArray natureArray = responseObject.getJSONArray("results");

            String[] myArray = new String[natureArray.length()];
            for (int i = 0; i < natureArray.length(); i++) {
                myArray[i] = natureArray.getJSONObject(i).getString("name");
            }

            String allNatures = Arrays.stream(myArray)
                    .map(AlfredoUtils::capitalizeFirst)
                    .sorted()
                    .collect(Collectors.joining(", "));
            EmbedBuilder builder = new EmbedBuilder().addField("List of Natures:", allNatures, false);
            event.getChannel().sendMessage(builder.build()).queue();
        }

    }

    private void describeTargetNature(MessageReceivedEvent event, String contentRaw) {
        String targetNature = contentRaw.split(" ")[1];

        JSONObject responseObject = natureCache.get(targetNature);

        if (responseObject == null) {
            try {
                responseObject =  Unirest.get("https://pokeapi.co/api/v2/nature/{targetNature}")
                        .routeParam("targetNature", targetNature)
                        .asJson().getBody().getObject();
                natureCache.put(targetNature, responseObject);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Cache used!");
        }

        if (responseObject != null) {
            // Retrieve the two stats we care about-- increase/decreased stat
            String name = AlfredoUtils.capitalizeFirst(responseObject.getString("name"));
            String increased = responseObject.getJSONObject("increased_stat").getString("name");
            String decreased = responseObject.getJSONObject("decreased_stat").getString("name");
            String japaneseName = responseObject.getJSONArray("names").getJSONObject(0).getString("name");

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(name)
                    .addField("Japanese Name: ", japaneseName, false)
                    .addField("Increased Stat: ", increased, true)
                    .addField("Decreased Stat: ", decreased, true);

            event.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
