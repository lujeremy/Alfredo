package io.jlu.alfredo.utils;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlfredoUtilsTest {

    private static MessageReceivedEvent mockEvent;

    @BeforeEach
    void init() {
        mockEvent = mock(MessageReceivedEvent.class);
        Message message = new MessageBuilder().setContent("!oof").build();
        User user = new UserImpl(1, null).setName("Yellow Beaver");

        Member m = mock(Member.class);
        when(m.getUser()).thenReturn(user);
        when(m.getNickname()).thenReturn("poop");
        List<Member> memberList = new ArrayList<>();
        memberList.add(m);

        Guild mockGuild = mock(Guild.class);
        when(mockGuild.getMembers()).thenReturn(memberList);

        when(mockEvent.getMessage()).thenReturn(message);
        when(mockEvent.getAuthor()).thenReturn(user);
        when(mockEvent.getGuild()).thenReturn(mockGuild);
    }

    @Test
    @DisplayName("Matching member should return correct user, ignoring case")
    void testMatchingMember() {
        Member match = AlfredoUtils.getFirstMatchingMember("yElLoW beaver", mockEvent);
        assertNotNull(match);
        assertEquals(match.getUser().getName(), "Yellow Beaver");
    }

    @Test
    @DisplayName("Matching member should return null when there are no matches")
    void testNoMatchingMember() {
        Member noMatch = AlfredoUtils.getFirstMatchingMember("blue beaver", mockEvent);
        assertNull(noMatch);
    }

    @Test
    @DisplayName("Empty strings should not match anything")
    void testNoEmptyStringMatches() {
        Member noMatch = AlfredoUtils.getFirstMatchingMember("", mockEvent);
        assertNull(noMatch);
    }

    @Test
    @DisplayName("Spaces should not match anything")
    void testSpaceStringMatches() {
        Member noMatch = AlfredoUtils.getFirstMatchingMember(" ", mockEvent);
        assertNull(noMatch);
    }

}
