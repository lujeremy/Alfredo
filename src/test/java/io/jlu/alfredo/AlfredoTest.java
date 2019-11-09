package io.jlu.alfredo;

import org.junit.jupiter.api.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AlfredoTest {

    private static Alfredo INSTANCE;
    //TODO: use following MRE mock for command tests
//    private static MessageReceivedEvent mockEvent;
//        mockEvent = mock(MessageReceivedEvent.class);
//        Message message = new MessageBuilder().setContent("!oof").build();
//        User user = new UserImpl(1, null).setName("Yellow Beaver");
//        when(mockEvent.getMessage()).thenReturn(message);
//        when(mockEvent.getAuthor()).thenReturn(user);

    @BeforeEach
    void init() {
        INSTANCE = Alfredo.getInstance();
    }

    @Test
    @DisplayName("IOException is thrown when incorrect token file is provided ╯°□°）╯")
    void testNoToken() {
        Exception exception = assertThrows(IOException.class, () ->
                INSTANCE.init("credentials.txt", "NOT A TOKEN"));
        assertEquals("NOT A TOKEN (The system cannot find the file specified)",
                exception.getMessage());
    }

    @Test
    @DisplayName("LoginException is thrown when bad token is provided")
    void testBadToken() {
        Exception exception = assertThrows(LoginException.class, () ->
                INSTANCE.init("credentials.txt", "src/test/resources/BadToken.txt"));
        assertEquals("The provided token is invalid!",
                exception.getMessage());
    }

    @Test
    @DisplayName("No exceptions thrown when incorrect credentials file is provided")
    void testNoCredentials() {
        assertDoesNotThrow(() -> INSTANCE.init("Potato", "token.txt"),
                "Should not throw an exception");
    }

    @Test
    @DisplayName("Without credentials, DB-related commands should not be initialized")
    void testMNoCredentialsMap() throws LoginException, IOException {
        INSTANCE.init("Potato", "token.txt");
        assertNull(INSTANCE.getCommandMap().get("record"));
        assertNull(INSTANCE.getCommandMap().get("show"));
    }

    @Test
    @DisplayName("With credentials, DB-related commands should be initialized")
    void testCredentialsMap() throws LoginException, IOException {
        INSTANCE.init("credentials.txt", "token.txt");
        assertNotNull(INSTANCE.getCommandMap().get("record"));
        assertNotNull(INSTANCE.getCommandMap().get("show"));
    }

    @AfterEach
    void tearDown() {
        INSTANCE = null;
    }

}