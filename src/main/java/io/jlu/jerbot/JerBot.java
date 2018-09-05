package io.jlu.jerbot;

import io.jlu.jerbot.commands.Command;
import io.jlu.jerbot.commands.GiveTaskCommand;
import io.jlu.jerbot.utils.JerBotUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JerBot extends ListenerAdapter {

    static Map<String, Command> commandMap = new HashMap<>();

    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        File file = new File("token.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String token = br.readLine();

        commandMap.put("givetask", new GiveTaskCommand());

        builder.setToken(token);
        builder.addEventListener(new JerBot());

        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();

        System.out.println("We received a message from " +
                author.getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        String contentRaw = event.getMessage().getContentRaw();

        if (contentRaw == null || contentRaw.length() == 0) {
            return;
        }

        String[] contentArr = contentRaw.split(" ", 2);
        String command = contentArr[0].substring(1);
        String parameter = "";
        if (contentArr.length > 1) {
            parameter = contentArr[1];
        }

        if (contentRaw.startsWith("?")) {
//            String command = contentRaw.substring(1).toLowerCase();

            if (command.equals("foo")) {
                channel.sendMessage("bar").queue();
            } else if (command.equals("hi")) {
                channel.sendMessage("Hello, " + author.getName()).queue();
            } else if (command.equals("ahnee")) {
                channel.sendMessage("frick").queue();
            } else if (command.startsWith("givetask ")) {
                commandMap.get("givetask").handleEvent(event, parameter);
            } else if (command.startsWith("roast ")) {
                String target = contentRaw.substring("roast ".length() + 1);
                Member match = JerBotUtils.getFirstMatchingMember(target, event);

                if (match != null) {
                    channel.sendMessage(
                            author.getName() + " -insert msg- " + match.getEffectiveName()).queue();
                } else {
                    channel.sendMessage("No one found").queue();
                }
            }
        }
    }
}
