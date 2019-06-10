package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {

//    public void handleEvent(MessageReceivedEvent event);
    void handleEvent(MessageReceivedEvent event, String parameter);
}
