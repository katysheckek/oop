package maksunova;

import static maksunova.TestUtil.DIRECTED_FILE;
import static maksunova.TestUtil.adjNames;
import static maksunova.TestUtil.edges;
import static maksunova.TestUtil.tempFile;
import static maksunova.TestUtil.ver;
import static maksunova.TestUtil.vertices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import maksunova.graph.common.AbstractGraph;
import maksunova.graph.common.Graph;
import org.junit.jupiter.api.Test;

class AdjListGraphTest extends GraphContractTest {

    @Override
    Graph<String> create(boolean directed) {
        return new AdjListGraph<>(directed, String.class);
    }

    @Test
    void neighboursKeepInsertionOrder() {
        Graph<String> g = create(true);
        vertices(g, "A", "B", "C");
        edges(g, "A->C", "A->B");
        assertEquals(List.of("C", "B"), adjNames(g, "A"));
    }

    @Test
    void deletedVertexDisappearsFromEveryNeighbourList() {
        Graph<String> g = create(false);
        vertices(g, "A", "B", "C");
        edges(g, "A->B", "C->B", "A->C");
        g.delVertex(ver("B"));
        assertEquals(List.of("C"), adjNames(g, "A"));
        assertEquals(List.of("A"), adjNames(g, "C"));
    }

    @Test
    void dataTypeIsKnownOnlyWhenPassedToConstructor() {
        assertEquals(String.class, ((AbstractGraph<String>) create(true)).getDataType());
        assertNull(new AdjListGraph<String>(true).getDataType());
    }

    @Test
    void constructorWithoutTypeAcceptsAnyTypeFromFile() throws IOException {
        AdjListGraph<Object> g = new AdjListGraph<>(false);
        g.readGraph(tempFile(DIRECTED_FILE).toString());
        assertTrue(g.isDirected());
        assertEquals("data1", g.getVertices().get(0).getData());
    }
}
