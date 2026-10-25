package maksunova.expression;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный класс, представляющий математическое выражение.
 */
public abstract class Expression {

    /**
     * Возвращает текстовое представление выражения.
     *
     * @return строка с выражением
     */
    public abstract String print();

    /**
     * Вычисляет значение выражения, подставляя значения переменных из карты.
     *
     * @param vars карта, где ключ - имя переменной, имеющий числовой эквивалент
     * @return результат вычисления
     */
    public abstract int eval(Map<String, Integer> vars);

    /**
     * Вычисляет значение выражения, если переменных нет.
     *
     * @return результат вычисления
     */
    public int eval() {
        return eval(java.util.Collections.emptyMap());
    }

    /**
     * Вычисляет значение выражения, принимая строку с присваиваниями.
     * Пример строки: "x=5; y=10"
     *
     * @param assignVars строка с переменными и их значениями, разделенными точкой с запятой
     * @return результат вычисления
     */
    public int eval(String assignVars) {
        Map<String, Integer> vars = new HashMap<>();

        if (assignVars != null && !assignVars.isEmpty()) {
            String[] pairs = assignVars.split(";");

            for (String pair : pairs) {
                String[] values = pair.split("=");
                if (values.length == 2) {
                    String key = values[0].trim();
                    String valStr = values[1].trim();

                    if (isValidIdentifier(key) && isInteger(valStr)) {
                        try {
                            int value = Integer.parseInt(valStr);
                            vars.put(key, value);
                        } catch (NumberFormatException ignored) {
                            continue;
                        }
                    } else {
                        throw new RuntimeException("Невалидное имя переменной");
                    }
                }
            }
        }
        return eval(vars);
    }

    /**
     * Находит производную выражения по заданной переменной.
     *
     * @param variable имя переменной, по которой берется производная
     * @return производная
     */
    public abstract Expression derivative(String variable);

    /**
     * Проверяет, является ли строка корректным именем переменной.
     */
    private static boolean isValidIdentifier(String s) {
        if (s.isEmpty()) {
            return false;
        }

        char c0 = s.charAt(0);

        if (!(Character.isLetter(c0) || c0 == '_')) {
            return false;
        }

        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, является ли строка целым числом.
     */
    private static boolean isInteger(String s) {
        if (s.isEmpty()) {
            return false;
        }
        int start = (s.charAt(0) == '+' || s.charAt(0) == '-') ? 1 : 0;
        if (start == s.length()) {
            return false;
        }

        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

