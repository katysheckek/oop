package maksunova;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MainTest {

    private static final Path FILE = Path.of("test.txt");

    /**
     * запускает Main с заданным содержимым test.txt (null - файла нет)
     * и возвращает всё, что Main напечатал. настоящий test.txt возвращается на место.
     */
    private static String runMain(String fileContent) throws IOException {
        byte[] backup = Files.exists(FILE) ? Files.readAllBytes(FILE) : null;
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            if (fileContent == null) {
                Files.deleteIfExists(FILE);
            } else {
                Files.writeString(FILE, fileContent);
            }
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            assertDoesNotThrow(() -> Main.main(new String[0]));
        } finally {
            System.setOut(original);
            if (backup != null) {
                Files.write(FILE, backup);
            } else {
                Files.deleteIfExists(FILE);
            }
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void mainPrintsGreetingAndCounter() throws IOException {
        String output = runMain(null);
        assertTrue(output.contains("Hello and welcome!"));
        assertTrue(output.contains("i = 1"));
        assertTrue(output.contains("i = 5"));
    }

    @Test
    void missingFileIsReported() throws IOException {
        String output = runMain(null);
        assertTrue(output.contains("Не удалось прочитать test.txt"));
    }

    @Test
    void directedGraphIsPrintedAndSortedByBothAlgorithms() throws IOException {
        String output = runMain(String.join("\n",
                "directed? - 1",
                "type of data? - String",
                "=== vertices - 3",
                "A = a",
                "B = b",
                "C = c",
                "=== edges - 2",
                "A B",
                "B C",
                "===") + "\n");

        assertTrue(output.contains("(ориентированный)"));
        assertTrue(output.contains("topSort:"));
        assertTrue(output.contains("topSort (поиск в глубину):"));
    }

    @Test
    void undirectedGraphCannotBeSorted() throws IOException {
        String output = runMain(String.join("\n",
                "directed? - 0",
                "type of data? - String",
                "=== vertices - 2",
                "A = a",
                "B = b",
                "=== edges - 1",
                "A B",
                "===") + "\n");

        assertTrue(output.contains("(неориентированный)"));
        assertTrue(output.contains("topological sort only for oriented graphs"));
    }
}