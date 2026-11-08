package maksunova.graph.algorithms;

import java.util.List;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;

public class Khansort<T> implements TopSortAlgorithm<T> {
    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        return graph.topSort();
    }
}
