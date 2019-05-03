package io.jlu.jerbot;

import io.jlu.jerbot.commands.*;
import io.jlu.jerbot.utils.JerBotUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.jdbi.v3.core.Jdbi;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JerBot extends ListenerAdapter {

    static Map<String, Command> commandMap = new HashMap<>();

    public static void main(String[] args) throws LoginException, IOException {

        String jdbcUrl = "jdbc:mysql://localhost:3306/workout";
        Jdbi jdbi = Jdbi.create(jdbcUrl, "root", "9033");

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        File file = new File("token.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String token = br.readLine();

        commandMap.put("compliment", new ComplimentCommand());
        commandMap.put("roast", new RoastCommand());
        commandMap.put("record", new RecordCommand(jdbi));
        commandMap.put("show", new ShowCommand(jdbi));
        commandMap.put("hi", (event, parameter) -> event.getChannel().sendMessage("Hello, " + event.getAuthor().getName()).queue());
        commandMap.put("ahnee", (event, parameter) -> event.getChannel().sendMessage("frick").queue());
        commandMap.put("help", new HelpCommand(commandMap));

        builder.setToken(token);
        builder.addEventListener(new JerBot());

        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();

        System.out.println("We received a message from " +
                author.getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        String contentRaw = event.getMessage().getContentRaw();
        if (contentRaw == null || contentRaw.length() == 0) {
            return;
        }

        String[] contentArr = contentRaw.split(" ", 2);
        String command = contentArr[0].substring(1).toLowerCase();
        String parameter = "";

        if (contentArr.length > 1) {
            parameter = contentArr[1].toLowerCase();
        }
        if (contentRaw.startsWith("!")) {
            Command commandHandler = commandMap.get(command);

            if (commandHandler != null) {
                commandHandler.handleEvent(event, parameter);
            } else {
                System.out.println("No existing command");
            }
        }
    }
}
