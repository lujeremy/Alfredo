package io.jlu.alfredo.commands;

import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class NatureCommand implements Command {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        String contentRaw = event.getMessage().getContentRaw().toLowerCase();

        if (contentRaw.length() < "nature ".length() + 2) {
            try {
                JSONArray natureArray = Unirest.get("https://pokeapi.co/api/v2/nature/")
                        .asJson().getBody().getObject().getJSONArray("results");

                // TODO: This is here solely to practice streams
                String[] myArray = new String[natureArray.length()];
                for (int i = 0; i < natureArray.length(); i++) {
                    myArray[i] = natureArray.getJSONObject(i).getString("name");
                }
                String allNatures = Arrays.stream(myArray)
                        .map(AlfredoUtils::capitalizeFirst)
                        .reduce("", (current, element) -> current + element + ", ");
                EmbedBuilder builder = new EmbedBuilder().addField("List of Natures:", allNatures, false);

                event.getChannel().sendMessage(builder.build()).queue();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return;
        }

        String targetNature = contentRaw.split(" ")[1];

        Unirest.get("https://pokeapi.co/api/v2/nature/{targetNature}")
                .routeParam("targetNature", targetNature)
                .asJson()
                .ifSuccess(response -> processSuccess(event, response))
                .ifFailure(response -> {
                    logger.error("Oh No! Status " + response.getStatus());
                    event.getChannel().sendMessage("Oh No! Status " + response.getStatus() + ". Please check your spelling").queue();
                    response.getParsingError().ifPresent(e -> {
                        logger.error("Parsing Exception: ", e);
                        logger.error("Original body: " + e.getOriginalBody());
                    });
                });
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

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
