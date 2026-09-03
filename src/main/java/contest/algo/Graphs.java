package contest.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Обходы графа. Обе реализации итеративные — на контесте граф может быть цепочкой
 * из 10^5 вершин, и рекурсивный DFS упадёт со StackOverflowError.
 *
 * Граф задаётся списками смежности: List&lt;List&lt;Integer&gt;&gt;.
 * Матрица смежности на 10^5 вершин потребовала бы 10^10 ячеек — не вариант.
 */
public final class Graphs {

    private Graphs() {
    }

    public static List<List<Integer>> newGraph(int vertexCount) {
        List<List<Integer>> graph = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    public static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    /**
     * Кратчайшие расстояния в рёбрах от стартовой вершины (поиск в ширину).
     * Работает только для невзвешенного графа: если у рёбер есть веса, нужен Дейкстра.
     * Недостижимые вершины остаются с -1.
     */
    public static int[] bfsDistances(List<List<Integer>> graph, int start) {
        if (start < 0 || start >= graph.size()) {
            throw new IllegalArgumentException(
                    "вершина " + start + " вне графа из " + graph.size() + " вершин");
        }
        int[] distance = new int[graph.size()];
        Arrays.fill(distance, -1);
        distance[start] = 0;

        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int next : graph.get(current)) {
                if (distance[next] == -1) {
                    distance[next] = distance[current] + 1;
                    queue.add(next);
                }
            }
        }
        return distance;
    }

    /**
     * Номер компоненты связности для каждой вершины и их общее количество.
     * Обход в глубину на явном стеке.
     */
    public static int[] connectedComponents(List<List<Integer>> graph) {
        int[] component = new int[graph.size()];
        Arrays.fill(component, -1);
        int componentId = 0;

        Deque<Integer> stack = new ArrayDeque<>();
        for (int start = 0; start < graph.size(); start++) {
            if (component[start] != -1) {
                continue;
            }
            stack.push(start);
            component[start] = componentId;
            while (!stack.isEmpty()) {
                int current = stack.pop();
                for (int next : graph.get(current)) {
                    if (component[next] == -1) {
                        component[next] = componentId;
                        stack.push(next);
                    }
                }
            }
            componentId++;
        }
        return component;
    }

    public static int countComponents(List<List<Integer>> graph) {
        int[] component = connectedComponents(graph);
        int max = -1;
        for (int id : component) {
            max = Math.max(max, id);
        }
        return max + 1;
    }
}
