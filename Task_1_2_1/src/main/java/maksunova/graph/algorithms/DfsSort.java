package maksunova.graph.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maksunova.graph.common.Graph;
import maksunova.graph.parts.Vertex;

/**
 * топологическая сортировка поиском в глубину: вершина попадает в результат
 * после обхода всех её потомков, а в конце порядок разворачивается.
 * серая вершина, встреченная повторно, означает цикл.
 *
 * @param <T> тип данных вершин
 */
public class DfsSort<T> implements TopSortAlgorithm<T> {

    private enum Color { WHITE, GRAY, BLACK }

    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        TopSortAlgorithm.requireDirected(graph);

        Map<Vertex<T>, Color> color = new HashMap<>();
        for (Vertex<T> ver : graph.getVertices()) {
            color.put(ver, Color.WHITE);
        }

        // push кладёт в начало очереди, поэтому порядок сразу получается обратный
        Deque<Vertex<T>> order = new ArrayDeque<>();
        for (Vertex<T> ver : graph.getVertices()) {
            if (color.get(ver) == Color.WHITE) {
                visit(graph, ver, color, order);
            }
        }
        return new ArrayList<>(order);
    }

    private void visit(
            Graph<T> graph, Vertex<T> ver,
            Map<Vertex<T>, Color> color,
                    Deque<Vertex<T>> order) {
        color.put(ver, Color.GRAY);
        for (Vertex<T> adjVer : graph.getAdjacentVertices(ver)) {
            Color c = color.get(adjVer);
            if (c == Color.GRAY) {
                throw new IllegalStateException("В графе есть цикл");
            }
            if (c == Color.WHITE) {
                visit(graph, adjVer, color, order);
            }
        }
        color.put(ver, Color.BLACK);
        order.push(ver);
    }
}
