package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.within;

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

    @Test
    @DisplayName("работает на всём диапазоне int")
    void worksOnWholeIntRange() {
        assertThat(BinarySearch.firstTrue(Integer.MIN_VALUE, Integer.MAX_VALUE, x -> x >= 0)).isZero();
        assertThat(BinarySearch.lastTrue(Integer.MIN_VALUE, Integer.MAX_VALUE, x -> x < 0)).isEqualTo(-1);
    }

    @Test
    @Timeout(10)
    @DisplayName("работает на всём диапазоне long")
    void worksOnWholeLongRange() {
        assertThat(BinarySearch.firstTrueLong(Long.MIN_VALUE, Long.MAX_VALUE, x -> x >= 0)).isZero();
        assertThat(BinarySearch.firstTrueLong(Long.MIN_VALUE, Long.MAX_VALUE, x -> x >= Long.MAX_VALUE))
                .isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("отрезок из одной точки")
    void singlePointRange() {
        assertThat(BinarySearch.firstTrue(7, 7, x -> true)).isEqualTo(7);
        assertThat(BinarySearch.firstTrue(7, 7, x -> false)).isEqualTo(8);
        assertThat(BinarySearch.lastTrue(7, 7, x -> true)).isEqualTo(7);
        assertThat(BinarySearch.lastTrue(7, 7, x -> false)).isEqualTo(6);
    }

    @Test
    @DisplayName("lastTrue: если истины нет, возвращает lo - 1")
    void lastTrueWithoutAnyTrue() {
        assertThat(BinarySearch.lastTrue(10, 20, x -> false)).isEqualTo(9);
        assertThat(BinarySearch.lastTrue(10, 20, x -> true)).isEqualTo(20);
    }

    @Test
    @DisplayName("перевёрнутый диапазон отвергается, а не отдаёт молча lo")
    void reversedRangeIsRejected() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BinarySearch.firstTrue(5, 3, x -> true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BinarySearch.lastTrue(5, 3, x -> true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BinarySearch.firstTrueLong(5L, 3L, x -> true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BinarySearch.firstTrueDouble(5.0, 3.0, x -> true));
    }

    @Test
    @DisplayName("вещественный поиск: корень из двух")
    void findsSquareRoot() {
        double root = BinarySearch.firstTrueDouble(0, 2, x -> x * x >= 2);

        assertThat(root).isCloseTo(Math.sqrt(2), within(1e-9));
    }

    @Test
    @Timeout(10)
    @DisplayName("вещественный поиск на широком отрезке не зависает")
    void doubleSearchTerminatesOnWideRange() {
        double root = BinarySearch.firstTrueDouble(0, 1e18, x -> x * x >= 1e18);

        assertThat(root).isCloseTo(1e9, within(1.0));
    }

}
