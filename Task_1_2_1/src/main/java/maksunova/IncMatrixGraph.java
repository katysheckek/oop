package maksunova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maksunova.graph.common.AbstractGraph;
import maksunova.graph.parts.Vertex;

/**
 * матрица инцидентности, йоу.
 *
 * @param <T> дженерик для типа данных
 */
public class IncMatrixGraph<T> extends AbstractGraph<T> {

    private final List<Vertex<T>> vertices = new ArrayList<>();
    private final Map<Vertex<T>, Integer> vertexIntegerHashMap = new HashMap<>();

    private int[][] incMatrix = new int[0][0];
    private boolean directed;
    private Class<T> type;
    private int nodes;
    private int edges;

    public IncMatrixGraph(boolean directed) {
        this.directed = directed;
    }

    public IncMatrixGraph(boolean directed, Class<T> type) {
        this.directed = directed;
        this.type = type;
    }

    /**
     * получаем индекс по вершине-ключу.
     *
     * @param ver вершина-ключ
     * @return значение-индекс
     */
    private int requireIndex(Vertex<T> ver) {
        Integer idx = vertexIntegerHashMap.get(ver);
        if (idx == null) {
            throw new IllegalArgumentException("there is no vertex: " + ver);
        }
        return idx;
    }

    /**
     * номер столбца ребра i -> j или -1, если такого ребра нет.
     *
     * @param fromVertex из какой вершины ребро
     * @param toVertex в какую вершину ребро
     * @return индекс столбца. если ребра нет, -1
     */
    private int findEdge(int fromVertex, int toVertex) {
        for (int column = 0; column < edges; column++) {
            boolean match;
            if (fromVertex == toVertex) {
                match = incMatrix[fromVertex][column] == 2;
            } else if (directed) {
                match = incMatrix[fromVertex][column] == 1 && incMatrix[toVertex][column] == -1;
            } else {
                match = incMatrix[fromVertex][column] == 1 && incMatrix[toVertex][column] == 1;
            }
            if (match) return column;
        }
        return -1;
    }

    /**
     * добавить вершину в матрицу смежности.
     *
     * @param ver добавляемая вершина
     */
    @Override
    public void addVertex(Vertex<T> ver) {
        if (vertexIntegerHashMap.containsKey(ver)) {
            throw new IllegalArgumentException("You try to add an existing vertex");
        }

        // добавляем место под новую вершину
        int[][] bigger = new int[nodes + 1][edges];
        for (int id = 0; id < nodes; id++) {
            System.arraycopy(incMatrix[id], 0, bigger[id], 0, edges);
        }
        incMatrix = bigger;

        vertexIntegerHashMap.put(ver, nodes);
        vertices.add(ver);
        nodes++;
    }

    /**
     * удалить вершину из матрицы смежности.
     *
     * @param ver удалямая вершина
     */
    @Override
    public void delVertex(Vertex<T> ver) {
        Integer removedVertex = vertexIntegerHashMap.get(ver);
        if (removedVertex == null) {
            throw new IllegalArgumentException("You try to delete non-existing vertex");
        }

        // рёбра, которые касались вершины, исчезают вместе с ней
        List<Integer> keptEdgeColumns = new ArrayList<>();
        for (int edgeColumn = 0; edgeColumn < edges; edgeColumn++) {
            if (incMatrix[removedVertex][edgeColumn] == 0) {
                keptEdgeColumns.add(edgeColumn);
            }
        }

        int[][] smallerMatrix = new int[nodes - 1][keptEdgeColumns.size()];
        for (int nod = 0, keeped = 0; nod < nodes; nod++) {

            if (nod == removedVertex) continue; // пропускаем удаляемую вершину
            for (int newColumn = 0; newColumn < keptEdgeColumns.size(); newColumn++) {
                smallerMatrix[keeped][newColumn] = incMatrix[nod][keptEdgeColumns.get(newColumn)];
            }
            keeped++;
        }
        incMatrix = smallerMatrix;
        nodes--;
        edges = keptEdgeColumns.size();

        vertices.remove(removedVertex.intValue());
        vertexIntegerHashMap.remove(ver);

        // обновляем индексы оставшихся
        for (int id = removedVertex; id < vertices.size(); id++) {
            vertexIntegerHashMap.put(vertices.get(id), id);
        }
    }

