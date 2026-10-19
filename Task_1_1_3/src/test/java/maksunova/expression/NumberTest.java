package maksunova.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class NumberTest {

    @Test
    void testEvalPositiveNumber() {
        Expression num = new Number(42);
        assertEquals(42, num.eval(Collections.emptyMap()));
    }

    @Test
    void testEvalNegativeNumberWithVars() {
        Expression num = new Number(-7);
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 100);

        assertEquals(-7, num.eval(vars));
    }

    @Test
    void testDerivativeIsZero() {
        Expression num = new Number(15);
        Expression derivative = num.derivative("x");

        assertNotNull(derivative);
        assertEquals(0, derivative.eval(Collections.emptyMap()));
        assertEquals("0", derivative.print());
    }

    @Test
    void testPrintRepresentation() {
        assertEquals("0", new Number(0).print());
        assertEquals("123", new Number(123).print());
        assertEquals("-456", new Number(-456).print());
    }
}
