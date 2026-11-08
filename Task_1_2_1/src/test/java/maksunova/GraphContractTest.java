package maksunova;

import static maksunova.TestUtil.DIRECTED_FILE;
import static maksunova.TestUtil.UNDIRECTED_FILE;
import static maksunova.TestUtil.adjNames;
import static maksunova.TestUtil.assertAdjacent;
import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.find;
import static maksunova.TestUtil.names;
import static maksunova.TestUtil.tempFile;
import static maksunova.TestUtil.ver;
import static maksunova.TestUtil.vertices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import maksunova.graph.common.AbstractGraph;
import maksunova.graph.common.Graph;
import org.junit.jupiter.api.Test;

/**
 * поведение, которое обязано быть одинаковым у любого представления графа.
 * наследники только говорят, какой граф создавать.
 */
abstract class GraphContractTest {

    abstract Graph<String> create(boolean directed);

    private static void reset(Graph<String> g, boolean directed) throws Exception {
        Method m = AbstractGraph.class.getDeclaredMethod("reset", boolean.class);
        m.setAccessible(true);
        m.invoke(g, directed);
    }

    // ---------- вершины ----------

    @Test
    void emptyGraphHasNoVertices() {
        assertTrue(create(true).getVertices().isEmpty());
    }

    @Test
    void addVertexKeepsInsertionOrder() {
        Graph<String> g = create(true);
        vertices(g, "A", "B", "C");
        assertEquals(List.of("A", "B", "C"), names(g));
        assertTrue(g.hasVertex(ver("B")));
        assertFalse(g.hasVertex(ver("Z")));
    }

    @Test
    void addExistingVertexThrows() {
        Graph<String> g = create(true);
        vertices(g, "A");
        assertThrows(IllegalArgumentException.class, () -> g.addVertex(ver("A")));
        assertEquals(1, g.getVertices().size());
    }

