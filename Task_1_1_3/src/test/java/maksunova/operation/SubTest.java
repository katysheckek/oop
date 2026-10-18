package maksunova.operation;

import static org.junit.jupiter.api.Assertions.*;

import maksunova.expression.Expression;
import maksunova.expression.Number;
import maksunova.expression.Variable;
import org.junit.jupiter.api.Test;

public class SubTest {
    @Test
    void testPrint() {
        Expression sub = new Sub(new Number(5), new Number(3));
        assertEquals("(5-3)", sub.print());
    }

    @Test
    void testEval() {
        Expression sub = new Sub(new Number(10), new Number(4));
        assertEquals(6, sub.eval());
        sub = new Sub(new Number(10), new Number(-4));
        assertEquals(14, sub.eval());
        sub = new Sub(new Number(-10), new Number(4));
        assertEquals(-14, sub.eval());
        sub = new Sub(new Number(-10), new Number(-4));
        assertEquals(-6, sub.eval());
        sub = new Sub(new Number(10), new Number(0));
        assertEquals(10, sub.eval());
        sub = new Sub(new Number(0), new Number(10));
        assertEquals(-10, sub.eval());
    }

    @Test
    void testDerivative() {
        Expression sub = new Sub(new Variable("x"), new Number(5));
        Expression deriv = sub.derivative("x");

        assertEquals("(1-0)", deriv.print());
    }
}
