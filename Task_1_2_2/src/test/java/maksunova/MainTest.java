package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * Сверяет вывод {@link Main} с ожидаемым результатом из задания.
 */
public class MainTest {
    @Test
    void printsUpdatedValue() {
        PrintStream original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            Main.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        assertEquals("1.0" + System.lineSeparator(), captured.toString());
    }
}
