package io.jlu.jerbot;

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
import java.util.ArrayList;
import java.util.List;

public class JerBot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        File file = new File("token.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String token = br.readLine();

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

        if (contentRaw.startsWith("?")) {
            String command = contentRaw.substring(1);

            if (command.equals("foo")) {
                channel.sendMessage("bar").queue();
            } else if (command.equals("hi")) {
                channel.sendMessage("Hello, " + author.getName()).queue();
            } else if (command.equals("ahnee")) {
                channel.sendMessage("frick").queue();
            } else if (command.equals("assign")) {
                try {
                    HttpResponse<JsonNode> jsonResponse = Unirest.get("https://corporatebs-generator.sameerkumar.website/").asJson();
                    String phrase = jsonResponse.getBody().getObject().getString("phrase");
                    channel.sendMessage(phrase).queue();
                } catch (UnirestException e) {
                    return;
                }
            } else if (command.startsWith("roast ")) {
                String target = contentRaw.substring(7);
                List<Member> memberList = event.getGuild().getMembersByName(target,true);

                if (memberList.isEmpty()) {
                    memberList = event.getGuild().getMembersByNickname(target,true);
                }

                if (!memberList.isEmpty()) {
                    channel.sendMessage(
                            author.getName() + " -insert msg- " + memberList.get(0).getEffectiveName()).queue();
                } else {
                    channel.sendMessage("No one found").queue();
                }
            }
//            else {
//                event.getChannel().sendMessage("Me no understand").queue();
//            }
        }
    }
}
