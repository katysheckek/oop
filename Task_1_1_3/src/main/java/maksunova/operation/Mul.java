package maksunova.operation;

import maksunova.expression.BinaryExpression;
import maksunova.expression.Expression;

import java.util.Map;

public class Mul extends BinaryExpression {
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        // правило произведения: (u * v)' = u' * v + u * v'
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }
}