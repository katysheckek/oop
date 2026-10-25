package maksunova.expression;

/**
 * надстройка для операций с двумя объектами.
 */
public abstract class BinaryExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}
