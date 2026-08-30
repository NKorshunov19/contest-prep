package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DisjointSetUnionTest {

    @Test
    @DisplayName("изначально каждый элемент — своя компонента")
    void startsWithEveryElementSeparate() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        assertThat(dsu.componentCount()).isEqualTo(5);
        assertThat(dsu.connected(0, 1)).isFalse();
    }

    @Test
    @DisplayName("объединение транзитивно: 0-1 и 1-2 делают 0 и 2 связанными")
    void unionIsTransitive() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        dsu.union(0, 1);
        dsu.union(1, 2);

        assertThat(dsu.connected(0, 2)).isTrue();
        assertThat(dsu.componentCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("повторное объединение уже связанных возвращает false и не меняет счётчик")
    void repeatedUnionChangesNothing() {
        DisjointSetUnion dsu = new DisjointSetUnion(3);
        assertThat(dsu.union(0, 1)).isTrue();

        assertThat(dsu.union(0, 1)).isFalse();
        assertThat(dsu.union(1, 0)).isFalse();
        assertThat(dsu.componentCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("длинная цепочка не переполняет стек — find итеративный")
    void longChainDoesNotOverflowStack() {
        int size = 200_000;
        DisjointSetUnion dsu = new DisjointSetUnion(size);
        for (int i = 0; i + 1 < size; i++) {
            dsu.union(i, i + 1);
        }

        assertThat(dsu.componentCount()).isEqualTo(1);
        assertThat(dsu.connected(0, size - 1)).isTrue();
    }
}
