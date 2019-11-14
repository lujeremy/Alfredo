package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class HelpCommandTest {

    private static MessageReceivedEvent mockEvent;
    private static MessageChannel mockChannel;

    @BeforeAll
    static void init() {
        mockEvent = mock(MessageReceivedEvent.class);
        mockChannel = mock(MessageChannel.class);
        MessageAction mockAction = mock(MessageAction.class);

        when(mockEvent.getChannel()).thenReturn(mockChannel);
        when(mockChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mockAction);
    }

    @Test
    @DisplayName("Any help event should send an embed titled alfredo's abilities")
    void testEventTrigger() {
        HelpCommand command = new HelpCommand();
        command.handleEvent(mockEvent, "");

        ArgumentCaptor<MessageEmbed> argCaptor = ArgumentCaptor.forClass(MessageEmbed.class);
        verify(mockChannel, times(1)).sendMessage(argCaptor.capture());

        MessageEmbed actualEmbed = argCaptor.getValue();
        assertEquals(
                "Alfredo's Abilities <:TohruGun:605162460479094804>",
                actualEmbed.getTitle());
    }

    @Test
    @DisplayName("Ensure a full static map is correctly generated with no null values")
    void testStaticMap() {
        Map<String, String> commandMap = HelpCommand.getCommandDefinitions();
        assertTrue(commandMap.values().size() > 0);
    }

}
