package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import maksunova.expression.Expression;
import maksunova.expression.Number;
import maksunova.expression.Variable;
import maksunova.operation.Add;
import maksunova.operation.Mul;
import org.junit.jupiter.api.Test;

/**
 * Набор юнит-тестов для проверки работы математических выражений.
 */
public class MainTest {

    /**
     * Конструктор по умолчанию для класса тестов.
     */
    public MainTest() {
    }

    /**
     * Проверяет корректность строкового представления выражения (печать).
     */
    @Test
    public void testPrintExpression() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        assertEquals("(3+(2*x))", e.print());
    }

    /**
     * Проверяет правильность вычисления значения выражения с подстановкой переменных.
     */
    @Test
    public void testEvaluateExpression() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        int result = e.eval("x = 10");
        assertEquals(23, result);
    }

    /**
     * Проверяет корректность символьного дифференцирования выражения.
     */
    @Test
    public void testDerivativeExpression() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        Expression de = e.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", de.print());
    }
}