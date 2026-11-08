package maksunova.graph.common;

import maksunova.graph.getting.GraphReader;
import maksunova.graph.parts.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * общая часть всех представлений графа: чтение из файла, сравнение и вывод в строку.
 * всё работает только через методы интерфейса Graph, поэтому новое представление
 * получает readGraph, equals, hashCode и toString без дополнительного кода.
 * <p>
 * два графа равны, если они одинаково направлены и содержат одни и те же вершины
 * (имя и данные) и одни и те же рёбра. способ хранения не важен: граф в матрице
 * смежности может быть равен такому же графу в списке смежности.
 *
 * @param <T> тип данных вершин
 */
public abstract class AbstractGraph<T> implements Graph<T> {

    /**
     * очистить граф и задать направленность (вызывается перед чтением из файла).
     *
     * @param directed направленность графа
     */
    protected abstract void reset(boolean directed);

    /**
     * тип данных вершин, нужен для проверки типа при чтении из файла.
     *
     * @return класс данных или null, если тип не задан (тогда принимается любой тип из файла)
     */
    public abstract Class<T> getDataType();

    /**
     * прочитать граф из файла. сначала файл целиком читается и проверяется,
     * и только потом граф меняется, поэтому битый файл не портит текущий граф.
     *
     * @param filename имя файла
     * @throws IOException не удалось прочитать файл или формат нарушен
     */
    @Override
    public void readGraph(String filename) throws IOException {
        GraphReader.GraphData data = GraphReader.readfileGraph(filename);

        Class<T> type = getDataType();
        if (type != null && !type.getSimpleName().equalsIgnoreCase(data.typeName())) {
            throw new IOException("Data type in file is " + data.typeName()
                    + ", but the graph expects " + type.getSimpleName());
        }

        // вершины: имя -> объект Vertex (LinkedHashMap сохраняет порядок из файла)
        Map<String, Vertex<T>> byName = new LinkedHashMap<>();
        for (GraphReader.VertexRec rec : data.vertices()) {
            if (byName.containsKey(rec.name())) {
                throw new IOException("Duplicate vertex name: " + rec.name());
            }
            T value;
            if (type != null) {
                try {
                    value = type.cast(rec.data());
                } catch (ClassCastException e) {
                    throw new IOException("Bad data for vertex " + rec.name() + ": " + rec.data(), e);
                }
            } else {
                value = (T) rec.data();
            }
            byName.put(rec.name(), new Vertex<>(rec.name(), value));
        }

        // рёбра проверяем до reset, чтобы ошибка не оставила граф наполовину заполненным
        for (GraphReader.EdgeRec rec : data.edges()) {
            if (!byName.containsKey(rec.from()) || !byName.containsKey(rec.to())) {
                throw new IOException("Edge refers to an unknown vertex: " + rec.from() + " -> " + rec.to());
            }
        }

        reset(data.directed());
        for (Vertex<T> ver : byName.values()) {
            addVertex(ver);
        }
        for (GraphReader.EdgeRec rec : data.edges()) {
            addEdge(byName.get(rec.from()), byName.get(rec.to()));
        }
    }

    /**
     * метод сравнения графов.
     *
     * @param o the reference object with which to compare.
     * @return ответ: равны ли графы?
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractGraph<?> other)) return false;
        return isDirected() == other.isDirected()
                && adjacency(this).equals(adjacency(other));
    }

    /**
     * метод получения хэшкода для графа.
     *
     * @return число int
     */
    @Override
    public int hashCode() {
        return Objects.hash(isDirected(), adjacency(this));
    }

    /**
     * метод записи графа в строке.
     *
     * @return запись графа в строковом формате
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName())
                .append(isDirected() ? " (ориентированный)" : " (неориентированный)");
        for (Vertex<T> ver : getVertices()) {
            List<String> names = new ArrayList<>();
            for (Vertex<T> adjVer : getAdjacentVertices(ver)) {
                names.add(adjVer.getName());
            }
            sb.append("\n  ").append(ver).append(" -> ").append(names);
        }
        return sb.toString();
    }

    /** вершина -> множество её соседей (не зависит ни от порядка, ни от способа хранения). */
    private static <U> Map<Vertex<U>, Set<Vertex<U>>> adjacency(Graph<U> graph) {
        Map<Vertex<U>, Set<Vertex<U>>> result = new HashMap<>();
        for (Vertex<U> ver : graph.getVertices()) {
            result.put(ver, new HashSet<>(graph.getAdjacentVertices(ver)));
        }
        return result;
    }
}

