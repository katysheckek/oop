package maksunova.graph.getting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class GraphReader {

    public record VertexRec(String name, Object data) {}
    public record EdgeRec(String from, String to) {}
    public record GraphData(boolean directed, String typeName,
                            List<VertexRec> vertices, List<EdgeRec> edges) {}

    private static final Map<String, Function<String, ?>> PARSERS = Map.of(
            "String", s -> s,
            "Integer", Integer::valueOf,
            "Double", Double::valueOf,
            "Float", Float::valueOf,
            "Boolean", Boolean::valueOf,
            "Character", s -> s.charAt(0)
    );

    // === единое форматирование ошибок через \n ===
    private static InputMismatchException formatErr(int line, String expected) {
        return new InputMismatchException("\nWrong text format in the line:" + (line + 1)
                + ".\nPlease check the structure.\nShould be " + expected + ".");
    }
    private static IllegalArgumentException badValue(int line, String value, RuntimeException cause) {
        return new IllegalArgumentException("\nBad value in the line:" + (line + 1)
                + ".\nValue: \"" + value + "\".\nCheck the declared data type.", cause);
    }
    private static InputMismatchException negativeCount(int line, String what) {
        return new InputMismatchException("\nNegative " + what + " count in the line:" + (line + 1)
                + ".\nPlease check the structure.");
    }

    public static GraphData readfileGraph(String filename) throws IOException {
        Path fpath = Path.of(filename);

        // пустые строки и пробелы по краям не важны
        List<String> lines = Files.readAllLines(fpath).stream()
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .toList();

        try {
            return parse(lines, filename);
        } catch (RuntimeException ex) {
            // InputMismatchException, NumberFormatException, IndexOutOfBounds и т.д.
            throw new IOException("Cannot read graph from \"" + filename + "\": " + ex.getMessage(), ex);
        }
    }

    private static GraphData parse(List<String> lines, String filename) {
        if (lines.isEmpty()) {
            throw new InputMismatchException("\nNo data in file \"" + filename + "\".");
        }

        int linec = 0; // индекс текущей строки

        // directed? - n
        String line = lines.get(linec).replaceAll("\\s+", "");
        String prefix = "directed?-";
        if (!line.startsWith(prefix)) throw formatErr(linec, "\"directed? - 0 or 1\"");

        String dir = line.substring(prefix.length());
        boolean directed;
        if (dir.equals("0"))      directed = false;
        else if (dir.equals("1")) directed = true;
        else throw formatErr(linec, "\"directed? - (0 or 1)\"");
        linec++;

        // type of data? - String
        line = lines.get(linec).replaceAll("\\s+", "");
        prefix = "typeofdata?-";
        if (!line.startsWith(prefix)) throw formatErr(linec, "\"type of data? - supported format\"");

        String typeName = line.substring(prefix.length()); // без toLowerCase: ключи PARSERS с заглавной буквы
        Function<String, ?> parser = PARSERS.get(typeName);
        if (parser == null) throw new IllegalArgumentException("\nUnsupported data type: \"" + typeName + "\".");
        linec++;

        // === vertices - 3
        line = lines.get(linec).replaceAll("\\s+", "");
        prefix = "===vertices-";
        if (!line.startsWith(prefix)) throw formatErr(linec, "\"=== vertices - some amount\"");
        int vers = Integer.parseInt(line.substring(prefix.length()));
        if (vers < 0) throw negativeCount(linec, "vertex");
        linec++;

        List<VertexRec> vertecies = new ArrayList<>();
        for (int id = 0; id < vers; id++) {
            String[] duo = lines.get(linec).split("=", 2);
            if (duo.length != 2 || duo[0].trim().isEmpty()) throw formatErr(linec, "\"vertex name = data\"");

            try {
                vertecies.add(new VertexRec(duo[0].trim(), parser.apply(duo[1].trim())));
            } catch (RuntimeException ex) {
                throw badValue(linec, duo[1].trim(), ex);
            }
            linec++;
        }

        // === edges - 3
        line = lines.get(linec).replaceAll("\\s+", "");
        prefix = "===edges-";
        if (!line.startsWith(prefix)) throw formatErr(linec, "\"=== edges - some amount\"");
        int edgs = Integer.parseInt(line.substring(prefix.length()));
        if (edgs < 0) throw negativeCount(linec, "edge");
        linec++;

        List<EdgeRec> edges = new ArrayList<>();
        for (int id = 0; id < edgs; id++) {
            String[] duo = lines.get(linec).split("\\s+");
            if (duo.length != 2) throw formatErr(linec, "\"(vertex source name) (vertex destination name)\"");
            edges.add(new EdgeRec(duo[0], duo[1]));
            linec++;
        }

        if (!lines.get(linec).equals("===")) throw formatErr(linec, "\"===\"");
        linec++;

        if (linec != lines.size()) {
            throw new InputMismatchException("\nUnexpected data after \"===\" in the line:" + (linec + 1)
                    + ".\nPlease check the structure.");
        }

        return new GraphData(directed, typeName, vertecies, edges);
    }
}