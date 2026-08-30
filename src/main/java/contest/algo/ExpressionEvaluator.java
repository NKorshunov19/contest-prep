package contest.algo;

import java.util.Optional;

/**
 * Калькулятор целочисленных выражений со скобками: + - * ( ) и унарный минус.
 *
 * Это задача с реального вступительного экзамена Т-Банка. Разбор — методом
 * рекурсивного спуска: грамматика записывается напрямую в виде трёх методов,
 * и приоритет операций получается сам собой из того, кто кого вызывает.
 *
 *   выражение := слагаемое (('+' | '-') слагаемое)*
 *   слагаемое := множитель ('*' множитель)*
 *   множитель := число | '(' выражение ')' | '-' множитель
 *
 * Умножение «сильнее» сложения именно потому, что слагаемое состоит из множителей,
 * а не наоборот. Никаких таблиц приоритетов не нужно.
 *
 * Переполнение ловится через Math.addExact / multiplyExact: они бросают
 * ArithmeticException вместо тихого заворачивания по кругу, как обычные + и *.
 * Возвращаем Optional.empty() на любой некорректный вход — вызывающий печатает WRONG.
 */
public final class ExpressionEvaluator {

    private final String input;
    private int position;

    private ExpressionEvaluator(String input) {
        this.input = input;
    }

    public static Optional<Long> evaluate(String expression) {
        if (expression == null) {
            return Optional.empty();
        }
        ExpressionEvaluator parser = new ExpressionEvaluator(expression);
        try {
            long value = parser.parseExpression();
            parser.skipSpaces();
            // Разобрали префикс, но что-то осталось — например "2+2)" — это ошибка.
            if (parser.position != expression.length()) {
                return Optional.empty();
            }
            return Optional.of(value);
        } catch (ArithmeticException | IllegalStateException e) {
            return Optional.empty();
        }
    }

    private long parseExpression() {
        long result = parseTerm();
        while (true) {
            skipSpaces();
            if (consumeIf('+')) {
                result = Math.addExact(result, parseTerm());
            } else if (consumeIf('-')) {
                result = Math.subtractExact(result, parseTerm());
            } else {
                return result;
            }
        }
    }

    private long parseTerm() {
        long result = parseFactor();
        while (true) {
            skipSpaces();
            if (consumeIf('*')) {
                result = Math.multiplyExact(result, parseFactor());
            } else {
                return result;
            }
        }
    }

    private long parseFactor() {
        skipSpaces();
        if (consumeIf('-')) {
            return Math.negateExact(parseFactor());
        }
        if (consumeIf('(')) {
            long inner = parseExpression();
            skipSpaces();
            if (!consumeIf(')')) {
                throw new IllegalStateException("нет закрывающей скобки");
            }
            return inner;
        }
        return parseNumber();
    }

    private long parseNumber() {
        skipSpaces();
        int start = position;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }
        if (start == position) {
            throw new IllegalStateException("ожидалось число на позиции " + position);
        }
        try {
            // parseLong сам бросит исключение, если число не влезает в 64 бита.
            return Long.parseLong(input, start, position, 10);
        } catch (NumberFormatException e) {
            throw new ArithmeticException("число вне диапазона long");
        }
    }

    private boolean consumeIf(char expected) {
        skipSpaces();
        if (position < input.length() && input.charAt(position) == expected) {
            position++;
            return true;
        }
        return false;
    }

    private void skipSpaces() {
        while (position < input.length() && input.charAt(position) == ' ') {
            position++;
        }
    }
}
