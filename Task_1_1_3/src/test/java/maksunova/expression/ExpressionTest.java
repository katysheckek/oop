package maksunova.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тест для класса Expression.
 */
public class ExpressionTest {

    @Test
    void testEvalEmpty() {
        Expression expr = new Number(42);
        assertEquals(42, expr.eval());
    }

    @Test
    void testEvalStringValid() {
        Expression expr = new Variable("x");

        // Проверяем парсинг строки вида "x=10; y=20"
        assertEquals(10, expr.eval("x=10; y=20"));
    }

    @Test
    void testEvalStringWithSpacesAndSigns() {
        Expression expr = new Variable("x");

        // знаки у чисел
        assertEquals(-5, expr.eval("  x = -5 ; y = +10 "));
    }

    @Test
    void testEvalStringInvalidIdentifiersOrValues() {
        Expression expr = new Variable("x");

        // Некорректное имя переменной
        assertThrows(RuntimeException.class, () -> expr.eval("123abc=10; x=abc"));
    }

    @Test
    void testEvalStringNullOrEmpty() {
        Expression expr = new Number(7);
        assertEquals(7, expr.eval());
        assertEquals(7, expr.eval(""));
    }

    @Test
    void testEvalStringPairWithoutEquals() {
        // пара без '=' — пропускается
        Expression expr = new Variable("x");
        assertThrows(RuntimeException.class, () -> expr.eval("x")); // x не задан
    }

    @Test
    void testEvalStringEmptyKey() {
        Expression expr = new Variable("x");
        assertThrows(RuntimeException.class, () -> expr.eval("=5"));
    }

    @Test
    void testEvalStringOnlySign() {
        Expression expr = new Variable("x");
        assertThrows(RuntimeException.class, () -> expr.eval("x=+"));
    }

    @Test
    void testEvalStringUnderscoreStart() {
        Expression expr = new Variable("_x");
        assertEquals(3, expr.eval("_x=3"));
    }

    @Test
    void testEvalStringKeyStartsWithDigit() {
        Expression expr = new Variable("x");
        assertThrows(RuntimeException.class, () -> expr.eval("1x=5"));
    }
}
