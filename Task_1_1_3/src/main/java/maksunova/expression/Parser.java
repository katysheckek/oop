package maksunova.expression;

import maksunova.operation.Add;
import maksunova.operation.Div;
import maksunova.operation.Mul;
import maksunova.operation.Sub;

/**
 * класс с методом разбора строки с файла или консоли.
 */
public class Parser {
    private String str;
    private int position;

    /**
     * перерабатывает валидную строку символов в вырадение.
     *
     * @param str передаваемая строка
     * @return выражение
     */
    public Expression parse(String str) {
        this.str = str;
        this.position = 0;

        this.str = this.str.replaceAll(" ", "");

        Expression node = parseAddSub();

        if (position < this.str.length()) {
            throw new RuntimeException("Использован неучитываемый символ: "
                    + this.str.charAt(position));
        }
        return node;
    }

    private Expression parseAddSub() {
        Expression node = parseMulDiv();

        while (position < str.length()) {
            char c = str.charAt(position);

            if (c == '+') {
                position++;
                node = new Add(node, parseMulDiv());
            } else if (c == '-') {
                position++;
                node = new Sub(node, parseFirst());
            } else {
                break;
            }
        }

        return node;
    }

    private Expression parseMulDiv() {
        Expression node = parseFirst();

        while (position < str.length()) {
            char c = str.charAt(position);

            if (c == '*') {
                position++;
                node = new Mul(node, parseFirst());
            } else if (c == '/') {
                position++;
                node = new Div(node, parseFirst());
            } else {
                break;
            }
        }

        return node;
    }

    private Expression parseFirst() {
        if (position >= str.length()) {
            throw new RuntimeException("Указатель не указывает на строку");
        }

        char c = str.charAt(position);

        if (c == '(') {
            position++;
            Expression node  = parseAddSub();

            if (position >= str.length() || str.charAt(position) != ')') {
                throw new RuntimeException("нет закрывающей скобки");
            }
            position++;
            return node;
        }


        if (Character.isDigit(c)) {
            int val = 0;
            while (position < str.length() && Character.isDigit(str.charAt(position))) {
                val = val * 10 + (str.charAt(position) - '0');
                position++;
            }
            return new Number(val);
        }

        if (Character.isLetter(c)) {
            StringBuilder strBl = new StringBuilder();
            while (position < str.length()
                    && (Character.isLetter(str.charAt(position))
                    || Character.isDigit(str.charAt(position))
                    || str.charAt(position) == '_')) {
                strBl.append(str.charAt(position));
                position++;
            }
            return new Variable(strBl.toString());
        }

        throw new RuntimeException("!неожиданный символ: " + c);
    }
}
