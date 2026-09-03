package contest.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FastScannerTest {

    private static FastScanner scanner(String input) {
        return new FastScanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    @DisplayName("читает числа через пробелы и переводы строк")
    void readsNumbersSeparatedByWhitespace() throws IOException {
        FastScanner in = scanner("1 2\n3\t4\r\n5");

        assertThat(in.nextInt()).isEqualTo(1);
        assertThat(in.nextInt()).isEqualTo(2);
        assertThat(in.nextInt()).isEqualTo(3);
        assertThat(in.nextInt()).isEqualTo(4);
        assertThat(in.nextInt()).isEqualTo(5);
    }

    @Test
    @DisplayName("читает отрицательные и ноль")
    void readsNegativeNumbers() throws IOException {
        FastScanner in = scanner("-5 0 -0 -1000000000000");

        assertThat(in.nextInt()).isEqualTo(-5);
        assertThat(in.nextInt()).isZero();
        assertThat(in.nextInt()).isZero();
        assertThat(in.nextLong()).isEqualTo(-1_000_000_000_000L);
    }

    @Test
    @DisplayName("границы long читаются точно")
    void readsLongBoundaries() throws IOException {
        FastScanner in = scanner(Long.MIN_VALUE + " " + Long.MAX_VALUE);

        assertThat(in.nextLong()).isEqualTo(Long.MIN_VALUE);
        assertThat(in.nextLong()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("число за границей long отвергается")
    void rejectsNumberOutOfLongRange() {
        FastScanner in = scanner("9223372036854775808");

        assertThatExceptionOfType(InputMismatchException.class).isThrownBy(in::nextLong);
    }

    @Test
    @DisplayName("число за границей int отвергается")
    void rejectsNumberOutOfIntRange() {
        FastScanner in = scanner("3000000000");

        assertThatExceptionOfType(InputMismatchException.class).isThrownBy(in::nextInt);
    }

    @Test
    @DisplayName("после конца ввода бросает исключение")
    void throwsAfterEndOfInput() throws IOException {
        FastScanner in = scanner("42");
        assertThat(in.nextInt()).isEqualTo(42);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextLong);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextLong);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::next);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextLine);
    }

    @Test
    @DisplayName("пустой ввод — сразу конец")
    void emptyInputHasNothing() throws IOException {
        FastScanner in = scanner("");

        assertThat(in.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextInt);
    }

    @Test
    @DisplayName("ввод из одних пробелов чисел не содержит")
    void whitespaceOnlyInputHasNoTokens() throws IOException {
        FastScanner in = scanner("   \n\n\t  ");

        assertThat(in.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextLong);
    }

    @Test
    @DisplayName("не число вместо числа — исключение")
    void rejectsNonNumericToken() {
        assertThatExceptionOfType(InputMismatchException.class)
                .isThrownBy(() -> scanner("abc").nextLong());
        assertThatExceptionOfType(InputMismatchException.class)
                .isThrownBy(() -> scanner("-x").nextLong());
        assertThatExceptionOfType(InputMismatchException.class)
                .isThrownBy(() -> scanner("- 5").nextLong());
    }

    @Test
    @DisplayName("одинокий минус в конце ввода не зацикливает")
    void loneMinusAtEndDoesNotHang() {
        assertThatExceptionOfType(InputMismatchException.class)
                .isThrownBy(() -> scanner("-").nextLong());
    }

    @Test
    @DisplayName("hasNext не съедает следующий токен")
    void hasNextDoesNotConsumeToken() throws IOException {
        FastScanner in = scanner("  7  ");

        assertThat(in.hasNext()).isTrue();
        assertThat(in.hasNext()).isTrue();
        assertThat(in.nextInt()).isEqualTo(7);
        assertThat(in.hasNext()).isFalse();
    }

    @Test
    @DisplayName("next читает слова")
    void readsWords() throws IOException {
        FastScanner in = scanner(" alpha\tbeta\r\ngamma ");

        assertThat(in.next()).isEqualTo("alpha");
        assertThat(in.next()).isEqualTo("beta");
        assertThat(in.next()).isEqualTo("gamma");
        assertThat(in.hasNext()).isFalse();
    }

    @Test
    @DisplayName("токен длиннее буфера читается целиком")
    void readsTokenLongerThanBuffer() throws IOException {
        String token = "x".repeat(200_000);
        FastScanner in = scanner(token + " 1");

        assertThat(in.next()).hasSize(200_000).isEqualTo(token);
        assertThat(in.nextInt()).isEqualTo(1);
    }

    @Test
    @DisplayName("числа на границе буфера собираются по кускам")
    void readsNumbersAcrossBufferBoundary() throws IOException {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 50_000; i++) {
            input.append(i).append('\n');
        }
        FastScanner in = scanner(input.toString());

        long sum = 0;
        while (in.hasNext()) {
            sum += in.nextInt();
        }

        assertThat(sum).isEqualTo(50_000L * 49_999 / 2);
    }

    @Test
    @DisplayName("nextLine отдаёт остаток текущей строки")
    void nextLineReturnsRestOfCurrentLine() throws IOException {
        FastScanner in = scanner("5 tail of line\nsecond line\n");

        assertThat(in.nextInt()).isEqualTo(5);
        assertThat(in.nextLine()).isEqualTo(" tail of line");
        assertThat(in.nextLine()).isEqualTo("second line");
    }

    @Test
    @DisplayName("сразу после числа в конце строки nextLine пустой")
    void nextLineIsEmptyRightAfterNumber() throws IOException {
        FastScanner in = scanner("5\nsecond line\n");

        assertThat(in.nextInt()).isEqualTo(5);
        assertThat(in.nextLine()).isEmpty();
        assertThat(in.nextLine()).isEqualTo("second line");
    }

    @Test
    @DisplayName("пустые строки сохраняются, \\r\\n не даёт лишней")
    void keepsEmptyLinesAndHandlesCarriageReturn() throws IOException {
        FastScanner in = scanner("first\r\n\r\nthird\r\n");

        assertThat(in.nextLine()).isEqualTo("first");
        assertThat(in.nextLine()).isEmpty();
        assertThat(in.nextLine()).isEqualTo("third");
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(in::nextLine);
    }

    @Test
    @DisplayName("последняя строка без перевода строки читается")
    void readsLastLineWithoutTerminator() throws IOException {
        FastScanner in = scanner("one line");

        assertThat(in.nextLine()).isEqualTo("one line");
    }

    @Test
    @DisplayName("миллион чисел читается быстро")
    void readsMillionNumbersQuickly() throws IOException {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 1_000_000; i++) {
            input.append(1_000_000_000).append(' ');
        }
        FastScanner in = scanner(input.toString());

        long sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += in.nextInt();
        }

        assertThat(sum).isEqualTo(1_000_000L * 1_000_000_000L);
    }
}
