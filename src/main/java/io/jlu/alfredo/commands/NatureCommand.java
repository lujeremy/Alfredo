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

    private Map<String, MessageEmbed> natureCache = new HashMap<>();
    private final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        MessageEmbed embed = natureCache.get("");
        if (embed == null) {
            try {
                JSONArray natureArray = Unirest.get("https://pokeapi.co/api/v2/nature/")
                        .asJson().getBody().getObject().getJSONArray("results");

                String[] myArray = new String[natureArray.length()];
                for (int i = 0; i < natureArray.length(); i++) {
                    myArray[i] = natureArray.getJSONObject(i).getString("name");
                }

                String allNatures = Arrays.stream(myArray)
                        .map(AlfredoUtils::capitalizeFirst)
                        .sorted()
                        .collect(Collectors.joining(", "));
                EmbedBuilder builder = new EmbedBuilder().addField("List of Natures:", allNatures, false);

                natureCache.put("", builder.build());
                event.getChannel().sendMessage( builder.build()).queue();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Cache used!");
            event.getChannel().sendMessage(embed).queue();
        }
    }

    private void describeTargetNature(MessageReceivedEvent event, String contentRaw) {
        String targetNature = contentRaw.split(" ")[1];

        MessageEmbed embed = natureCache.get(targetNature);
        if (embed == null) {
            Unirest.get("https://pokeapi.co/api/v2/nature/{targetNature}")
                    .routeParam("targetNature", targetNature)
                    .asJson()
                    .ifSuccess(response -> processSuccess(event, response))
                    .ifFailure(response -> {
                        LOG.error("Oh No! Status " + response.getStatus());
                        event.getChannel().sendMessage("Oh No! Status " + response.getStatus() + ". Please check your spelling").queue();
                        response.getParsingError().ifPresent(e -> {
                            LOG.error("Parsing Exception: ", e);
                            LOG.error("Original body: " + e.getOriginalBody());
                        });
                    });
        } else {
            LOG.info("Cache used!");
            event.getChannel().sendMessage(embed).queue();
        }
    }

    private void processSuccess(MessageReceivedEvent event, HttpResponse<JsonNode> jsonResponse) {
        JSONObject jsonObject = jsonResponse.getBody().getObject();

        // Retrieve the two stats we care about-- increase/decreased stat
        String name = AlfredoUtils.capitalizeFirst(jsonObject.getString("name"));
        String increased = jsonObject.getJSONObject("increased_stat").getString("name");
        String decreased = jsonObject.getJSONObject("decreased_stat").getString("name");
        String japaneseName = jsonObject.getJSONArray("names").getJSONObject(0).getString("name");

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(name)
                .addField("Japanese Name: ", japaneseName, false)
                .addField("Increased Stat: ", increased, true)
                .addField("Decreased Stat: ", decreased, true);

        natureCache.put(name.toLowerCase(), builder.build());
        event.getChannel().sendMessage(builder.build()).queue();
    }
}
