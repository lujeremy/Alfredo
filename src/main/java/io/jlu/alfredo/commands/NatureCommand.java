package io.jlu.alfredo.commands;


import io.jlu.alfredo.utils.AlfredoUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class NatureCommand implements Command {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public NatureCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();

        if (contentRaw.length() < "nature ".length() + 2) {
            channel.sendMessage("Incorrect usage, please specify the nature you want details on.").queue();
            return;
        }

        String targetNature = contentRaw.substring("nature ".length() + 1);

        Unirest.get("https://pokeapi.co/api/v2/nature/{targetNature}")
                .routeParam("targetNature", targetNature)
                .asJson()
                .ifSuccess(response -> processSuccess(event, response))
                .ifFailure(response -> {
                    logger.error("Oh No! Status " + response.getStatus());
                    response.getParsingError().ifPresent(e -> {
                        logger.error("Parsing Exception: ", e);
                        logger.error("Original body: " + e.getOriginalBody());
                    });
                });
    }

    private void processSuccess(MessageReceivedEvent event, HttpResponse<JsonNode> jsonResponse) {
        JSONObject jsonObject = jsonResponse.getBody().getObject();

        // Retrieve the two stats we care about-- increase/decreased stat
        String name = jsonObject.getString("name");
        String increased = jsonObject.getJSONObject("increased_stat").getString("name");
        String decreased = jsonObject.getJSONObject("decreased_stat").getString("name");
        String japaneseName = jsonObject.getJSONArray("names").getJSONObject(0).getString("name");

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Nature: " + name)
                .setThumbnail(AlfredoUtils.getRandomPokemonSprite())
                .addField("Japanese Name: ", japaneseName, false)
                .addField("Increased Stat: ", increased, false)
                .addField("Decreased Stat: ", decreased, false);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
