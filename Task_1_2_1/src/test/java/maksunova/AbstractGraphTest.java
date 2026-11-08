package maksunova;

import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.factories;
import static maksunova.TestUtil.vertices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;
import org.junit.jupiter.api.Test;

/**
 * equals, hashCode и toString: общие для всех представлений.
 */
class AbstractGraphTest {

    private static List<Graph<String>> sameGraphInAllRepresentations(boolean directed) {
        List<Graph<String>> result = new ArrayList<>();
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(directed);
            vertices(g, "A", "B", "C");
            edges(g, "A->B", "B->C", "C->C");
            result.add(g);
        }
        return result;
    }

    @Test
    void sameGraphIsEqualInAllRepresentations() {
        for (boolean directed : new boolean[]{true, false}) {
            List<Graph<String>> graphs = sameGraphInAllRepresentations(directed);
            for (Graph<String> a : graphs) {
                for (Graph<String> b : graphs) {
                    assertEquals(a, b);
                    assertEquals(a.hashCode(), b.hashCode());
                }
            }
        }
    }

    @Test
    void equalityDoesNotDependOnInsertionOrder() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g1 = factory.apply(true);
            vertices(g1, "A", "B", "C");
            edges(g1, "A->B", "B->C");

            Graph<String> g2 = factory.apply(true);
            vertices(g2, "C", "A", "B");
            edges(g2, "B->C", "A->B");

            assertEquals(g1, g2);
            assertEquals(g1.hashCode(), g2.hashCode());
        }
    }

    @Test
    void differentEdgesMeanDifferentGraphs() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g1 = factory.apply(true);
            Graph<String> g2 = factory.apply(true);
            vertices(g1, "A", "B");
            vertices(g2, "A", "B");
            edges(g1, "A->B");
            assertNotEquals(g1, g2);
            edges(g2, "B->A");
            assertNotEquals(g1, g2);
        }
    }

    @Test
    void directedAndUndirectedGraphsAreNeverEqual() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> directed = factory.apply(true);
            Graph<String> undirected = factory.apply(false);
            for (Graph<String> g : List.of(directed, undirected)) {
                vertices(g, "A", "B");
            }
            edges(directed, "A->B");   // те же соседи, что у неориентированного
            edges(undirected, "A->B");
            assertNotEquals(directed, undirected);
        }
    }

    @Test
    void differentVertexDataMeansDifferentGraphs() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g1 = factory.apply(true);
            Graph<String> g2 = factory.apply(true);
            g1.addVertex(new Vertex<>("A", "x"));
            g2.addVertex(new Vertex<>("A", "y"));
            assertNotEquals(g1, g2);
        }
    }

    @Test
    void extraIsolatedVertexMeansDifferentGraphs() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g1 = factory.apply(true);
            Graph<String> g2 = factory.apply(true);
            vertices(g1, "A");
            vertices(g2, "A", "B");
            assertNotEquals(g1, g2);
        }
    }

    @Test
    void graphIsNotEqualToNullOrOtherTypes() {
        Graph<String> g = sameGraphInAllRepresentations(true).get(0);
        assertNotEquals(null, g);
        assertNotEquals("graph", g);
    }

    @Test
    void toStringListsVerticesAndNeighbours() {
        for (Graph<String> g : sameGraphInAllRepresentations(true)) {
            String expected = g.getClass().getSimpleName() + " (ориентированный)"
                    + "\n  A(dA) -> [B]"
                    + "\n  B(dB) -> [C]"
                    + "\n  C(dC) -> [C]";
            assertEquals(expected, g.toString());
        }
    }

    @Test
    void toStringTellsUndirectedFromDirected() {
        for (Graph<String> g : sameGraphInAllRepresentations(false)) {
            assertTrue(g.toString().contains("(неориентированный)"));
            assertFalse(g.toString().contains("(ориентированный)"));
        }
        for (Graph<String> g : sameGraphInAllRepresentations(true)) {
            assertTrue(g.toString().contains("(ориентированный)"));
        }
    }

    @Test
    void toStringOfEmptyGraphIsJustHeader() {
        IncMatrixGraph<String> g1 = new IncMatrixGraph<String>(true);
        assertEquals(g1.getClass().getSimpleName() + " (ориентированный)", g1.toString());
        AdjMatrixGraph<String> g2 = new AdjMatrixGraph<String>(true);
        assertEquals(g2.getClass().getSimpleName() + " (ориентированный)", g2.toString());
        AdjListGraph<String> g3 = new AdjListGraph<String>(true);
        assertEquals(g3.getClass().getSimpleName() + " (ориентированный)", g3.toString());
    }

}
