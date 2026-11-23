package maksunova;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongConsumer;

public class FinderSubString {

    static final int BUFF_SIZE = 64 * 1000;

    public List<Long> find(String fileName, String substring) throws IOException {

        Objects.requireNonNull(fileName, "fileName");

        if (substring.isEmpty()) {
            throw new IllegalArgumentException("Подстрока не должна быть пустой");
        }
        Objects.requireNonNull(substring, "substring");

        List<Long> result = new ArrayList<>();
        find(fileName, substring, result::add);
        return result;
    }

    public void find(String fileName, String substring, LongConsumer consumer) throws IOException {

        try (InputStream in = Files.newInputStream(Path.of(fileName))) {
            find(in, substring, consumer);
        }
    }

    public List<Long> find(InputStream in, String substring) throws IOException {

        List<Long> result = new ArrayList<>();
        find(in, substring, result::add);
        return result;
    }


    public void find(InputStream inputStream, String pattern, LongConsumer onMatch) throws IOException {

        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(onMatch, "onMatch");

        Matcher matcher = new Matcher(pattern.codePoints().toArray(), onMatch);
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        char[] buffer = new char[BUFF_SIZE];
        int pendingHighSurrogate = -1;

        int charsRead;
        while ((charsRead = reader.read(buffer)) != -1) {
            for (int i = 0; i < charsRead; i++) {
                char ch = buffer[i];
                if (pendingHighSurrogate >= 0) {
                    if (Character.isLowSurrogate(ch)) {
                        matcher.accept(Character.toCodePoint((char) pendingHighSurrogate, ch));
                        pendingHighSurrogate = -1;
                        continue;
                    }
                    matcher.accept(pendingHighSurrogate);
                    pendingHighSurrogate = -1;
                }
                if (Character.isHighSurrogate(ch)) {
                    pendingHighSurrogate = ch;
                } else {
                    matcher.accept(ch);
                }
            }
        }
        if (pendingHighSurrogate >= 0) {
            matcher.accept(pendingHighSurrogate);
        }
    }

    private static final class Matcher {
        private final int[] pattern;
        private final int[] prefix;
        private final LongConsumer consumer;
        private int matched;
        private long index;

        Matcher(int[] pattern, LongConsumer consumer) {
            this.pattern = pattern;
            this.prefix = prefixFunction(pattern);
            this.consumer = consumer;
        }

        void accept(int codePoint) {
            while (matched > 0 && pattern[matched] != codePoint) {
                matched = prefix[matched - 1];
            }
            if (pattern[matched] == codePoint) {
                matched++;
            }
            if (matched == pattern.length) {
                consumer.accept(index - pattern.length + 1);
                matched = prefix[matched - 1];
            }
            index++;
        }

        private static int[] prefixFunction(int[] pattern) {
            int[] prefixLength = new int[pattern.length];

            for (int i = 1, len = 0; i < pattern.length; i++) {
                while (len > 0 && pattern[i] != pattern[len]) {
                    len = prefixLength[len - 1];
                }
                if (pattern[i] == pattern[len]) {
                    len++;
                }
                prefixLength[i] = len;
            }

            return prefixLength;
        }
    }

}
