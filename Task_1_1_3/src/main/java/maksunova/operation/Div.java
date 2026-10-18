package maksunova.operation;

import maksunova.expression.BinaryExpression;
import maksunova.expression.Expression;

import java.util.Map;

/**
 * Выражение для операции деления двух подвыражений.
 */
public class Div extends BinaryExpression {
    /**
     * Создает новое выражение деления.
     *
     * @param left  делимое выражение
     * @param right делитель выражение
     */
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        // Правило частного: (u / v)' = (u' * v - u * v') / (v * v)
        return new Div(
                new Sub(
                        new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))
                ),
                new Mul(right, right)
        );
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        int r = right.eval(vars);
        if (r == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return left.eval(vars) / r;
    }
}