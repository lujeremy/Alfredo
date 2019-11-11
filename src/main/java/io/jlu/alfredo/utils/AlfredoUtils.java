package io.jlu.alfredo.utils;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class AlfredoUtils {

    private static Map<String, Color> pokemonTypeColors;

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
        return "https://pokeres.bastionbot.org/images/pokemon/" + id + ".png";
    }

    public static String capitalizeFirst(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static Color getColor(String pokemonType) {
        return pokemonTypeColors.get(pokemonType.toLowerCase());
    }

    private static void defineColors() {
        pokemonTypeColors = new HashMap<>();

        pokemonTypeColors.put("normal", Color.decode("#A8A878"));
        pokemonTypeColors.put("fighting", Color.decode("#C03028"));
        pokemonTypeColors.put("flying", Color.decode("#A890F0"));
        pokemonTypeColors.put("poison", Color.decode("#A040A0"));
        pokemonTypeColors.put("ground", Color.decode("#E0C068"));
        pokemonTypeColors.put("rock", Color.decode("#B8A038"));
        pokemonTypeColors.put("bug", Color.decode("#A8B820"));
        pokemonTypeColors.put("ghost", Color.decode("#705898"));
        pokemonTypeColors.put("steel", Color.decode("#B8B8D0"));
        pokemonTypeColors.put("fire", Color.decode("#F08030"));
        pokemonTypeColors.put("water", Color.decode("#6890F0"));
        pokemonTypeColors.put("grass", Color.decode("#78C850"));
        pokemonTypeColors.put("electric", Color.decode("#F8D030"));
        pokemonTypeColors.put("psychic", Color.decode("#F85888"));
        pokemonTypeColors.put("ice", Color.decode("#98D8D8"));
        pokemonTypeColors.put("dragon", Color.decode("#7038F8"));
        pokemonTypeColors.put("dark", Color.decode("#705848"));
        pokemonTypeColors.put("fairy", Color.decode("#EE99AC"));
    }

    public static Map<String, Color> getPokemonTypeColors() {
        return pokemonTypeColors;
    }
}
