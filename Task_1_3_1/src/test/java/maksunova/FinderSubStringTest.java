package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FinderSubStringTest {

    private static final String emoji = new String(Character.toChars(0x1F600));
    private final FinderSubString finder = new FinderSubString();

    @TempDir
    Path tempDir;

    private static InputStream utf8(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void findsOccurrencesInFile() throws IOException {
        Path file = tempDir.resolve("input.txt");
        Files.writeString(file, "абракадабра", StandardCharsets.UTF_8);

        assertEquals(List.of(1L, 8L), finder.find(file.toString(), "бра"));
    }

    @Test
    void missingFileThrows() {
        String missing = tempDir.resolve("nope.txt").toString();
        assertThrows(NoSuchFileException.class, () -> finder.find(missing, "a"));
    }

    @Test
    void emptySubstringIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> finder.find("input.txt", ""));
    }

    @Test
    void positionsAreCountedInCodePoints() throws IOException {
        String text = "a" + emoji + "b" + emoji;
        assertEquals(List.of(1L, 3L), finder.find(utf8(text), emoji));
        assertEquals(List.of(2L), finder.find(utf8(text), "b"));
    }


    @Test
    void hugeGeneratedStreamIsProcessedInConstantMemory() throws IOException {
        long repeats = 20_000_000L;
        byte[] unit = "абракадабра".getBytes(StandardCharsets.UTF_8);
        long[] count = {0};

        finder.find(new RepeatingInputStream(unit, repeats), "бра", position -> {
            long block = count[0] / 2;
            long expected = 11 * block + (count[0] % 2 == 0 ? 1 : 8);
            assertEquals(expected, position);
            count[0]++;
        });
    }

    private static final class RepeatingInputStream extends InputStream {
        private final byte[] unit;
        private final long totalBytes;
        private long position;

        RepeatingInputStream(byte[] unit, long repeats) {
            this.unit = unit;
            this.totalBytes = unit.length * repeats;
        }

        @Override
        public int read() {
            if (position >= totalBytes) {
                return -1;
            }
            return unit[(int) (position++ % unit.length)] & 0xFF;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            if (len == 0) {
                return 0;
            }
            if (position >= totalBytes) {
                return -1;
            }
            int n = (int) Math.min(len, totalBytes - position);
            for (int i = 0; i < n; i++) {
                b[off + i] = unit[(int) ((position + i) % unit.length)];
            }
            position += n;
            return n;
        }
    }



}
