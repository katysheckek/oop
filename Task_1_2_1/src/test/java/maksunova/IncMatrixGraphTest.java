package maksunova;

import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.DIRECTED_FILE;
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

class IncMatrixGraphTest extends GraphContractTest {

    @Override
    Graph<String> create(boolean directed) {
        return new IncMatrixGraph<>(directed, String.class);
    }

    /** рёбра по порядку добавления: e0 = A->B, e1 = A->C, e2 = B->C, e3 = C->C. */
    private IncMatrixGraph<String> sample(boolean directed) {
        IncMatrixGraph<String> g = new IncMatrixGraph<>(directed, String.class);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "A->C", "B->C", "C->C");
        return g;
    }

    @Test
    void emptyMatrix() {
        assertArrayEquals(new int[0][0], new IncMatrixGraph<String>(true).toMatrix());
    }

    @Test
    void verticesWithoutEdgesGiveRowsWithoutColumns() {
        IncMatrixGraph<String> g = new IncMatrixGraph<>(true, String.class);
        vertices(g, "A", "B");
        assertArrayEquals(new int[][]{{}, {}}, g.toMatrix());
    }

    @Test
    void directedMatrixHasPlusOneAtSourceAndMinusOneAtTarget() {
        assertArrayEquals(new int[][]{
                {1, 1, 0, 0},
                {-1, 0, 1, 0},
                {0, -1, -1, 2}}, sample(true).toMatrix());
    }

    @Test
    void undirectedMatrixHasOnesAtBothEnds() {
        assertArrayEquals(new int[][]{
                {1, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 1, 2}}, sample(false).toMatrix());
    }

    @Test
    void matrixAfterDeletingEdge() {
        IncMatrixGraph<String> g = sample(true);
        g.delEdge(v("A"), v("C"));
        assertArrayEquals(new int[][]{
                {1, 0, 0},
                {-1, 1, 0},
                {0, -1, 2}}, g.toMatrix());
    }

    @Test
    void deletingVertexRemovesItsColumns() {
        IncMatrixGraph<String> g = sample(true);
        g.delVertex(v("B"));   // уходят e0 и e2, остаются A->C и C->C
        assertArrayEquals(new int[][]{
                {1, 0},
                {-1, 2}}, g.toMatrix());
    }

    @Test
    void addingVertexAddsZeroRow() {
        IncMatrixGraph<String> g = sample(true);
        g.addVertex(v("D"));
        assertArrayEquals(new int[]{0, 0, 0, 0}, g.toMatrix()[3]);
    }

    @Test
    void toMatrixReturnsCopy() {
        IncMatrixGraph<String> g = sample(true);
        g.toMatrix()[0][0] = 9;
        assertEquals(1, g.toMatrix()[0][0]);
    }

    @Test
    void edgesInBothDirectionsAreDifferentColumnsInDirectedGraph() {
        IncMatrixGraph<String> g = new IncMatrixGraph<>(true, String.class);
        vertices(g, "A", "B");
        edges(g, "A->B", "B->A");
        assertArrayEquals(new int[][]{{1, -1}, {-1, 1}}, g.toMatrix());
    }

    @Test
    void dataTypeIsKnownOnlyWhenPassedToConstructor() {
        assertEquals(String.class, ((AbstractGraph<String>) create(true)).getDataType());
        assertNull(new IncMatrixGraph<String>(true).getDataType());
    }

    @Test
    void constructorWithoutTypeAcceptsAnyTypeFromFile() throws IOException {
        IncMatrixGraph<Object> g = new IncMatrixGraph<>(false);
        g.readGraph(tempFile(DIRECTED_FILE).toString());
        assertTrue(g.isDirected());
        assertEquals("data1", g.getVertices().get(0).getData());
    }
}
