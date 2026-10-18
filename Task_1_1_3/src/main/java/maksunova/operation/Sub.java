package maksunova.operation;

import maksunova.expression.BinaryExpression;
import maksunova.expression.Expression;

import java.util.Map;

public class Sub extends BinaryExpression {
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }
}