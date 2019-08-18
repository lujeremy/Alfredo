package io.jlu.alfredo.commands;

import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class PokemonCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        String contentRaw = event.getMessage().getContentRaw().toLowerCase();

        String idOrName;
        // Param -> named pokemon, no param -> random pokemon
        if (contentRaw.length() < "pokemon ".length() + 2) {
            Random rand = new Random();
            // Current pokemon ID goes up from [1 - 802]
            idOrName = Integer.toString(rand.nextInt(802) + 1);
        } else {
            idOrName = contentRaw.split(" ")[1];
        }

        Unirest.get("https://pokeapi.co/api/v2/pokemon/{targetPokemon}")
                .routeParam("targetPokemon", idOrName)
                .asJson()
                .ifSuccess(response -> processSuccess(event, response))
                .ifFailure(response -> {
                    event.getChannel().sendMessage("Oh no! Status " + response.getStatus() + ". Please check your spelling").queue();
                    response.getParsingError().ifPresent(e -> {
                        LOGGER.error("Parsing Exception: ", e);
                        LOGGER.error("Original body: " + e.getOriginalBody());
                    });
                });
    }

    private void processSuccess(MessageReceivedEvent event, HttpResponse<JsonNode> jsonResponse) {
        JSONObject jsonObject = jsonResponse.getBody().getObject();

        String name = AlfredoUtils.capitalizeFirst(jsonObject.getString("name"));
        String id = Integer.toString(jsonObject.getInt("id"));

        String mainType;
        String subType;
        JSONArray typesArray = jsonObject.getJSONArray("types");
        if (typesArray.length() == 1) {
            mainType = AlfredoUtils.capitalizeFirst(
                    typesArray.getJSONObject(0)
                            .getJSONObject("type")
                            .getString("name"));
            subType = null;
        } else {
            // If there are two types, the main type is actually placed on the second slot of array
            mainType = AlfredoUtils.capitalizeFirst(
                    typesArray.getJSONObject(1)
                            .getJSONObject("type")
                            .getString("name"));
            subType = AlfredoUtils.capitalizeFirst(
                    typesArray.getJSONObject(0)
                            .getJSONObject("type")
                            .getString("name"));
        }

        String joinedType;
        if (subType == null) {
            joinedType = mainType;
        } else {
            joinedType = mainType + " / " + subType;
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(name)
                .setImage(AlfredoUtils.getPokemonImageUrl(id))
                .setColor(AlfredoUtils.getColor(mainType))
                .addField("ID: ", id, true)
                .addField("Type: ", joinedType, true);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
