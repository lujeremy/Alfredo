package io.jlu.alfredo;

import io.jlu.alfredo.commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class Alfredo extends ListenerAdapter {

    private static final String CREDENTIALS_FILE = "credentials.txt";
    private static final String TOKEN_FILE = "token.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws LoginException, IOException {
        getInstance().init(CREDENTIALS_FILE, TOKEN_FILE);
    }

    /**
     * Singleton instance
     */
    private static Alfredo INSTANCE = null;

    private Map<String, Command> commandMap = new HashMap<>();

    private Alfredo() {}

    public static Alfredo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Alfredo();
        }

        return INSTANCE;
    }

    /**
     * Initialize the bot
     */
    public void init(String credentialsFile, String tokenFile) throws LoginException, IOException {
        // obligatory ascii art
        System.out.println(
               "   _   _  __              _     \n" +
                       "  /_\\ | |/ _|_ __ ___  __| | ___  \n" +
                       " //_\\\\| | |_| '__/ _ \\/ _` |/ _ \\ \n" +
                       "/  _  \\ |  _| | |  __/ (_| | (_) |\n" +
                       "\\_/ \\_/_|_| |_|  \\___|\\__,_|\\___/ \n"
        );

        commandMap = initCommands(credentialsFile);

        // Allow bot to throw IO / LoginException here since bot cannot instantiate w/o a token
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        File file = new File(tokenFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String token = br.readLine();

        builder.setToken(token);
        builder.addEventListener(getInstance());
        builder.build();

        LOGGER.info("Alfredo successfully built with token");
    }

    static Map<String, Command> initCommands(String filename) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("compliment", new ComplimentCommand());
        commands.put("roast", new RoastCommand());
        commands.put("nature", new NatureCommand());
        commands.put("pokemon", new PokemonCommand());
        commands.put("hi", (event, parameter) -> event.getChannel().sendMessage("Hello, " + event.getAuthor().getName()).queue());
        commands.put("ahnee", (event, parameter) -> event.getChannel().sendMessage("frick").queue());
        commands.put("help", new HelpCommand());

        // Add only the db-related commands if db credentials are provided
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String jdbcUrl = br.readLine();
            Jdbi jdbi = Jdbi.create(jdbcUrl, br.readLine(), br.readLine());

            commands.put("record", new RecordCommand(jdbi));
            commands.put("show", new ShowCommand(jdbi));
        } catch (IOException e) {
            LOGGER.error("Database credentials not found or invalid, connection not established. All db commands will fail");
        }

        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();

        LOGGER.info("We received a message from " +
                author.getName() + ": " +
                event.getMessage().getContentRaw()
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
                LOGGER.warn("No existing command");
            }
        }
    }

}
