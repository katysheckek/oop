package maksunova;

import static maksunova.TestUtil.assertTopologicalOrder;
import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.factories;
import static maksunova.TestUtil.names;
import static maksunova.TestUtil.ver;
import static maksunova.TestUtil.vertices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import maksunova.graph.algorithms.DfsSort;
import maksunova.graph.algorithms.Khansort;
import maksunova.graph.algorithms.TopSortAlgorithm;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;
import org.junit.jupiter.api.Test;

class TopSortTest {

    private static List<TopSortAlgorithm<String>> algorithms() {
        return List.of(new Khansort<>(), new DfsSort<>());
    }

    @Test
    void chainIsSortedInItsOwnOrder() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "D", "B", "A", "C");
            edges(g, "A->B", "B->C", "C->D");
            List<String> expected = List.of("A", "B", "C", "D");
            assertEquals(expected, names(g.topSort()));
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertEquals(expected, names(g.topSort(algorithm)));
            }
        }
    }

    @Test
    void diamondGivesValidOrder() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C", "D");
            edges(g, "A->B", "A->C", "B->D", "C->D");
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertTopologicalOrder(g, g.topSort(algorithm));
            }
        }
    }

    @Test
    void disconnectedPartsAreAllIncluded() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C", "D", "E");
            edges(g, "A->B", "C->D");   // E стоит отдельно
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                List<Vertex<String>> order = g.topSort(algorithm);
                assertEquals(5, order.size());
                assertTopologicalOrder(g, order);
            }
        }
    }

    @Test
    void emptyAndSingleVertexGraphs() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> empty = factory.apply(true);
            Graph<String> single = factory.apply(true);
            vertices(single, "A");
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertTrue(empty.topSort(algorithm).isEmpty());
                assertEquals(List.of("A"), names(single.topSort(algorithm)));
            }
        }
    }

    @Test
    void cycleIsDetected() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C", "D");
            edges(g, "A->B", "B->C", "C->A", "D->A");
            assertThrows(IllegalStateException.class, () -> g.topSort());
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertThrows(IllegalStateException.class, () -> g.topSort(algorithm));
            }
        }
    }

    @Test
    void selfLoopIsaCycle() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B");
            edges(g, "A->B", "B->B");
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertThrows(IllegalStateException.class, () -> g.topSort(algorithm));
            }
        }
    }

    @Test
    void undirectedGraphCannotBeSorted() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(false);
            vertices(g, "A", "B");
            edges(g, "A->B");
            assertThrows(IllegalStateException.class, () -> g.topSort());
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                assertThrows(IllegalStateException.class, () -> g.topSort(algorithm));
            }
        }
    }

    @Test
    void defaultTopSortIsKahn() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C", "D");
            edges(g, "A->B", "A->C", "B->D", "C->D");
            assertEquals(g.topSort(), g.topSort(new Khansort<>()));
        }
    }

    @Test
    void sortingDoesNotChangeTheGraph() {
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C");
            edges(g, "A->B", "B->C");
            String before = g.toString();
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                g.topSort(algorithm);
            }
            assertEquals(before, g.toString());
        }
    }

    @Test
    void newAlgorithmCanBePluggedInWithoutChangingTheGraph() {
        TopSortAlgorithm<String> reversed = graph -> {
            List<Vertex<String>> result = new ArrayList<>(graph.getVertices());
            Collections.reverse(result);
            return result;
        };
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            vertices(g, "A", "B", "C");
            assertEquals(List.of("C", "B", "A"), names(g.topSort(reversed)));
        }
    }

    @Test
    void longChainWorksForEveryAlgorithm() {
        int n = 300;
        for (Function<Boolean, Graph<String>> factory : factories()) {
            Graph<String> g = factory.apply(true);
            for (int i = 0; i < n; i++) {
                g.addVertex(ver("V" + i));
            }
            for (int i = 0; i + 1 < n; i++) {
                g.addEdge(ver("V" + i), ver("V" + (i + 1)));
            }
            for (TopSortAlgorithm<String> algorithm : algorithms()) {
                List<String> order = names(g.topSort(algorithm));
                assertEquals("V0", order.get(0));
                assertEquals("V" + (n - 1), order.get(n - 1));
            }
        }
    }

    @Test
    void randomAcyclicGraphsAreSortedCorrectly() {
        Random rnd = new Random(42);
        for (int round = 0; round < 200; round++) {
            for (Function<Boolean, Graph<String>> factory : factories()) {
                Graph<String> g = factory.apply(true);
                int n = 7;
                for (int i = 0; i < n; i++) {
                    g.addVertex(ver("V" + i));
                }
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        if (rnd.nextInt(3) == 0) {
                            g.addEdge(ver("V" + i), ver("V" + j));
                        }
                    }
                }
                for (TopSortAlgorithm<String> algorithm : algorithms()) {
                    assertTopologicalOrder(g, g.topSort(algorithm));
                }
            }
        }
    }

    @Test
    void algorithmsAgreeAboutCycles() {
        Random rnd = new Random(7);
        for (int round = 0; round < 300; round++) {
            Graph<String> g = new AdjListGraph<>(true, String.class);
            int n = 6;
            for (int i = 0; i < n; i++) {
                g.addVertex(ver("V" + i));
            }
            for (int k = 0; k < 7; k++) {
                g.addEdge(ver("V" + rnd.nextInt(n)), ver("V" + rnd.nextInt(n)));
            }
            boolean kahnFails = fails(g, new Khansort<>());
            boolean dfsFails = fails(g, new DfsSort<>());
            assertEquals(kahnFails, dfsFails);
        }
    }

    private static boolean fails(Graph<String> g, TopSortAlgorithm<String> algorithm) {
        try {
            assertTopologicalOrder(g, g.topSort(algorithm));
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
