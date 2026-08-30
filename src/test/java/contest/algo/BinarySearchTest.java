package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BinarySearchTest {

    @Test
    @DisplayName("находит границу ложь→истина")
    void findsFirstTrue() {
        assertThat(BinarySearch.firstTrue(0, 100, x -> x >= 37)).isEqualTo(37);
    }

    @Test
    @DisplayName("если истинно везде, возвращает левую границу")
    void allTrueReturnsLowerBound() {
        assertThat(BinarySearch.firstTrue(5, 100, x -> true)).isEqualTo(5);
    }

    @Test
    @DisplayName("если истины нет, возвращает hi + 1")
    void noTrueReturnsBeyondUpperBound() {
        assertThat(BinarySearch.firstTrue(0, 100, x -> false)).isEqualTo(101);
    }

    @Test
    @DisplayName("находит границу истина→ложь")
    void findsLastTrue() {
        assertThat(BinarySearch.lastTrue(0, 100, x -> x <= 42)).isEqualTo(42);
    }

    @Test
    @DisplayName("не переполняется на границах близких к Integer.MAX_VALUE")
    void doesNotOverflowNearMaxValue() {
        int target = Integer.MAX_VALUE - 10;

        int found = BinarySearch.firstTrue(Integer.MAX_VALUE - 1000, Integer.MAX_VALUE - 1,
                x -> x >= target);

        assertThat(found).isEqualTo(target);
    }

    @Test
    @DisplayName("работает на диапазоне long")
    void worksOnLongRange() {
        long target = 3_000_000_000L;

        assertThat(BinarySearch.firstTrueLong(0L, 4_000_000_000L, x -> x >= target)).isEqualTo(target);
    }
}
