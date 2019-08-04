package io.jlu.alfredo.utils;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class AlfredoUtils {

    private static Map<String, String> pokemonTypeColors;

    static {
        defineColors();
    }

    /**
     * Gets the first matching member of in a server
     * @param target name String to match members list against
     * @param event event trigger
     * @return a member that matches the String, null otherwise
     */
    public static Member getFirstMatchingMember(String target, MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            Member matchingMember = event.getGuild().getMembers()
                    .stream()
                    .filter((member) -> target.equalsIgnoreCase(member.getUser().getName()) || target.equalsIgnoreCase(member.getNickname()))
                    .findFirst()
                    .orElse(null);
            return matchingMember;
        }

        return null;
    }

    public static String getPokemonImageUrl(String id) {
        // TODO: How safe is this...?
        return "https://pokeres.bastionbot.org/images/pokemon/" + id + ".png";
    }

    public static String capitalizeFirst(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static Color getColor(String pokemonType) {
        return Color.decode(pokemonTypeColors.get(pokemonType.toLowerCase()));
    }

    private static void defineColors() {
        pokemonTypeColors = new HashMap<>();

        pokemonTypeColors.put("normal", "#A8A878");
        pokemonTypeColors.put("fighting", "#C03028");
        pokemonTypeColors.put("flying", "#A890F0");
        pokemonTypeColors.put("poison", "#A040A0");
        pokemonTypeColors.put("ground", "#E0C068");
        pokemonTypeColors.put("rock", "#B8A038");
        pokemonTypeColors.put("bug", "#A8B820");
        pokemonTypeColors.put("ghost", " #705898");
        pokemonTypeColors.put("steel", "#B8B8D0");
        pokemonTypeColors.put("fire", "#F08030");
        pokemonTypeColors.put("water", "#6890F0");
        pokemonTypeColors.put("grass", "#78C850");
        pokemonTypeColors.put("electric", " #F8D030");
        pokemonTypeColors.put("psychic", "#F85888");
        pokemonTypeColors.put("ice", "#98D8D8");
        pokemonTypeColors.put("dragon", "#7038F8");
        pokemonTypeColors.put("dark", "#705848");
        pokemonTypeColors.put("fairy", "#EE99AC");
    }
}
