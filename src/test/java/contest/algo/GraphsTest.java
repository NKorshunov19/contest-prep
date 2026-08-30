package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GraphsTest {

    @Test
    @DisplayName("BFS считает расстояния в рёбрах")
    void bfsComputesDistances() {
        List<List<Integer>> graph = Graphs.newGraph(6);
        Graphs.addEdge(graph, 0, 1);
        Graphs.addEdge(graph, 1, 2);
        Graphs.addEdge(graph, 2, 3);
        Graphs.addEdge(graph, 0, 4);

        int[] distance = Graphs.bfsDistances(graph, 0);

        assertThat(distance[0]).isZero();
        assertThat(distance[1]).isEqualTo(1);
        assertThat(distance[3]).isEqualTo(3);
        assertThat(distance[4]).isEqualTo(1);
        assertThat(distance[5]).as("вершина 5 недостижима").isEqualTo(-1);
    }

    @Test
    @DisplayName("BFS находит кратчайший путь, а не первый попавшийся")
    void bfsFindsShortestNotFirst() {
        List<List<Integer>> graph = Graphs.newGraph(5);
        // Длинный обход 0-1-2-3-4 и короткая перемычка 0-4
        Graphs.addEdge(graph, 0, 1);
        Graphs.addEdge(graph, 1, 2);
        Graphs.addEdge(graph, 2, 3);
        Graphs.addEdge(graph, 3, 4);
        Graphs.addEdge(graph, 0, 4);

        assertThat(Graphs.bfsDistances(graph, 0)[4]).isEqualTo(1);
    }

    @Test
    @DisplayName("считает компоненты связности")
    void countsConnectedComponents() {
        List<List<Integer>> graph = Graphs.newGraph(7);
        Graphs.addEdge(graph, 0, 1);
        Graphs.addEdge(graph, 1, 2);
        Graphs.addEdge(graph, 3, 4);
        // вершины 5 и 6 изолированы

        assertThat(Graphs.countComponents(graph)).isEqualTo(4);
    }

    @Test
    @DisplayName("DFS на длинной цепочке не переполняет стек")
    void dfsHandlesLongChain() {
        int size = 200_000;
        List<List<Integer>> graph = Graphs.newGraph(size);
        for (int i = 0; i + 1 < size; i++) {
            Graphs.addEdge(graph, i, i + 1);
        }

        assertThat(Graphs.countComponents(graph)).isEqualTo(1);
    }
}
