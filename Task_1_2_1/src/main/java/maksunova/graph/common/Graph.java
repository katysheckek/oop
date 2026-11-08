package maksunova.graph.common;

import maksunova.graph.algorithms.TopSortAlgorithm;
import maksunova.graph.parts.Edge;
import maksunova.graph.parts.Vertex;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * интерфейс графа для реализации разных способов хранения.
 *
 * @param <T>
 */
public interface Graph<T> {

    // вершины
    /**
     * объявление метода добавьить вершину.
     *
     * @param ver добавляемая вершина
     */
    void addVertex(Vertex<T> ver);

    /**
     * объявление метода убрать вершину.
     *
     * @param ver удаляемая вершина
     */
    void delVertex(Vertex<T> ver);

    // ребра
    /**
     * объявление метода добавить ребро.
     *
     * @param source из какой вершины начинается ребро
     * @param dest в какой вершине заканчивается ребро
     */
    void addEdge(Vertex<T> source, Vertex<T> dest);

    /**
     * объявление метода удалить ребро.
     *
     * @param source из какой вершины начинается ребро
     * @param dest в какой вершине заканчивается ребро
     */
    void delEdge(Vertex<T> source, Vertex<T> dest);



    /**
     * объявление метода получить все вершины графа.
     *
     * @return список вершин графа
     */
    List<Vertex<T>> getVertices();

    /**
     * объявление метода получить все смежные вершины для передаваемой вершины.
     *
     * @param ver вершина, у которой просматриваем все смежные
     * @return список смежных вершин
     */
    List<Vertex<T>> getAdjacentVertices(Vertex<T> ver);





    /**
     * объявление метода прочитать граф из файла.
     *
     * @param filename имя файла
     * @throws IOException не удалось прочитать файл
     */
    void readGraph(String filename) throws IOException;

    /**
     * объявление метода, отвечающего за направленность ребра.
     *
     * @return ответ: направлено ли ребро?
     */
    boolean isDirected();

    /**
     * //.
     *
     * @param ver //
     * @return //
     */
    default boolean hasVertex(Vertex<T> ver) {
        return getVertices().contains(ver);
    }

    /**
     * //.
     *
     * @param edge //
     * @return //
     */
    default boolean hasEdge(Edge<T> edge) {
        return getAdjacentVertices(edge.getFrom()).contains(edge.getTo());
    }

    /**
     * //.
     *
     * @param source //
     * @param dest //
     * @return //
     */
    default boolean hasEdge(Vertex<T> source, Vertex<T> dest) {
        return getAdjacentVertices(source).contains(dest);
    }

    /**
     * топологическая сортировка для любого графа.
     *
     * @return список вершин, отсортированных топологически
     */
    default List<Vertex<T>> topSort() {
        // получаем список всх вершин
        if (!isDirected()) {
            throw new IllegalStateException("topological sort only for oriented graphs");
        }
        List<Vertex<T>> all = getVertices();

        // мапируем по ключам-вершинам их значение-степень
        Map<Vertex<T>, Integer> inDegree = new HashMap<>();
        for (Vertex<T> ver : all) {
            inDegree.put(ver, 0);
        }
        for (Vertex<T> ver : all) {
            for (Vertex<T> adjVer : getAdjacentVertices(ver)) {
                inDegree.put(adjVer, inDegree.getOrDefault(adjVer, 0) + 1);
            }
        }

        // выносим висящие вершинки в очередь
        Deque<Vertex<T>> queue = new ArrayDeque<>();
        for (Vertex<T> v : all) {
            if (inDegree.get(v) == 0) queue.add(v);
        }

        // проходимся по графу, начиная из висячих вершин
        List<Vertex<T>> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            Vertex<T> ver = queue.poll();
            result.add(ver);
            for (Vertex<T> adjVer : getAdjacentVertices(ver)) {
                // после обработки взятой по пути вершины учитываем новую степень смежжных вершин
                int newDegree = inDegree.get(adjVer) - 1;
                inDegree.put(adjVer, newDegree);

                // если из вершины идти некуда, добавляем в очередь
                if (newDegree == 0) {
                    queue.add(adjVer);
                }
            }
        }

        // если есть цикл, сортировки не будет
        if (result.size() != all.size()) {
            throw new IllegalStateException("В графе есть цикл");
        }
        return result;

    }

    /**
     * топологическая сортировка выбранным алгоритмом.
     *
     * @param algorithm алгоритм сортировки
     * @return список вершин, отсортированных топологически
     */
    default List<Vertex<T>> topSort(TopSortAlgorithm<T> algorithm) {
        return algorithm.sort(this);
    }
}
