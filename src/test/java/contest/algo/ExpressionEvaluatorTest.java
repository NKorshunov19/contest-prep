package contest.algo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        // Классическая ошибка наивного парсера: он посчитает 10-(3-2)=9
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
    @DisplayName("некорректный ввод даёт пустой результат, а не исключение наружу")
    void rejectsMalformedInput(String expression) {
        assertThat(ExpressionEvaluator.evaluate(expression)).isEmpty();
    }

    @Test
    @DisplayName("переполнение long ловится, а не заворачивается по кругу")
    void detectsOverflow() {
        // Обычное умножение молча дало бы отрицательное число
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
    @DisplayName("null не роняет вычислитель")
    void nullIsHandled() {
        assertThat(ExpressionEvaluator.evaluate(null)).isEmpty();
    }
}
