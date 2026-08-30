package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
