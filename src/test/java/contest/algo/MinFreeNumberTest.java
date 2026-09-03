package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MinFreeNumberTest {

    @Test
    @DisplayName("без освобождений номера идут подряд")
    void issuesConsecutiveNumbers() {
        MinFreeNumber pool = new MinFreeNumber(1);

        assertThat(pool.acquire()).isEqualTo(1);
        assertThat(pool.acquire()).isEqualTo(2);
        assertThat(pool.acquire()).isEqualTo(3);
    }

    @Test
    @DisplayName("освобождённый номер выдаётся раньше нового")
    void reusesReleasedNumberFirst() {
        MinFreeNumber pool = new MinFreeNumber(1);
        pool.acquire();
        pool.acquire();
        pool.acquire();

        pool.release(2);

        assertThat(pool.acquire()).isEqualTo(2);
        assertThat(pool.acquire()).isEqualTo(4);
    }

    @Test
    @DisplayName("из нескольких освобождённых берётся наименьший")
    void takesSmallestReleased() {
        MinFreeNumber pool = new MinFreeNumber(1);
        for (int i = 0; i < 5; i++) {
            pool.acquire();
        }

        pool.release(4);
        pool.release(1);
        pool.release(3);

        assertThat(pool.acquire()).isEqualTo(1);
        assertThat(pool.acquire()).isEqualTo(3);
        assertThat(pool.acquire()).isEqualTo(4);
        assertThat(pool.acquire()).isEqualTo(6);
    }

    @Test
    @DisplayName("нумерация может начинаться с нуля")
    void supportsZeroBasedNumbering() {
        MinFreeNumber pool = new MinFreeNumber(0);

        assertThat(pool.acquire()).isZero();
    }

    @Test
    @DisplayName("освобождение невыданного номера отвергается")
    void rejectsReleaseOfUnissuedNumber() {
        MinFreeNumber pool = new MinFreeNumber(1);
        pool.acquire();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> pool.release(2))
                .withMessageContaining("не выдавался");
        assertThatIllegalArgumentException().isThrownBy(() -> pool.release(0));
    }

    @Test
    @DisplayName("повторное освобождение отвергается")
    void rejectsDoubleRelease() {
        MinFreeNumber pool = new MinFreeNumber(1);
        pool.acquire();
        pool.acquire();
        pool.release(1);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> pool.release(1));
        assertThat(pool.acquire()).isEqualTo(1);
        assertThat(pool.acquire()).isEqualTo(3);
    }

    @Test
    @DisplayName("номер можно освободить и занять снова")
    void numberCanBeReleasedAgainAfterReacquire() {
        MinFreeNumber pool = new MinFreeNumber(1);
        pool.acquire();
        pool.release(1);
        assertThat(pool.acquire()).isEqualTo(1);

        pool.release(1);

        assertThat(pool.acquire()).isEqualTo(1);
    }

    @Test
    @Timeout(30)
    @DisplayName("миллион операций укладывается по времени")
    void handlesMillionOperations() {
        int count = 1_000_000;
        MinFreeNumber pool = new MinFreeNumber(1);
        for (int i = 0; i < count; i++) {
            pool.acquire();
        }
        for (int i = count; i >= 1; i--) {
            pool.release(i);
        }

        assertThat(pool.acquire()).isEqualTo(1);
        assertThat(pool.acquire()).isEqualTo(2);
    }

}