    @Test
    void delVertexRemovesItAndItsEdges() {
        Graph<String> g = create(true);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "B->C", "C->A");
        g.delVertex(ver("B"));
        assertEquals(List.of("A", "C"), names(g));
        assertFalse(g.hasVertex(ver("B")));
        assertAdjacent(g, "A");
        assertAdjacent(g, "C", "A");
    }

    @Test
    void delVertexInTheMiddleKeepsIndexesConsistent() {
        Graph<String> g = create(true);
        vertices(g, "A", "B", "C", "D");
        edges(g, "A->D", "B->C", "C->D");
        g.delVertex(ver("B"));
        assertAdjacent(g, "A", "D");
        assertAdjacent(g, "C", "D");

        g.addVertex(ver("E"));
        g.addEdge(ver("C"), ver("E"));
        assertAdjacent(g, "C", "D", "E");
        assertAdjacent(g, "E");
        assertEquals(List.of("A", "C", "D", "E"), names(g));
    }

    @Test
    void delFirstAndLastVertex() {
        Graph<String> g = create(false);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "B->C");
        g.delVertex(ver("A"));
        g.delVertex(ver("C"));
        assertEquals(List.of("B"), names(g));
        assertAdjacent(g, "B");
    }

    @Test
    void delMissingVertexThrows() {
        Graph<String> g = create(true);
        assertThrows(IllegalArgumentException.class, () -> g.delVertex(ver("A")));
    }

    @Test
    void vertexCanBeAddedAgainAfterDeletion() {
        Graph<String> g = create(true);
        vertices(g, "A", "B");
        edges(g, "A->B");
        g.delVertex(ver("A"));
        g.addVertex(ver("A"));
        assertAdjacent(g, "A");
        assertAdjacent(g, "B");
        assertEquals(List.of("B", "A"), names(g));
    }

    // ---------- рёбра ----------

    @Test
    void directedEdgeIsOneWay() {
        Graph<String> g = create(true);
        vertices(g, "A", "B");
        edges(g, "A->B");
        assertAdjacent(g, "A", "B");
        assertAdjacent(g, "B");
        assertTrue(g.hasEdge(ver("A"), ver("B")));
        assertFalse(g.hasEdge(ver("B"), ver("A")));
    }

    @Test
    void undirectedEdgeIsSymmetric() {
        Graph<String> g = create(false);
        vertices(g, "A", "B");
        edges(g, "A->B");
        assertAdjacent(g, "A", "B");
        assertAdjacent(g, "B", "A");
        assertTrue(g.hasEdge(ver("A"), ver("B")));
        assertTrue(g.hasEdge(ver("B"), ver("A")));
    }

    @Test
    void loopEdgeWorksInBothKindsOfGraph() {
        for (boolean directed : new boolean[]{true, false}) {
            Graph<String> g = create(directed);
            vertices(g, "C");
            edges(g, "C->C");
            assertAdjacent(g, "C", "C");
            assertTrue(g.hasEdge(ver("C"), ver("C")));

            g.delEdge(ver("C"), ver("C"));
            assertAdjacent(g, "C");
            assertFalse(g.hasEdge(ver("C"), ver("C")));
        }
    }

    @Test
    void addingSameEdgeTwiceDoesNotDuplicateIt() {
        for (boolean directed : new boolean[]{true, false}) {
            Graph<String> g = create(directed);
            vertices(g, "A", "B");
            edges(g, "A->B", "A->B");
            assertAdjacent(g, "A", "B");
            assertEquals(directed ? 0 : 1, adjNames(g, "B").size());
        }
    }

    @Test
    void addEdgeWithUnknownVertexThrows() {
        Graph<String> g = create(true);
        vertices(g, "A");
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(ver("A"), ver("Z")));
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(ver("Z"), ver("A")));
        assertAdjacent(g, "A");
    }

    @Test
    void delEdgeDirectedLeavesReverseEdge() {
        Graph<String> g = create(true);
        vertices(g, "A", "B");
        edges(g, "A->B", "B->A");
        g.delEdge(ver("A"), ver("B"));
        assertAdjacent(g, "A");
        assertAdjacent(g, "B", "A");
    }

    @Test
    void delEdgeUndirectedRemovesBothDirections() {
        Graph<String> g = create(false);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "A->C");
        g.delEdge(ver("B"), ver("A"));
        assertAdjacent(g, "A", "C");
        assertAdjacent(g, "B");
        assertAdjacent(g, "C", "A");
    }

    @Test
    void delMissingEdgeChangesNothing() {
        Graph<String> g = create(true);
        vertices(g, "A", "B", "C");
        edges(g, "A->B");
        g.delEdge(ver("B"), ver("A"));
        g.delEdge(ver("A"), ver("C"));
        assertAdjacent(g, "A", "B");
    }

    @Test
    void delEdgeWithUnknownVertexThrows() {
        Graph<String> g = create(true);
        vertices(g, "A");
        assertThrows(IllegalArgumentException.class, () -> g.delEdge(ver("A"), ver("Z")));
        assertThrows(IllegalArgumentException.class, () -> g.delEdge(ver("Z"), ver("A")));
    }

    // ---------- соседи и копии ----------

    @Test
    void adjacentVerticesOfUnknownVertexThrows() {
        Graph<String> g = create(true);
        assertThrows(IllegalArgumentException.class, () -> g.getAdjacentVertices(ver("A")));
    }

    @Test
    void returnedListsAreCopies() {
        Graph<String> g = create(true);
        vertices(g, "A", "B");
        edges(g, "A->B");
        g.getVertices().clear();
        g.getAdjacentVertices(find(g, "A")).clear();
        assertEquals(2, g.getVertices().size());
        assertAdjacent(g, "A", "B");
    }

    @Test
    void isDirectedReflectsConstructor() {
        assertTrue(create(true).isDirected());
        assertFalse(create(false).isDirected());
    }

    @Test
    void biggerGraphKeepsAllEdges() {
        Graph<String> g = create(true);
        int n = 40;
        for (int i = 0; i < n; i++) {
            g.addVertex(ver("V" + i));
        }
        for (int i = 0; i + 1 < n; i++) {
            g.addEdge(ver("V" + i), ver("V" + (i + 1)));
        }
        for (int i = 0; i + 1 < n; i++) {
            assertAdjacent(g, "V" + i, "V" + (i + 1));
        }
        assertAdjacent(g, "V" + (n - 1));
    }

    // ---------- reset и чтение из файла ----------

    @Test
    void resetClearsGraphAndChangesDirection() throws Exception {
        Graph<String> g = create(true);
        vertices(g, "A", "B");
        edges(g, "A->B");
        reset(g, false);
        assertTrue(g.getVertices().isEmpty());
        assertFalse(g.isDirected());
        vertices(g, "A", "B");
        edges(g, "A->B");
        assertAdjacent(g, "B", "A");
    }

    @Test
    void readDirectedGraphFromFile() throws IOException {
        Graph<String> g = create(false);   // направленность берётся из файла, а не из конструктора
        g.readGraph(tempFile(DIRECTED_FILE).toString());
        assertTrue(g.isDirected());
        assertEquals(List.of("A", "B", "C"), names(g));
        assertEquals("data1", find(g, "A").getData());
        assertAdjacent(g, "A", "B", "C");
        assertAdjacent(g, "B", "C");
        assertAdjacent(g, "C");
    }

    @Test
    void readUndirectedGraphWithLoopFromFile() throws IOException {
        Graph<String> g = create(true);
        g.readGraph(tempFile(UNDIRECTED_FILE).toString());
        assertFalse(g.isDirected());
        assertAdjacent(g, "A", "B", "C");
        assertAdjacent(g, "B", "A", "C");
        assertAdjacent(g, "C", "A", "B", "C");
    }

    @Test
    void readGraphReplacesPreviousContent() throws IOException {
        Graph<String> g = create(false);
        vertices(g, "X", "Y");
        edges(g, "X->Y");
        g.readGraph(tempFile(DIRECTED_FILE).toString());
        assertEquals(List.of("A", "B", "C"), names(g));
        assertTrue(g.isDirected());
    }

    @Test
    void brokenFileLeavesGraphUntouched() throws IOException {
        String broken = DIRECTED_FILE.replace("A B\n", "A Z\n");
        Graph<String> graph = create(true);
        vertices(graph, "X", "Y");
        edges(graph, "X->Y");
        Path file = tempFile(broken);
        assertThrows(IOException.class, () -> graph.readGraph(file.toString()));
        assertEquals(List.of("X", "Y"), names(graph));
        assertAdjacent(graph, "X", "Y");
    }

    @Test
    void missingFileThrowsIoException() {
        Graph<String> g = create(true);
        assertThrows(IOException.class, () -> g.readGraph("no_such_file_for_graph_tests.txt"));
    }

    @Test
    void fileWithOtherDataTypeThrows() throws IOException {
        String integers = String.join("\n",
                "directed? - 1",
                "type of data? - Integer",
                "=== vertices - 1",
                "A = 5",
                "=== edges - 0",
                "===") + "\n";
        Graph<String> g = create(true);
        Path file = tempFile(integers);
        assertThrows(IOException.class, () -> g.readGraph(file.toString()));
    }
}
