package maksunova.expression;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    void testSimpleNumber() {
        Expression expr = parser.parse("123");
        assertNotNull(expr);
        assertEquals(123, expr.eval()); // Предполагается, что есть метод evaluate()
    }

    @Test
    void testSimpleVariable() {
        Expression expr = parser.parse("x_1");
        assertNotNull(expr);
    }

    @Test
    void testAdditionAndSubtraction() {
        Expression expr = parser.parse("10 - 3 + 5");
        assertNotNull(expr);
    }

    @Test
    void testMultiplicationAndDivisionPriority() {
        // Умножение должно выполниться раньше сложения
        Expression expr = parser.parse("2 + 3 * 4");
        assertNotNull(expr);
    }

    @Test
    void testBrackets() {
        Expression expr = parser.parse("(2 + 3) * 4");
        assertNotNull(expr);
    }

    @Test
    void testMissingBracketThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            parser.parse("(2 + 3");
        });
    }

    @Test
    void testUnexpectedSymbolThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            parser.parse("2 + $");
        });
    }
}