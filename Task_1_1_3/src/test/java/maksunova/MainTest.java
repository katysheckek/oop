package maksunova;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Набор юнит-тестов для проверки работы математических выражений.
 */
public class MainTest {

    /**
     * проверяет работу с консолью из входной точки.
     *
     * @throws Exception
     */
    @Test
    void testMainConsoleFlow() throws Exception {
        String input = "0\n"          // способ ввода: консоль
                + "2+3\n"        // выражение
                + "0\n"          // вывод в консоль
                + "0\n";         // выйти
        InputStream origIn = System.in;
        PrintStream origOut = System.out;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            Main.main(new String[]{});
        } finally {
            System.setIn(origIn);
            System.setOut(origOut);
        }
    }

    /**
     * проверяет работу с файлами из входной точки.
     *
     * @param tmp
     * @throws Exception
     */
    @Test
    void testMainFromFile(@TempDir Path tmp) throws Exception {
        // Main читает "output.txt" из CWD — поэтому chdir не сделать,
        // но можно создать output.txt рядом и удалить после
        Path f = Path.of("output.txt");
        Files.writeString(f, "2+3");
        try {
            String input = "1\n"   // из файла
                    + "0\n"   // в консоль
                    + "0\n";  // выход
            // ... подмена System.in/out + Main.main
        } finally {
            Files.deleteIfExists(f);
        }
    }
}