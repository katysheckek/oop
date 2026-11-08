package maksunova.graph.parts;

import java.util.Objects;

/**
 * Представление вершины графа.
 *
 * @param <T> тип данных
 */
public class Vertex<T> {

    private final String name;
    private final T data;

    public Vertex(String name, T data) {
        this.name = name;
        this.data = data;
    }

    /**
     * метод получения имени вершины.
     *
     * @return имя вершины
     */
    public String getName() {
        return name;
    }

    /**
     * метод получения данных, записанных в вершине.
     *
     * @return данные вершины
     */
    public T getData(){
        return data;
    }

    /**
     * метод записи вершины в строке.
     *
     * @return запись вершины в строковом формате
     */
    @Override
    public String toString() {
        return name + '(' + data + ')';
    }

    /**
     * метод сравнения с вершиной.
     *
     * @param o   the reference object with which to compare
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex<?> other)) return false;
        return Objects.equals(name, other.name) && Objects.equals(data, other.data);
    }

    /**
     * метод получения хэшкода для вершины.
     *
     * @return число int
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
