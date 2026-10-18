package maksunova.operation;

import static org.junit.jupiter.api.Assertions.*;

import maksunova.expression.Expression;
import maksunova.expression.Number;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class SubTest {
    @Test
    void testPrint() {
        Expression left = new Number(5);
        Expression right = new Number(3);
        Sub sub = new Sub(left, right);

        assertEquals("(5-3)", sub.print());
    }

    @Test
    void testEval() {
        Expression left = new Number(10);
        Expression right = new Number(4);
        Sub sub = new Sub(left, right);

        assertEquals(6, sub.eval(new HashMap<>()));
    }

    @Test
    void testDerivative() {
        Expression left = new Number(0);
        Expression right = new Number(5);
        Sub sub = new Sub(left, right);

        Expression deriv = sub.derivative("x");
        assertNotNull(deriv);
        assertTrue(deriv instanceof Sub);
    }
}
