package maksunova;

import maksunova.graph.algorithms.DfsSort;
import maksunova.graph.common.Graph;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

        // чтение графа из test.txt (лежит в корне проекта) во все три представления
        List<Graph<Object>> graphs = List.of(
                new AdjMatrixGraph<>(false),
                new AdjListGraph<>(false),
                new IncMatrixGraph<>(false));
        // c:\\users
        for (Graph<Object> graph : graphs) {
            try {
                graph.readGraph("test.txt");
                System.out.println(graph);
                System.out.println("  topSort: " + graph.topSort());
                System.out.println("  topSort (поиск в глубину): " + graph.topSort(new DfsSort<>()));
            } catch (IOException e) {
                System.out.println("Не удалось прочитать test.txt: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("  topSort: " + e.getMessage());
            }
        }
    }
}