package maksunova.expression;

import java.util.Map;

/**
 * Число в дереве выражений.
 */
public class Number extends Expression {
    private final int value;

    /**
     * Создает новое число.
     *
     * @param num значение числа
     */
    public Number(int num) {
        this.value = num;
    }

    /**
     * Возвращает строковое представление числа.
     */
    @Override
    public String print(){
        return Integer.toString(value);
    }

    /**
     * Возвращает производную (для константы всегда 0).
     *
     * @param variable имя переменной, по которой берется производная
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Вычисляет значение выражения.
     *
     * @param vars карта переменных (для константы игнорируется)
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return value;
    }
}