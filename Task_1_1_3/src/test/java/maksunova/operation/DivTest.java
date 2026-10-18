package maksunova.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import maksunova.expression.Expression;
import maksunova.expression.Number;
import maksunova.expression.Variable;

import org.junit.jupiter.api.Test;



class DivTest {

    @Test
    void testEvalBasic() {
        // 10 / 2 = 5
        Expression div = new Div(new Number(10), new Number(2));
        assertEquals(5, div.eval("x"));
    }

    @Test
    void testEvalWithRemainder() {
        // 7 / 3 = 2
        Expression div = new Div(new Number(7), new Number(3));
        assertEquals(2, div.eval("x"));
    }

    @Test
    void testEvalWithVariables() {
        // x / y  при x = 20, y = 4
        Expression div = new Div(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 20);
        vars.put("y", 4);

        assertEquals(5, div.eval(vars));
    }

    @Test
    void testEvalDivisionByZero() {
        // делениe на ноль
        Expression div = new Div(new Number(10), new Number(0));
        assertThrows(ArithmeticException.class, () -> div.eval("x"));
    }

    @Test
    void testPrint() {
        Expression div = new Div(new Variable("x"), new Number(2));
        assertEquals("(x/2)", div.print());
    }

    @Test
    void testDerivative() {
        // (x/2)' = ((1*2 - x*0) / (2*2))
        Expression div = new Div(new Variable("x"), new Number(2));
        Expression derivative = div.derivative("x");

        assertEquals("(((1*2)-(x*0))/(2*2))", derivative.print());
    }
}
