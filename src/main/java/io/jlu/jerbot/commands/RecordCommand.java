package io.jlu.jerbot.commands;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RecordCommand implements Command {

    public RecordCommand() {

    }

    @Override
    public void handleEvent(MessageReceivedEvent event, String parameter) {
        MessageChannel channel = event.getChannel();
        String contentRaw = event.getMessage().getContentRaw();
        User author = event.getAuthor();

        String info = contentRaw.substring("roast ".length() + 1);
        String[] parsedInfo = info.split(" ");

        if (parsedInfo.length == 0) {
            return;
        }

        String workoutName = "";
        int reps = 0;
        int weight = 0;

        for (int i = 0; i < parsedInfo.length; i++) {

            try {
                int num = Integer.parseInt(parsedInfo[i]);
                if (num <= 14) {
                    reps = num;
                } else {
                    weight = num;
                }
            } catch (NumberFormatException e) {
                workoutName += parsedInfo[i];
            }
        }

        channel.sendMessage("Workout: " + workoutName + ", " + "Reps: " + reps + ", Weight: " + weight + ".").queue();

    }
}