    /**
     * добавить реюро между source и dest.
     *
     * @param source откуда выходит ребро
     * @param dest куда входит ребро
     */
    @Override
    public void addEdge(Vertex<T> source, Vertex<T> dest) {
        int sourceId = requireIndex(source);
        int destId = requireIndex(dest);
        if (findEdge(sourceId, destId) != -1) return;

        // новый столбец
        int[][] widermatrix = new int[nodes][edges + 1];
        for (int row = 0; row < nodes; row++) {
            System.arraycopy(incMatrix[row], 0, widermatrix[row], 0, edges);
        }
        if (sourceId == destId) {
            widermatrix[sourceId][edges] = 2;
        } else if (directed) {
            widermatrix[sourceId][edges] = 1;
            widermatrix[destId][edges] = -1;
        } else {
            widermatrix[sourceId][edges] = 1;
            widermatrix[destId][edges] = 1;
        }
        incMatrix = widermatrix;
        edges++;
    }

    /**
     * удалить ребро между source и dest.
     *
     * @param source откуда выходило ребро
     * @param dest куда входило ребро
     */
    @Override
    public void delEdge(Vertex<T> source, Vertex<T> dest) {
        int removedEdgeColumn = findEdge(requireIndex(source), requireIndex(dest));
        if (removedEdgeColumn == -1) return;

        int[][] narrowerMatrix = new int[nodes][edges - 1];
        for (int row = 0; row < nodes; row++) {
            System.arraycopy(incMatrix[row], 0, narrowerMatrix[row], 0, removedEdgeColumn);
            System.arraycopy(incMatrix[row], removedEdgeColumn + 1,
                    narrowerMatrix[row], removedEdgeColumn, edges - 1 - removedEdgeColumn);
        }
        incMatrix = narrowerMatrix;
        edges--;
    }

    /**
     * геттер всех вершин графа.
     *
     * @return список из вершин
     */
    @Override
    public List<Vertex<T>> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * геттер смежных вершине ver вершин.
     *
     * @param ver рассматриваемя вершина
     * @return список смежных вершин
     */
    @Override
    public List<Vertex<T>> getAdjacentVertices(Vertex<T> ver) {
        int i = requireIndex(ver);
        List<Vertex<T>> result = new ArrayList<>();
        for (int c = 0; c < edges; c++) {
            int value = incMatrix[i][c];
            if (value == 0) continue;
            if (value == 2) {                          // петля
                result.add(ver);
                continue;
            }
            if (directed && value == -1) continue;     // ребро входит в вершину, а не выходит
            for (int k = 0; k < nodes; k++) {          // второй конец ребра
                if (k != i && incMatrix[k][c] != 0) result.add(vertices.get(k));
            }
        }
        return result;
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

    /**
     * вспоминает тип данных у графа.
     *
     * @return ответ: какой тип данных графа?
     */
    @Override
    public Class<T> getDataType() {
        return this.type;
    }

    /**
     * очистить граф перед чтением из файла.
     *
     * @param directed направленность графа
     */
    @Override
    protected void reset(boolean directed) {
        this.directed = directed;
        vertices.clear();
        vertexIntegerHashMap.clear();
        incMatrix = new int[0][0];
        nodes = 0;
        edges = 0;
    }

    /**
     * копия матрицы инцидентности [вершины][рёбра] (для вывода и тестов).
     *
     * @return матрица инцидентности
     */
    public int[][] toMatrix() {
        int[][] copy = new int[incMatrix.length][];
        for (int i = 0; i < incMatrix.length; i++) {
            copy[i] = incMatrix[i].clone();
        }
        return copy;
    }
}