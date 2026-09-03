package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorTest {

    @ParameterizedTest(name = "{0} = {1}")
    @CsvSource({
            "'2+2', 4",
            "'2+2*2', 6",
            "'(2+2)*2', 8",
            "'2*(3+4)*5', 70",
            "'10-3-2', 5",
            "'-5', -5",
            "'-(2+3)', -5",
            "'2*-3', -6",
            "'((((7))))', 7",
            "'0', 0",
            "'1000000000*1000000000', 1000000000000000000"
    })
    @DisplayName("считает корректные выражения")
    void evaluatesValidExpressions(String expression, long expected) {
        assertThat(ExpressionEvaluator.evaluate(expression)).contains(expected);
    }

    @Test
    @DisplayName("вычитание слева направо, а не справа налево")
    void subtractionIsLeftAssociative() {
        assertThat(ExpressionEvaluator.evaluate("10-3-2")).contains(5L);
    }

    @Test
    @DisplayName("пробелы игнорируются")
    void spacesAreIgnored() {
        assertThat(ExpressionEvaluator.evaluate("  2 +  3 * ( 4 - 1 ) ")).contains(11L);
    }

    @ParameterizedTest(name = "\"{0}\" отвергается")
    @ValueSource(strings = {
            "2+", "+", "", "2++2", "(2+3", "2+3)", "()", "2 3", "abc", "2/3", "((2)"
    })
    @DisplayName("некорректный ввод даёт пустой результат")
    void rejectsMalformedInput(String expression) {
        assertThat(ExpressionEvaluator.evaluate(expression)).isEmpty();
    }

    @Test
    @DisplayName("переполнение long ловится")
    void detectsOverflow() {
        assertThat(ExpressionEvaluator.evaluate("9223372036854775807*2")).isEmpty();
        assertThat(ExpressionEvaluator.evaluate("9223372036854775807+1")).isEmpty();
        assertThat(ExpressionEvaluator.evaluate("99999999999999999999")).isEmpty();
    }

    @Test
    @DisplayName("граничное значение long принимается")
    void acceptsLongBoundary() {
        assertThat(ExpressionEvaluator.evaluate("9223372036854775807")).contains(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("null допустим")
    void nullIsHandled() {
        assertThat(ExpressionEvaluator.evaluate(null)).isEmpty();
    }

    @ParameterizedTest(name = "{0} = {1}")
    @CsvSource({
            "'--3', 3",
            "'---3', -3",
            "'-(1+2)', -3",
            "'2*-(3+1)', -8",
            "'-2*-2', 4",
            "'10--5', 15"
    })
    @DisplayName("унарный минус можно ставить подряд")
    void handlesRepeatedUnaryMinus(String expression, long expected) {
        assertThat(ExpressionEvaluator.evaluate(expression)).contains(expected);
    }

    @Test
    @DisplayName("табуляции и переводы строк считаются пробелами")
    void tabsAndNewlinesAreWhitespace() {
        assertThat(ExpressionEvaluator.evaluate("2\t+\n3 * 4")).contains(14L);
    }

    @Test
    @DisplayName("умеренная вложенность скобок считается")
    void moderateNestingIsEvaluated() {
        String expression = "(".repeat(300) + "1+1" + ")".repeat(300);

        assertThat(ExpressionEvaluator.evaluate(expression)).contains(2L);
    }

    @Test
    @Timeout(10)
    @DisplayName("слишком глубокая вложенность отвергается")
    void tooDeepNestingIsRejected() {
        String parentheses = "(".repeat(100_000) + "1" + ")".repeat(100_000);
        String minuses = "-".repeat(100_000) + "1";

        assertThat(ExpressionEvaluator.evaluate(parentheses)).isEmpty();
        assertThat(ExpressionEvaluator.evaluate(minuses)).isEmpty();
    }

    @Test
    @DisplayName("Long.MIN_VALUE получается через унарный минус")
    void reachesLongMinValue() {
        assertThat(ExpressionEvaluator.evaluate("-9223372036854775808")).contains(Long.MIN_VALUE);
    }

}
