package io.jlu.jerbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
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
        System.out.println("We received a message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        if (event.getMessage().getContentRaw() == null || event.getMessage().getContentRaw().length() == 0) {
            return;
        }

        if (event.getMessage().getContentRaw().substring(0,1).equals("?")) {
            if (event.getMessage().getContentRaw().substring(1).equals("foo")) {
                event.getChannel().sendMessage("bar").queue();
            } else if (event.getMessage().getContentRaw().substring(1).equals("hi")) {
                event.getChannel().sendMessage("Hello, " + event.getAuthor().getName()).queue();
            } else if (event.getMessage().getContentRaw().substring(1).equals("ahnee")) {
                event.getChannel().sendMessage("frick").queue();
            } else if (event.getMessage().getContentRaw().substring(1, 6).equals("roast")) {
                List<Member> memberList = event.getGuild().getMembersByName(event.getMessage().getContentRaw()
                        .substring(7),true);

                if (memberList.size() == 0) {
                    memberList = event.getGuild().getMembersByNickname(event.getMessage().getContentRaw()
                            .substring(7),true);
                }

                if (memberList.size() != 0) {
                    event.getChannel().sendMessage(
                            event.getAuthor().getName() + " -insert msg- " + memberList.get(0).getEffectiveName()).queue();
                } else {
                    event.getChannel().sendMessage("No one found").queue();
                }
            }
//            else {
//                event.getChannel().sendMessage("Me no understand").queue();
//            }
        }
    }
}
