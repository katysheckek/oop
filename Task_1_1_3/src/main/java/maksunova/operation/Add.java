package maksunova.operation;

import maksunova.expression.BinaryExpression;
import java.util.Map;
import maksunova.expression.Expression;

/**
 * Представляет операцию сложения двух выражений.
 */
public class Add extends BinaryExpression {
    /**
     * Конструирует новое выражение сложения.
     *
     * @param left  левое выражение
     * @param right правое выражение
     */
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает строковое представление сложения.
     *
     * @return отформатированная строка выражения
     */
    @Override
    public String print() {
        return "(" + left.print() + "+" + right.print() + ")";
    }

    /**
     * Вычисляет производную сложения, которая равна сумме производных обоих операндов.
     *
     * @param variable переменная, по которой дифференцируем
     * @return новое выражение {@code Add}, представляющее производную
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Вычисляет значение сложения результатов левого и правого выражений.
     *
     * @param vars карта имен переменных и их значений
     * @return результат сложения
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) + right.eval(vars);
    }


}

