package maksunova;

import maksunova.graph.parts.Vertex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexTest {

    @Test
    void gettersReturnConstructorArguments() {
        Vertex<Integer> v = new Vertex<>("A", 42);
        assertEquals("A", v.getName());
        assertEquals(Integer.valueOf(42), v.getData());
    }

    @Test
    void dataMayBeNull() {
        Vertex<String> v = new Vertex<>("A", null);
        assertNull(v.getData());
        assertEquals("A(null)", v.toString());
    }

    @Test
    void toStringShowsNameAndData() {
        assertEquals("A(data1)", new Vertex<>("A", "data1").toString());
        assertEquals("B(7)", new Vertex<>("B", 7).toString());
    }

    @Test
    void equalVerticesHaveSameNameAndData() {
        Vertex<String> a1 = new Vertex<>("A", "x");
        Vertex<String> a2 = new Vertex<>("A", "x");
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void differentNameOrDataMeansDifferentVertices() {
        Vertex<String> a = new Vertex<>("A", "x");
        assertNotEquals(new Vertex<>("B", "x"), a);
        assertNotEquals(new Vertex<>("A", "y"), a);
        assertNotEquals(new Vertex<>("A", null), a);
    }

    @Test
    void equalsIsReflexiveAndHandlesNullAndOtherTypes() {
        Vertex<String> a = new Vertex<>("A", "x");
        assertNotNull(a);
        assertNotEquals("A", a);
    }

    @Test
    void nullDataVerticesAreComparedSafely() {
        assertEquals(new Vertex<String>("A", null).hashCode(), new Vertex<String>("A", null).hashCode());
    }
}
