package maksunova.expression;

import java.util.Map;

/**
 * Представление переменной.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Создает новую переменную с заданным именем.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает строковое представление переменной.
     *
     * @return имя переменной
     */
    @Override
    public String print() {
        return name;
    }

    /**
     * Вычисляет производную переменной по заданной переменной.
     *
     * @param variable имя переменной, по которой берется производная
     * @return 1, если имена совпадают, иначе 0
     */
    @Override
    public Expression derivative(String variable) {
        if (name.equals(variable)) {
            return new Number(1);
        }
        else {
            return new Number(0);
        }
    }

    /**
     * Вычисляет значение переменной на основе переданных значений.
     *
     * @param vars карта со значениями переменных
     * @return значение переменной
     * @throws RuntimeException если переменная отсутствует в карте
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        if (vars.containsKey(name)) {
            return vars.get(name);
        }
        throw new RuntimeException("нет такой переменной: " + name);
    }
}