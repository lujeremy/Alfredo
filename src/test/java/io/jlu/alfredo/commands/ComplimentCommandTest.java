package io.jlu.alfredo.commands;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ComplimentCommandTest {

    private static MessageReceivedEvent mockEvent;
    private static MessageChannel mockChannel;

    @BeforeEach
    void init() {
        mockEvent = mock(MessageReceivedEvent.class);
        mockChannel = mock(MessageChannel.class);
        Message mockMessage = mock(Message.class);
        MessageAction mockAction = mock(MessageAction.class);

        when(mockEvent.getChannel()).thenReturn(mockChannel);
        when(mockEvent.getMessage()).thenReturn(mockMessage);
        when(mockChannel.sendMessage(anyString())).thenReturn(mockAction);

        User user = new UserImpl(1, null).setName("yellow beaver");
        Member mockMember = mock(Member.class);
        when(mockMember.getUser()).thenReturn(user);
        when(mockMember.getNickname()).thenReturn("poop");
        List<Member> memberList = new ArrayList<>();
        memberList.add(mockMember);

        Guild mockGuild = mock(Guild.class);
        when(mockGuild.getMembers()).thenReturn(memberList);

        when(mockMember.getEffectiveName()).thenReturn(user.getName());
        when(mockEvent.getAuthor()).thenReturn(user);
        when(mockEvent.getGuild()).thenReturn(mockGuild);
    }

    @Test
    @DisplayName("ComplimentCommand should compliment an existing member")
    void testEventTrigger() {
        Message message = new MessageBuilder().setContent("!compliment yellow beaver").build();
        when(mockEvent.getMessage()).thenReturn(message);

        ComplimentCommand command = new ComplimentCommand();
        command.handleEvent(mockEvent, "");

        String expected = "yellow beaver";

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockChannel, times(1)).sendMessage(argCaptor.capture());

        String actual = argCaptor.getValue();
        assertTrue(actual.startsWith(expected));
    }

    @Test
    @DisplayName("ComplimentCommand should note no one found when there are no matching members")
    void testNoMatches() {
        Message message = new MessageBuilder().setContent("!compliment elk").build();
        when(mockEvent.getMessage()).thenReturn(message);

        ComplimentCommand command = new ComplimentCommand();
        command.handleEvent(mockEvent, "");

        String expected = "No one found";

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockChannel).sendMessage(argCaptor.capture());

        String actual = argCaptor.getValue();
        assertTrue(actual.startsWith(expected));
    }

    @Test
    @DisplayName("ComplimentCommand should note no one found when blank space is specified")
    void testEmptySpace() {
        Message message = new MessageBuilder().setContent("!compliment   ").build();
        when(mockEvent.getMessage()).thenReturn(message);

        ComplimentCommand command = new ComplimentCommand();
        command.handleEvent(mockEvent, "");

        String expected = "No one found";

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockChannel).sendMessage(argCaptor.capture());

        String actual = argCaptor.getValue();
        assertTrue(actual.startsWith(expected));
    }

}
