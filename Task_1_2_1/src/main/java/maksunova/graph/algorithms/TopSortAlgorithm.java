package maksunova.graph.algorithms;

import java.util.List;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;

/**
 * интерфейс алгоритма топологической сортировки.
 * чтобы добавить новый алгоритм, достаточно реализовать метод sort
 * и передать объект в graph.topSort(algorithm).
 *
 * @param <T> тип данных вершин
 */
public interface TopSortAlgorithm<T> {

    /**
     * отсортировать вершины графа топологически.
     *
     * @param graph граф, который сортируем
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если граф неориентированный или в нём есть цикл
     */
    List<Vertex<T>> sort(Graph<T> graph);

    /**
     * общая проверка для всех алгоритмов: сортировать можно только ориентированные графы.
     *
     * @param graph проверяемый граф
     */
    static void requireDirected(Graph<?> graph) {
        if (!graph.isDirected()) {
            throw new IllegalStateException("topological sort only for oriented graphs");
        }
    }
}

