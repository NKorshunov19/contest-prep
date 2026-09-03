package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DisjointSetUnionTest {

    @Test
    @DisplayName("изначально каждый элемент — своя компонента")
    void startsWithEveryElementSeparate() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        assertThat(dsu.componentCount()).isEqualTo(5);
        assertThat(dsu.connected(0, 1)).isFalse();
    }

    @Test
    @DisplayName("объединение транзитивно")
    void unionIsTransitive() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);

        dsu.union(0, 1);
        dsu.union(1, 2);

        assertThat(dsu.connected(0, 2)).isTrue();
        assertThat(dsu.componentCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("повторное объединение уже связанных ничего не меняет")
    void repeatedUnionChangesNothing() {
        DisjointSetUnion dsu = new DisjointSetUnion(3);
        assertThat(dsu.union(0, 1)).isTrue();

        assertThat(dsu.union(0, 1)).isFalse();
        assertThat(dsu.union(1, 0)).isFalse();
        assertThat(dsu.componentCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("длинная цепочка не переполняет стек")
    void longChainDoesNotOverflowStack() {
        int size = 200_000;
        DisjointSetUnion dsu = new DisjointSetUnion(size);
        for (int i = 0; i + 1 < size; i++) {
            dsu.union(i, i + 1);
        }

        assertThat(dsu.componentCount()).isEqualTo(1);
        assertThat(dsu.connected(0, size - 1)).isTrue();
    }

    @Test
    @Timeout(30)
    @DisplayName("find на цепочке из миллиона не падает")
    void findSurvivesMillionElementChain() {
        int size = 1_000_000;
        DisjointSetUnion dsu = new DisjointSetUnion(size);
        for (int i = 0; i + 1 < size; i++) {
            dsu.union(i, i + 1);
        }

        assertThat(dsu.find(0)).isEqualTo(dsu.find(size - 1));
        assertThat(dsu.componentCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("элемент вне диапазона отвергается")
    void rejectsElementOutOfRange() {
        DisjointSetUnion dsu = new DisjointSetUnion(3);

        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> dsu.find(3))
                .withMessageContaining("0..2");
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> dsu.find(-1));
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> dsu.union(0, 5));
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> dsu.connected(0, 5));
    }

    @Test
    @DisplayName("пустая и отрицательная размерность")
    void emptyAndNegativeSize() {
        assertThat(new DisjointSetUnion(0).componentCount()).isZero();
        assertThatIllegalArgumentException().isThrownBy(() -> new DisjointSetUnion(-1));
    }

    @Test
    @DisplayName("элемент связан сам с собой")
    void elementIsConnectedToItself() {
        DisjointSetUnion dsu = new DisjointSetUnion(3);

        assertThat(dsu.connected(1, 1)).isTrue();
        assertThat(dsu.union(1, 1)).isFalse();
        assertThat(dsu.componentCount()).isEqualTo(3);
    }

}
