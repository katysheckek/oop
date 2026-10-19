package maksunova.expression;

import maksunova.operation.Add;
import maksunova.operation.Div;
import maksunova.operation.Mul;
import maksunova.operation.Sub;

public class Parser {
    private String s;
    private int position;

    public Expression parse(String str){
        this.s = str;
        this.position = 0;

        s = s.replaceAll(" ", "");

        Expression node = parseAddSub();

        if (position < s.length()) {
            throw new RuntimeException("Использован неучитываемый символ: " + s.charAt(position));
        }
        return node;
    }

    private Expression parseAddSub() {
        Expression node = parseMulDiv();

        while (position < s.length()) {
            char c = s.charAt(position);

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

        while (position < s.length()) {
            char c = s.charAt(position);

            if (c == '*') {
                position++;
                node = new Mul(node, parseFirst());
            }
            else if (c == '/') {
                position++;
                node = new Div(node, parseFirst());
            } else {
                break;
            }
        }

        return node;
    }

    private Expression parseFirst() {
        if (position >= s.length()) {
            throw new RuntimeException("Указатель не указывает на строку");
        }

        char c = s.charAt(position);

        if (c == '(') {
            position++;
            Expression node  = parseAddSub();

            if (position >= s.length() || s.charAt(position) != ')') {
                throw new RuntimeException("нет закрывающей скобки");
            }
            position++;
            return node;
        }


        if (Character.isDigit(c)) {
            int val = 0;
            while (position < s.length() && Character.isDigit(s.charAt(position))) {
                val = val*10 + (s.charAt(position) - '0');
                position++;
            }
            return new Number(val);
        }

        if (Character.isLetter(c)) {
            StringBuilder strBl = new StringBuilder();
            while (position < s.length() &&
                            (Character.isLetter(s.charAt(position)) ||
                            Character.isDigit(s.charAt(position)) ||
                            s.charAt(position) == '_')) {

                strBl.append(s.charAt(position));
                position++;
            }
            return new Variable(strBl.toString());
        }

        throw new RuntimeException("!неожиданный символ: " + c);
    }
}
