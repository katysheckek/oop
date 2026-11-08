package maksunova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import maksunova.graph.common.AbstractGraph;
import maksunova.graph.parts.Vertex;

/**
 * граф в виде списка смжности.
 *
 * @param <T> дженерик для типа данных графа
 */
public class AdjListGraph<T> extends AbstractGraph<T> {

    private boolean directed;
    private Class<T> type;
    // LinkedHashMap помнит порядок добавления,
    // поэтому вершины выводятся в том же порядке, что и в матрицах
    private final Map<Vertex<T>, List<Vertex<T>>> adjacencyMap = new LinkedHashMap<>();

    public AdjListGraph(boolean directed) {
        this.directed = directed;
    }

    public AdjListGraph(boolean directed, Class<T> type) {
        this.directed = directed;
        this.type = type;
    }

    /**
     * получаем индекс по вершине-ключу.
     *
     * @param ver вершина-ключ
     * @return значение-индекс
     */
    private List<Vertex<T>> requireList(Vertex<T> ver) {
        List<Vertex<T>> list = adjacencyMap.get(ver);
        if (list == null) {
            throw new IllegalArgumentException("вершины нет в графе: " + ver);
        }
        return list;
    }

    /**
     * добавить вершину в матрицу смежности.
     *
     * @param ver добавляемая вершина
     */
    @Override
    public void addVertex(Vertex<T> ver) {
        if (adjacencyMap.containsKey(ver)) {
            throw new IllegalArgumentException("You try to add an existing vertex");
        }
        adjacencyMap.put(ver, new ArrayList<>());
    }

    /**
     * удалить вершину из матрицы смежности.
     *
     * @param ver удалямая вершина
     */
    @Override
    public void delVertex(Vertex<T> ver) {
        if (!adjacencyMap.containsKey(ver)) {
            throw new IllegalArgumentException("Попытка удалить несуществующую вершину");
        }
        adjacencyMap.remove(ver);

        // убираем вершину из списков соседей остальных вершин
        for (List<Vertex<T>> neighbours : adjacencyMap.values()) {
            neighbours.remove(ver);
        }
    }

    /**
     * добавить реюро между source и dest.
     *
     * @param source откуда выходит ребро
     * @param dest   куда входит ребро
     */
    @Override
    public void addEdge(Vertex<T> source, Vertex<T> dest) {
        List<Vertex<T>> out = requireList(source);
        List<Vertex<T>> in = requireList(dest);
        if (!out.contains(dest)) {
            out.add(dest);
        }
        if (!directed && !in.contains(source)) {
            in.add(source);
        }
    }

    /**
     * удалить ребро между source и dest.
     *
     * @param source откуда выходило ребро
     * @param dest   куда входило ребро
     */
    @Override
    public void delEdge(Vertex<T> source, Vertex<T> dest) {
        List<Vertex<T>> out = requireList(source);
        List<Vertex<T>> in = requireList(dest);
        out.remove(dest);
        if (!directed) {
            in.remove(source);
        }
    }

    /**
     * геттер всех вершин графа.
     *
     * @return список из вершин
     */
    @Override
    public List<Vertex<T>> getVertices() {
        return new ArrayList<>(adjacencyMap.keySet());
    }

    /**
     * геттер смежных вершине ver вершин.
     *
     * @param ver рассматриваемя вершина
     * @return список смежных вершин
     */
    @Override
    public List<Vertex<T>> getAdjacentVertices(Vertex ver) {
        return new ArrayList<>(requireList(ver));
    }

    /**
     * сбросить все параметры графа.
     *
     * @param directed направленность графа
     */
    @Override
    protected void reset(boolean directed) {
        this.directed = directed;
        adjacencyMap.clear();
    }

    /**
     * вспоминает тип данных у графа.
     *
     * @return ответ: какой тип данных графа?
     */
    @Override
    public Class getDataType() {
        return type;
    }

    /**
     * читает граф из файла и записывает.
     *
     * @param filename имя файла
     * @throws IOException ошибка при работе с файлом
     */
    @Override
    public void readGraph(String filename) throws IOException {
        super.readGraph(filename);
    }

    /**
     * вспоминает, ориентирован ли граф.
     *
     * @return ответ: ориентирован ли граф?
     */
    @Override
    public boolean isDirected() {
        return directed;
    }
}
