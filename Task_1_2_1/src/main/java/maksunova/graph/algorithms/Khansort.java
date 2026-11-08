package maksunova.graph.algorithms;

import java.util.List;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;

/**
 * сортировка кана.
 * использует дефолтную сортировку интерфейса графа.
 *
 * @param <T> дженерик типа данных графа
 */
public class Khansort<T> implements TopSortAlgorithm<T> {
    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        return graph.topSort();
    }
}
