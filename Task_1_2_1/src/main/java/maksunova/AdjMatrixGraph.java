package maksunova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maksunova.graph.common.AbstractGraph;
import maksunova.graph.parts.Vertex;

/**
 * граф представленный матрицей смежности.
 *
 * @param <T> дженерик для типа данных, хранящихся в графе
 */
public class AdjMatrixGraph<T> extends AbstractGraph<T> {

    private boolean directed;
    private Class<T> type = null;
    private int[][] matrix = new int[0][0];
    private final List<Vertex<T>> vertices = new ArrayList<>();
    private final Map<Vertex<T>, Integer> vertexIntegerHashMap = new HashMap<>();

    public AdjMatrixGraph(boolean directed) {
        this.directed = directed;
    }

    public AdjMatrixGraph(boolean directed, Class<T> type) {
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
            throw new IllegalArgumentException("вершины нет в графе: " + ver);
        }
        return idx;
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

        int size = vertices.size();
        int[][] bigger = new int[size + 1][size + 1];

        for (int i = 0; i < size; i++) {
            System.arraycopy(matrix[i], 0, bigger[i], 0, size);
        }
        matrix = bigger;

        vertexIntegerHashMap.put(ver, size);
        vertices.add(ver);
    }

    /**
     * удалить вершину из матрицы смежности.
     *
     * @param ver удалямая вершина
     */
    @Override
    public void delVertex(Vertex<T> ver) {
        Integer idx = vertexIntegerHashMap.get(ver);
        if (idx == null) {
            throw new IllegalArgumentException("You try to delete non-existing vertex");
        }

        int n = vertices.size();
        int[][] smaller = new int[n - 1][n - 1];

        for (int i = 0, ni = 0; i < n; i++) {
            if (i == idx) {
                continue; // пропускаем удаляемую вершину
            }

            // копируем левую часть
            System.arraycopy(matrix[i], 0, smaller[ni], 0, idx);

            // копируем правую часть
            System.arraycopy(matrix[i], idx + 1, smaller[ni], idx, n - 1 - idx);

            ni++;
        }
        matrix = smaller;

        vertices.remove(idx.intValue());
        vertexIntegerHashMap.remove(ver);

        // обновляем индексы оставшихся
        for (int i = idx; i < vertices.size(); i++) {
            vertexIntegerHashMap.put(vertices.get(i), i);
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
        int i = requireIndex(source);
        int j = requireIndex(dest);
        matrix[i][j] = 1;
        if (!directed) {
            matrix[j][i] = 1;
        }
    }

    /**
     * удалить ребро между source и dest.
     *
     * @param source откуда выходило ребро
     * @param dest куда входило ребро
     */
    @Override
    public void delEdge(Vertex<T> source, Vertex<T> dest) {
        int i = requireIndex(source);
        int j = requireIndex(dest);
        matrix[i][j] = 0;
        if (!directed) {
            matrix[j][i] = 0;
        }
    }

    /**
     * геттер всех вершин графа.
     *
     * @return список из вершин
     */
    @Override
    public List<Vertex<T>> getVertices() {
        return new ArrayList<>(vertices); // copy for thievies)
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
        for (int j = 0; j < matrix.length; j++) {
            if (matrix[i][j] != 0) {
                result.add(vertices.get(j));
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
     * сбросить все параметры графа.
     *
     * @param directed направленность графа
     */
    @Override
    protected void reset(boolean directed) {
        this.directed = directed;
        vertices.clear();
        vertexIntegerHashMap.clear();
        matrix = new int[0][0];
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
     * копия матрицы смежности (для вывода и тестов).
     *
     * @return матрица смежности
     */
    public int[][] toMatrix() {
        return matrix;
    }

}
