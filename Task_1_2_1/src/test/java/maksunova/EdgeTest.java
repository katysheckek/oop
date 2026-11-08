package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import maksunova.graph.parts.Edge;
import maksunova.graph.parts.Vertex;
import org.junit.jupiter.api.Test;

class EdgeTest {

    private final Vertex<String> aa = new Vertex<>("A", "x");
    private final Vertex<String> bb = new Vertex<>("B", "y");

    @Test
    void gettersReturnEnds() {
        Edge<String> e = new Edge<>(aa, bb);
        assertEquals(aa, e.getFrom());
        assertEquals(bb, e.getTo());
    }

    @Test
    void equalEdgesHaveSameEnds() {
        Edge<String> e1 = new Edge<>(aa, bb);
        Edge<String> e2 = new Edge<>(new Vertex<>("A", "x"), new Vertex<>("B", "y"));
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void directionMatters() {
        assertNotEquals(new Edge<>(aa, bb), new Edge<>(bb, aa));
    }

    @Test
    void differentEndsMeanDifferentEdges() {
        assertNotEquals(new Edge<>(aa, bb), new Edge<>(aa, aa));
    }

    @Test
    void equalsIsReflexiveAndHandlesNullAndOtherTypes() {
        Edge<String> e = new Edge<>(aa, bb);
        assertTrue(e.equals(e));
        assertFalse(e.equals(null));
        assertFalse(e.equals("A -> B"));
    }

    @Test
    void stringFormats() {
        Edge<String> e = new Edge<>(aa, bb);
        assertEquals("A -> B", e.orientEdgeToString());
        assertEquals("A <-> B", e.nonorientEdgeToString());
    }
}
