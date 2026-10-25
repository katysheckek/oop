package maksunova.operation;

import maksunova.expression.BinaryExpression;
import java.util.Map;
import maksunova.expression.Expression;

/**
 * Класс операции умножения двух выражений.
 */
public class Mul extends BinaryExpression {

    /**
     * Создает новую выражение с умножением.
     *
     * @param left  левое выражение
     * @param right правое выражение
     */
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает строковое представление умножения в формате "(left*right)".
     */
    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }

    /**
     * Считает производную произведения.
     *
     * @param variable переменная, по которой берется производная
     */
    @Override
    public Expression derivative(String variable) {
        // правило произведения: (u * v)' = u' * v + u * v'
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }

    /**
     * Вычисляет результат произведения.
     *
     * @param vars карта значений переменных
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }
}