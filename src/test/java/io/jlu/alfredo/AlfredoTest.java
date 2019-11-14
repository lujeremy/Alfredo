package io.jlu.alfredo;

import io.jlu.alfredo.commands.Command;
import org.junit.jupiter.api.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlfredoTest {

    private static Alfredo INSTANCE;

    @BeforeEach
    void init() {
        INSTANCE = Alfredo.getInstance();
    }

    @Test
    @DisplayName("IOException is thrown when incorrect token file is provided ╯°□°）╯")
    void testNoToken() {
        assertThrows(IOException.class, () ->
                INSTANCE.init("credentials.txt", "NOT A TOKEN"));
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
    void testMNoCredentialsMap() {
        Map<String, Command> map = Alfredo.initCommands("Potato");
        assertNull(map.get("record"));
        assertNull(map.get("show"));
    }

    @Test
    @DisplayName("With credentials, DB-related commands should be initialized")
    void testCredentialsMap() {
        Map<String, Command> map = Alfredo.initCommands("credentials.txt");
        assertNotNull(map.get("record"));
        assertNotNull(map.get("show"));
    }

    @AfterEach
    void tearDown() {
        INSTANCE = null;
    }

}