package maksunova.graph.parts;

import java.util.Objects;

/**
 * представление отношения связи между вершинами(ребра).
 *
 * @param <T> дженерик для типа данных вершин
 */
public class Edge<T> {

    private final Vertex<T> from;
    private final Vertex<T> to;

    public Edge(Vertex<T> from, Vertex<T> to) {
        this.from = from;
        this.to = to;
    }

    /**
     * геттер для вершины из еоторой ребро начинается.
     *
     * @return начальную вершину
     */
    public Vertex<T> getTo() {
        return to;
    }

    /**
     * геттер для вершины в которую ребро идет.
     *
     * @return конечную вершину
     */
    public Vertex<T> getFrom() {
        return from;
    }

    /**
     * метод получения хэшкода для ребра.
     *
     * @return число int
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    /**
     * метод сравнения ребер.
     *
     * @param o the reference object with which to compare.
     * @return ответ: равны ли ребра?
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge<?> other)) {
            return false;
        }
        return from.equals(other.from)
                && to.equals(other.to);
    }

    /**
     * метод для вывода орребра в строковом формате.
     *
     * @return связь между вершинами
     */
    public String orientEdgeToString() {
        return from.getName() + " -> " + to.getName();
    }

    /**
     * метод для вывода неорребра в строковом формате.
     *
     * @return связь между вершинами
     */
    public String nonorientEdgeToString() {
        return from.getName() + " <-> " + to.getName();
    }

}
