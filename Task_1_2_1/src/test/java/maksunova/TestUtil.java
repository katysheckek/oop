package maksunova;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;

/** общие помощники для тестов. */
final class TestUtil {

    private TestUtil() {
    }

    static final String DIRECTED_FILE = String.join("\n",
            "directed? - 1",
            "type of data? - String",
            "=== vertices - 3",
            "A = data1",
            "B = data2",
            "C = data3",
            "=== edges - 3",
            "A B",
            "A C",
            "B C",
            "===") + "\n";

    static final String UNDIRECTED_FILE = String.join("\n",
            "directed? - 0",
            "type of data? - String",
            "=== vertices - 3",
            "A = data1",
            "B = data2",
            "C = data3",
            "=== edges - 4",
            "A B",
            "A C",
            "B C",
            "C C",
            "===") + "\n";

    /** вершина с предсказуемыми данными: name -> "d" + name. */
    static Vertex<String> v(String name) {
        return new Vertex<>(name, "d" + name);
    }

    /** фабрики всех трёх представлений: аргумент - направленность. */
    static List<Function<Boolean, Graph<String>>> factories() {
        return List.of(
                d -> new AdjMatrixGraph<>(d, String.class),
                d -> new AdjListGraph<>(d, String.class),
                d -> new IncMatrixGraph<>(d, String.class));
    }

    static void vertices(Graph<String> g, String... names) {
        for (String name : names) {
            g.addVertex(v(name));
        }
    }

    /** рёбра в виде "A->B". */
    static void edges(Graph<String> g, String... pairs) {
        for (String pair : pairs) {
            String[] ab = pair.split("->");
            g.addEdge(v(ab[0]), v(ab[1]));
        }
    }

    static Vertex<String> find(Graph<String> g, String name) {
        for (Vertex<String> x : g.getVertices()) {
            if (x.getName().equals(name)) return x;
        }
        throw new AssertionError("в графе нет вершины " + name);
    }

    static List<String> names(List<Vertex<String>> vs) {
        List<String> result = new ArrayList<>();
        for (Vertex<String> x : vs) result.add(x.getName());
        return result;
    }

    static List<String> names(Graph<String> g) {
        return names(g.getVertices());
    }

    static List<String> adjNames(Graph<String> g, String from) {
        return names(g.getAdjacentVertices(find(g, from)));
    }

    /** соседи вершины совпадают с ожидаемыми (порядок не важен, повторов быть не должно). */
    static void assertAdjacent(Graph<String> g, String from, String... expected) {
        List<String> actual = adjNames(g, from);
        assertEquals(expected.length, actual.size());
        assertEquals(new HashSet<>(List.of(expected)), new HashSet<>(actual));
    }

    /** порядок удовлетворяет условию: для каждого ребра u -> w вершина u стоит раньше w. */
    static void assertTopologicalOrder(Graph<String> g, List<Vertex<String>> order) {
        assertEquals(g.getVertices().size(), order.size());
        Map<Vertex<String>, Integer> pos = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            pos.put(order.get(i), i);
        }
        for (Vertex<String> from : g.getVertices()) {
            for (Vertex<String> to : g.getAdjacentVertices(from)) {
                assertTrue(pos.get(from) < pos.get(to));
            }
        }
    }

    static Path tempFile(String content) throws IOException {
        Path path = Files.createTempFile("graph", ".txt");
        Files.writeString(path, content);
        path.toFile().deleteOnExit();
        return path;
    }
}
