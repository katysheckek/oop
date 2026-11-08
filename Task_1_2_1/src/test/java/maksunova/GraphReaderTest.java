package maksunova;

import static maksunova.TestUtil.DIRECTED_FILE;
import static maksunova.TestUtil.UNDIRECTED_FILE;
import static maksunova.TestUtil.tempFile;
import static maksunova.graph.getting.GraphReader.readfileGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import maksunova.graph.common.Graph;
import maksunova.graph.getting.GraphReader;
import maksunova.graph.getting.GraphReader.GraphData;
import org.junit.jupiter.api.Test;

class GraphReaderTest {

    private static GraphData read(String content) throws IOException {
        return readfileGraph(tempFile(content).toString());
    }

    private static List<String> names(GraphData data) {
        return data.vertices().stream().map(GraphReader.VertexRec::name).toList();
    }

    /**
     * ридер бросает и IOException, и InputMismatchException, и IllegalArgumentException.
     *
     * @param content откуда брать контент
     * @throws IOException вылезла ошибка при чтении файла
     */
    private static void assertBroken(String content) throws IOException {
        Path file = tempFile(content);
        assertThrows(IOException.class, () -> GraphReader.readfileGraph(file.toString()));
    }

    // ---------- корректные файлы ----------

    @Test
    void parsesUndirectedGraphWithLoop() throws IOException {
        GraphData data = read(UNDIRECTED_FILE);
        assertFalse(data.directed());
        assertEquals(4, data.edges().size());
        assertEquals("C", data.edges().get(3).from());
        assertEquals("C", data.edges().get(3).to());
    }

    @Test
    void parsesIntegerData() throws IOException {
        String file = String.join("\n",
                "directed? - 1", "type of data? - Integer",
                "=== vertices - 2", "A = 5", "B = -12",
                "=== edges - 1", "A B", "===") + "\n";
        GraphData data = read(file);
        assertEquals(5, data.vertices().get(0).data());
        assertEquals(-12, data.vertices().get(1).data());
    }

    @Test
    void parsesDoubleData() throws IOException {
        String file = String.join("\n",
                "directed? - 0", "type of data? - Double",
                "=== vertices - 1", "A = 2.5",
                "=== edges - 0", "===") + "\n";
        GraphData data = read(file);
        assertEquals(2.5, data.vertices().get(0).data());
        assertTrue(data.edges().isEmpty());
    }

    @Test
    void valueMayContainSpacesAndEqualsSigns() throws IOException {
        String file = String.join("\n",
                "directed? - 1", "type of data? - String",
                "=== vertices - 1", "A = x = y z",
                "=== edges - 0", "===") + "\n";
        assertEquals("x = y z", read(file).vertices().get(0).data());
    }

    @Test
    void blankLinesAndSurroundingSpacesAreIgnored() throws IOException {
        String spaced = "\n" + DIRECTED_FILE.replace("\n", "  \n\n");
        assertEquals(3, read(spaced).vertices().size());
    }

    @Test
    void graphWithoutVerticesIsAllowed() throws IOException {
        String file = String.join("\n",
                "directed? - 1", "type of data? - String",
                "=== vertices - 0", "=== edges - 0", "===") + "\n";
        GraphData data = read(file);
        assertTrue(data.vertices().isEmpty());
        assertTrue(data.edges().isEmpty());
    }

    // ---------- некорректные файлы ----------

    @Test
    void missingFileThrowsIoException() {
        assertThrows(IOException.class,
                () -> readfileGraph("no_such_file_for_graph_tests.txt"));
    }

    @Test
    void emptyFileThrows() throws IOException {
        assertBroken("");
    }

    @Test
    void edgeWithThreeNamesThrows() throws IOException {
        assertBroken(DIRECTED_FILE.replace("A B\n", "A B C\n"));
    }

    @Test
    void wrongDirectedFlagThrows() throws IOException {
        assertBroken(DIRECTED_FILE.replace("directed? - 1", "directed? - 2"));
        assertBroken(DIRECTED_FILE.replace("directed? - 1", "directed? - yes"));
    }

    @Test
    void wrongHeaderLinesThrow() throws IOException {
        assertBroken(DIRECTED_FILE.replace("directed? - 1", "oriented? - 1"));
        assertBroken(DIRECTED_FILE.replace("directed? - 1", "directed? 1"));
        assertBroken(DIRECTED_FILE.replace("=== vertices - 3", "=== vertices"));
        assertBroken(DIRECTED_FILE.replace("=== edges - 3", "=== lines - 3"));
    }

    @Test
    void unsupportedTypeThrows() throws IOException {
        assertBroken(DIRECTED_FILE.replace("type of data? - String", "type of data? - Long"));
    }

    @Test
    void valueThatDoesNotMatchTheTypeThrows() throws IOException {
        assertBroken(String.join("\n",
                "directed? - 1", "type of data? - Integer",
                "=== vertices - 1", "A = five",
                "=== edges - 0", "===") + "\n");
    }

    @Test
    void wrongCountsThrow() throws IOException {
        assertBroken(DIRECTED_FILE.replace("=== vertices - 3", "=== vertices - 4"));
        assertBroken(DIRECTED_FILE.replace("=== vertices - 3", "=== vertices - 2"));
        assertBroken(DIRECTED_FILE.replace("=== edges - 3", "=== edges - 4"));
        assertBroken(DIRECTED_FILE.replace("=== edges - 3", "=== edges - 2"));
        assertBroken(DIRECTED_FILE.replace("=== edges - 3", "=== edges - three"));
        assertBroken(DIRECTED_FILE.replace("=== vertices - 3", "=== vertices - -1"));
    }

    @Test
    void badVertexLinesThrow() throws IOException {
        assertBroken(DIRECTED_FILE.replace("A = data1", "A data1"));
    }

    @Test
    void badEdgeLinesThrow() throws IOException {
        assertBroken(DIRECTED_FILE.replace("A B\n", "A\n"));
    }

    @Test
    void missingClosingSeparatorThrows() throws IOException {
        assertBroken(DIRECTED_FILE.replace("===\n", ""));
    }

    @Test
    void extraLinesAfterClosingSeparatorThrow() throws IOException {
        assertBroken(DIRECTED_FILE + "A B\n");
    }

    // ---------- test.txt из корня проекта ----------

    @Test
    void testTxtGivesTheSameGraphInAllRepresentations() throws IOException {
        Path file = Path.of("test.txt");
        assumeTrue(Files.exists(file), "test.txt not found in the work directory");

        Graph<Object> matrix = new AdjMatrixGraph<>(false);
        Graph<Object> list = new AdjListGraph<>(false);
        Graph<Object> incidence = new IncMatrixGraph<>(false);
        for (Graph<Object> g : List.of(matrix, list, incidence)) {
            g.readGraph(file.toString());
        }
        assertEquals(matrix, list);
        assertEquals(list, incidence);
        assertEquals(matrix.hashCode(), incidence.hashCode());
    }
}