package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class MainTest {

    private final ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errBytes = new ByteArrayOutputStream();

    private final PrintStream out = new PrintStream(outBytes, true, StandardCharsets.UTF_8);
    private final PrintStream err = new PrintStream(errBytes, true, StandardCharsets.UTF_8);

    private String stdout() {
        return outBytes.toString(StandardCharsets.UTF_8);
    }
    private String stderr() {
        return errBytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void runPrintsPositions() throws IOException {
        int code = Main.run(new String[] {"input.txt", "бра"}, out, err);

        assertEquals(0, code);
        assertEquals("[1, 8]" + System.lineSeparator(), stdout());
        assertEquals("", stderr());
    }
}
