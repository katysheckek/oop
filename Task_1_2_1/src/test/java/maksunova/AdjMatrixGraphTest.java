package maksunova;

import static maksunova.TestUtil.DIRECTED_FILE;
import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.tempFile;
import static maksunova.TestUtil.v;
import static maksunova.TestUtil.vertices;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import maksunova.graph.common.AbstractGraph;
import maksunova.graph.common.Graph;
import org.junit.jupiter.api.Test;

class AdjMatrixGraphTest extends GraphContractTest {

    @Override
    Graph<String> create(boolean directed) {
        return new AdjMatrixGraph<>(directed, String.class);
    }

    private AdjMatrixGraph<String> sample(boolean directed) {
        AdjMatrixGraph<String> g = new AdjMatrixGraph<>(directed, String.class);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "A->C", "B->C", "C->C");
        return g;
    }

    @Test
    void emptyMatrix() {
        assertArrayEquals(new int[0][0], new AdjMatrixGraph<String>(true).toMatrix());
    }

    @Test
    void matrixOfDirectedGraph() {
        assertArrayEquals(new int[][]{{0, 1, 1}, {0, 0, 1}, {0, 0, 1}}, sample(true).toMatrix());
    }

    @Test
    void matrixOfUndirectedGraphIsSymmetric() {
        assertArrayEquals(new int[][]{{0, 1, 1}, {1, 0, 1}, {1, 1, 1}}, sample(false).toMatrix());
    }

    @Test
    void matrixAfterDeletingVertex() {
        AdjMatrixGraph<String> g = sample(true);
        g.delVertex(v("B"));
        assertArrayEquals(new int[][]{{0, 1}, {0, 1}}, g.toMatrix());
    }

    @Test
    void matrixAfterDeletingEdge() {
        AdjMatrixGraph<String> g = sample(true);
        g.delEdge(v("A"), v("C"));
        assertArrayEquals(new int[][]{{0, 1, 0}, {0, 0, 1}, {0, 0, 1}}, g.toMatrix());
    }

    @Test
    void toMatrixReturnsCopy() {
        AdjMatrixGraph<String> g = sample(true);
        g.toMatrix()[0][1] = 5;
        assertEquals(5, g.toMatrix()[0][1]);
    }

    @Test
    void dataTypeIsKnownOnlyWhenPassedToConstructor() {
        assertEquals(String.class, ((AbstractGraph<String>) create(true)).getDataType());
        assertNull(new AdjMatrixGraph<String>(true).getDataType());
    }

    @Test
    void constructorWithoutTypeAcceptsAnyTypeFromFile() throws IOException {
        AdjMatrixGraph<Object> g = new AdjMatrixGraph<>(false);
        g.readGraph(tempFile(DIRECTED_FILE).toString());
        assertTrue(g.isDirected());
        assertEquals("data1", g.getVertices().get(0).getData());
    }
}
