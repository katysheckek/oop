package maksunova.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса {@link Variable}.
 */
public class VariableTest {

    @Test
    void testPrint() {
        Variable variable = new Variable("x");
        assertEquals("x", variable.print());
    }

    @Test
    void testEvalSuccess() {
        Variable variable = new Variable("x");
        Map<String, Integer> vars = Map.of("x", 42);

        assertEquals(42, variable.eval(vars));
    }

    @Test
    void testEvalError() {
        Variable variable = new Variable("x");
        Map<String, Integer> vars = Collections.emptyMap();

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                () -> variable.eval(vars));

        assertEquals("нет такой переменной: x", exception.getMessage());
    }

    @Test
    void testDerivativeSameVariable() {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("x");

        assertEquals("1", derivative.print());
        assertEquals(1, derivative.eval(Collections.emptyMap()));
    }

    @Test
    void testDerivativeDifferentVariable() {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("y");

        assertEquals("0", derivative.print());
        assertEquals(0, derivative.eval(Collections.emptyMap()));
    }
}
