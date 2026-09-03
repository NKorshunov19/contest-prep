package contest.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * Быстрое чтение чисел из стандартного потока.
 *
 * Зачем это нужно на контесте: Scanner разбирает вход регулярными выражениями и на
 * 10^5–10^6 числах легко съедает секунду-две из лимита. Этот вариант читает байты
 * блоками по 64 КБ и собирает числа вручную — на порядок быстрее.
 *
 * На маленьком вводе (до пары тысяч чисел) разницы нет, можно брать Scanner.
 * Но если в условии N до 10^5 и больше — берите этот.
 *
 * Исключения как у java.util.Scanner: NoSuchElementException, когда ввод кончился,
 * InputMismatchException, когда прочитанное не похоже на число или не влезает в тип.
 */
public class FastScanner implements AutoCloseable {

    private static final int BUFFER_SIZE = 1 << 16;

    private final InputStream stream;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private int bufferPointer;
    private int bytesRead;
    private boolean eof;

    public FastScanner(InputStream input) {
        this.stream = input;
    }

    public boolean hasNext() throws IOException {
        skipWhitespace();
        return peek() != -1;
    }

    public int nextInt() throws IOException {
        long value = nextLong();
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new InputMismatchException("число " + value + " не влезает в int");
        }
        return (int) value;
    }

    public long nextLong() throws IOException {
        skipWhitespace();
        int b = peek();
        if (b == -1) {
            throw new NoSuchElementException("ввод закончился");
        }
        boolean negative = b == '-';
        if (negative) {
            read();
            b = peek();
        }
        if (b < '0' || b > '9') {
            throw new InputMismatchException("ожидалось число, встретилось '" + (char) b + "'");
        }
        long result = 0;
        try {
            while (b >= '0' && b <= '9') {
                read();
                // копим отрицательное: положительное не вместило бы Long.MIN_VALUE
                result = Math.subtractExact(Math.multiplyExact(result, 10), b - '0');
                b = peek();
            }
            return negative ? result : Math.negateExact(result);
        } catch (ArithmeticException e) {
            throw new InputMismatchException("число не влезает в long");
        }
    }

    public String next() throws IOException {
        skipWhitespace();
        int b = peek();
        if (b == -1) {
            throw new NoSuchElementException("ввод закончился");
        }
        StringBuilder sb = new StringBuilder();
        while (b > ' ') {
            sb.append((char) read());
            b = peek();
        }
        return sb.toString();
    }

    /**
     * Остаток текущей строки, как у java.util.Scanner: сразу после nextInt это пустая
     * строка, потому что число остановилось перед переводом строки, а не за ним.
     */
    public String nextLine() throws IOException {
        int b = read();
        if (b == -1) {
            throw new NoSuchElementException("ввод закончился");
        }
        StringBuilder sb = new StringBuilder();
        while (b != -1 && b != '\n' && b != '\r') {
            sb.append((char) b);
            b = read();
        }
        if (b == '\r' && peek() == '\n') {
            read();
        }
        return sb.toString();
    }

    private void skipWhitespace() throws IOException {
        int b = peek();
        while (b != -1 && b <= ' ') {
            read();
            b = peek();
        }
    }

    private int read() throws IOException {
        if (!fill()) {
            return -1;
        }
        return buffer[bufferPointer++] & 0xFF;
    }

    private int peek() throws IOException {
        if (!fill()) {
            return -1;
        }
        return buffer[bufferPointer] & 0xFF;
    }

    /** false, если ввод кончился. Флаг eof обязателен: без него после -1 буфер читался бы заново. */
    private boolean fill() throws IOException {
        if (eof) {
            return false;
        }
        while (bufferPointer == bytesRead) {
            bytesRead = stream.read(buffer, 0, BUFFER_SIZE);
            bufferPointer = 0;
            if (bytesRead == -1) {
                bytesRead = 0;
                eof = true;
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
