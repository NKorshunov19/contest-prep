package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
    @DisplayName("BFS находит кратчайший путь")
    void bfsFindsShortestNotFirst() {
        List<List<Integer>> graph = Graphs.newGraph(5);
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

    @Test
    @DisplayName("граф без вершин")
    void emptyGraph() {
        List<List<Integer>> graph = Graphs.newGraph(0);

        assertThat(Graphs.countComponents(graph)).isZero();
        assertThatIllegalArgumentException().isThrownBy(() -> Graphs.bfsDistances(graph, 0));
    }

    @Test
    @DisplayName("старт вне графа отвергается")
    void startOutsideGraphIsRejected() {
        List<List<Integer>> graph = Graphs.newGraph(3);

        assertThatIllegalArgumentException().isThrownBy(() -> Graphs.bfsDistances(graph, 3));
        assertThatIllegalArgumentException().isThrownBy(() -> Graphs.bfsDistances(graph, -1));
    }

    @Test
    @DisplayName("петли и кратные рёбра")
    void loopsAndParallelEdgesAreHarmless() {
        List<List<Integer>> graph = Graphs.newGraph(3);
        Graphs.addEdge(graph, 0, 0);
        Graphs.addEdge(graph, 0, 1);
        Graphs.addEdge(graph, 0, 1);
        Graphs.addEdge(graph, 1, 2);

        int[] distance = Graphs.bfsDistances(graph, 0);

        assertThat(distance).containsExactly(0, 1, 2);
        assertThat(Graphs.countComponents(graph)).isEqualTo(1);
    }

    @Test
    @DisplayName("при 1-индексации вершина 0 остаётся лишней компонентой")
    void oneBasedInputLeavesVertexZeroSeparate() {
        List<List<Integer>> graph = Graphs.newGraph(4 + 1);
        Graphs.addEdge(graph, 1, 2);
        Graphs.addEdge(graph, 3, 4);

        assertThat(Graphs.countComponents(graph)).isEqualTo(3);
        assertThat(Graphs.bfsDistances(graph, 1)[2]).isEqualTo(1);
        assertThat(Graphs.bfsDistances(graph, 1)[0]).isEqualTo(-1);
    }

    @Test
    @Timeout(30)
    @DisplayName("BFS на цепочке из миллиона не падает")
    void bfsSurvivesMillionVertexChain() {
        int size = 1_000_000;
        List<List<Integer>> graph = Graphs.newGraph(size);
        for (int i = 0; i + 1 < size; i++) {
            Graphs.addEdge(graph, i, i + 1);
        }

        assertThat(Graphs.bfsDistances(graph, 0)[size - 1]).isEqualTo(size - 1);
        assertThat(Graphs.countComponents(graph)).isEqualTo(1);
    }

}
