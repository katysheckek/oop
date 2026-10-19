package maksunova.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import maksunova.expression.Expression;
import maksunova.expression.Variable;
import maksunova.expression.Number;
import org.junit.jupiter.api.Test;
import java.util.Map;

class MulTest {

    @Test
    void testPrint() {
        // (2 * x)
        Mul mul = new Mul(new Number(2), new Variable("x"));
        assertEquals("(2*x)", mul.print());
    }

    @Test
    void testEval() {
        // 3 * 4 = 12
        Mul mul = new Mul(new Number(3), new Number(4)); // 3*4
        assertEquals(12, mul.eval());

        mul = new Mul(new Number(-3), new Number(4));    // -3*4
        assertEquals(-12, mul.eval());

        mul = new Mul(new Number(3), new Number(0));    // 3*0
        assertEquals(0, mul.eval());

        mul = new Mul(new Number(-3), new Number(0));    // -3*0
        assertEquals(0, mul.eval());

        mul = new Mul(new Number(-3), new Number(-4));    // -3*-4
        assertEquals(12, mul.eval());

        // 2 * x, где x = 5 -> 10
        Mul mulWithVar = new Mul(new Number(2), new Variable("x"));
        assertEquals(10, mulWithVar.eval(Map.of("x", 5)));
    }

    @Test
    void testDerivative() {
        // (x * 3)' по x -> (x' * 3) + (x * 3')
        Mul mul = new Mul(new Variable("x"), new Number(3));
        Expression deriv = mul.derivative("x");

        // Проверяем текстовый вид полученной производной: ((1*3)+(x*0))
        assertEquals("((1*3)+(x*0))", deriv.print());
    }
}