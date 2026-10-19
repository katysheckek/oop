package maksunova.operation;

import maksunova.expression.BinaryExpression;
import maksunova.expression.Expression;

import java.util.Map;

/**
 * Операция вычитания одного выражения из другого.
 */
public class Sub extends BinaryExpression {
    /**
     * Создает новое выражение вычитания.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает строковое представление разности в скобках.
     */
    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }

    /**
     * Возвращает разность производных.
     *
     * @param variable имя переменной, по которой ищем производную
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Вычисляет числовое значение разности.
     *
     * @param vars карта со значениями переменных
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }
}