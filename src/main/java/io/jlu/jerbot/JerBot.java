package io.jlu.jerbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.*;

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

        if (event.getMessage().getContentRaw().substring(0,1).equals("?")) {
            if (event.getMessage().getContentRaw().substring(1).equals("foo")) {
                event.getChannel().sendMessage("bar").queue();
            } else if (event.getMessage().getContentRaw().substring(1).equals("hi")) {
                event.getChannel().sendMessage("Hello, " + event.getAuthor().getName()).queue();
            } else if (event.getMessage().getContentRaw().substring(1).equals("ahnee")) {
                event.getChannel().sendMessage("frick").queue();
            } else {
                event.getChannel().sendMessage("Me no understand").queue();
            }
        }
    }
}
