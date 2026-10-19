package maksunova.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import maksunova.expression.Expression;
import maksunova.expression.Number;
import maksunova.expression.Variable;
import org.junit.jupiter.api.Test;

public class AddTest {
    @Test
    void testDerivative() {
        Expression add = new Add(new Variable("x"), new Number(5));
        Expression result = add.derivative("x");

        assertEquals("(1+0)", result.print());
    }

    @Test
    void testEval() {
        Expression allPositive = new Add(new Number(9), new Number(20));
        Expression posNeg = new Add(new Number(-9), new Number(20));
        Expression allNegative = new Add(new Number(-9), new Number(-20));
        Expression oneNullPoz = new Add(new Number(9), new Number(0));
        Expression oneNullNeg = new Add(new Number(-9), new Number(0));
        Expression allNull = new Add(new Number(0), new Number(0));

        assertEquals(29, allPositive.eval());
        assertEquals(11, posNeg.eval());
        assertEquals(-29, allNegative.eval());
        assertEquals(9, oneNullPoz.eval());
        assertEquals(-9, oneNullNeg.eval());
        assertEquals(0, allNull.eval());
    }

    @Test
    void testPrint() {
        Expression add = new Add(new Variable("a"), new Variable("b"));
        assertEquals("(a+b)", add.print());

        Expression ad2 = new Add(new Variable("a"), new Add(new Variable("b"), new Number(67)));
        assertEquals("(a+(b+67))", ad2.print());
    }
}
