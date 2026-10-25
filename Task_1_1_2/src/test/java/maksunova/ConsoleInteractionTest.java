package maksunova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import maksunova.game.console.ConsoleInteraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ConsoleInteractionTest {

    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreInput() {
        System.setIn(originalIn);
    }

    private ConsoleInteraction interactionWith(String input) {
        System.setIn(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)
        ));
        return new ConsoleInteraction();
    }

    @Test
    void actionOneMeansTakeCard() {
        assertTrue(interactionWith("1\n").askAction());
    }

    @Test
    void actionZeroMeansStop() {
        assertFalse(interactionWith("0\n").askAction());
    }

    @Test
    void actionQuestionKeepsReadingUntilValidAnswer() {
        ConsoleInteraction console = interactionWith("maybe\nwat\n1\n");

        assertTrue(console.askAction());
    }

    @Test
    void newGameZeroMeansExit() {
        assertFalse(interactionWith("0\n").askNewGame());
    }

    @Test
    void newGameOneMeansContinue() {
        assertTrue(interactionWith("1\n").askNewGame());
    }

    @Test
    void newGameInvalidInputIsRejectedBeforeValidInput() {
        ConsoleInteraction console =
                interactionWith("banana\n2\n-1\n1\n");

        assertTrue(console.askNewGame());
    }
}
