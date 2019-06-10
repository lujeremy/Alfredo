package io.jlu.alfredo;

import io.jlu.alfredo.commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdbi.v3.core.Jdbi;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Alfredo extends ListenerAdapter {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private static Map<String, Command> commandMap = new HashMap<>();

    public static void main(String[] args) throws LoginException, IOException {

        // Logger setup
        BasicConfigurator.configure();

        File file = new File("credentials.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String jdbcUrl = br.readLine();
        Jdbi jdbi = Jdbi.create(jdbcUrl, br.readLine(), br.readLine());

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        file = new File("token.txt");
        br = new BufferedReader(new FileReader(file));


        String token = br.readLine();

        commandMap.put("compliment", new ComplimentCommand());
        commandMap.put("roast", new RoastCommand());
        commandMap.put("record", new RecordCommand(jdbi));
        commandMap.put("show", new ShowCommand(jdbi));
        commandMap.put("hi", (event, parameter) -> event.getChannel().sendMessage("Hello, " + event.getAuthor().getName()).queue());
        commandMap.put("ahnee", (event, parameter) -> event.getChannel().sendMessage("frick").queue());
        commandMap.put("help", new HelpCommand());

        builder.setToken(token);
        builder.addEventListener(new Alfredo());

        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();

        logger.info("We received a message from " +
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
                logger.warn("No existing command");
            }
        }
    }
}
