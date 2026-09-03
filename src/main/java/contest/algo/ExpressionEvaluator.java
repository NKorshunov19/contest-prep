package contest.algo;

import java.util.Optional;

public final class ExpressionEvaluator {

    private static final int MAX_DEPTH = 1000;

    private final String input;
    private int position;
    private int depth;

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
        if (++depth > MAX_DEPTH) {
            throw new IllegalStateException("слишком глубокая вложенность: " + depth);
        }
        try {
            skipSpaces();
            if (consumeIf('-')) {
                if (position < input.length() && Character.isDigit(input.charAt(position))) {
                    return parseNumber(true);
                }
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
            return parseNumber(false);
        } finally {
            depth--;
        }
    }

    // минус разбираю вместе с цифрами: иначе -9223372036854775808 не влезет в long
    private long parseNumber(boolean negative) {
        skipSpaces();
        int start = position;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }
        if (start == position) {
            throw new IllegalStateException("ожидалось число на позиции " + position);
        }
        String digits = input.substring(start, position);
        try {
            return Long.parseLong(negative ? "-" + digits : digits);
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
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
}
