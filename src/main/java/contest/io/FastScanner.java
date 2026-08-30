package contest.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Быстрое чтение чисел из стандартного потока.
 *
 * Зачем это нужно на контесте: Scanner разбирает вход регулярными выражениями и на
 * 10^5–10^6 числах легко съедает секунду-две из лимита. Этот вариант читает байты
 * блоками по 64 КБ и собирает числа вручную — на порядок быстрее.
 *
 * На маленьком вводе (до пары тысяч чисел) разницы нет, можно брать Scanner.
 * Но если в условии N до 10^5 и больше — берите этот.
 */
public class FastScanner implements AutoCloseable {

    private static final int BUFFER_SIZE = 1 << 16;

    private final DataInputStream stream;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private int bufferPointer;
    private int bytesRead;

    public FastScanner(InputStream input) {
        this.stream = new DataInputStream(input);
    }

    public int nextInt() throws IOException {
        return (int) nextLong();
    }

    public long nextLong() throws IOException {
        int b = skipWhitespace();
        boolean negative = b == '-';
        if (negative) {
            b = read();
        }
        long result = 0;
        while (b >= '0' && b <= '9') {
            result = result * 10 + (b - '0');
            b = read();
        }
        return negative ? -result : result;
    }

    public String next() throws IOException {
        int b = skipWhitespace();
        StringBuilder sb = new StringBuilder();
        while (b > ' ') {
            sb.append((char) b);
            b = read();
        }
        return sb.toString();
    }

    /** Читает строку целиком, включая пробелы. Нужно, например, для разбора выражений. */
    public String nextLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int b = read();
        while (b == '\n' || b == '\r') {
            b = read();
        }
        while (b != -1 && b != '\n' && b != '\r') {
            sb.append((char) b);
            b = read();
        }
        return sb.toString();
    }

    private int skipWhitespace() throws IOException {
        int b = read();
        while (b != -1 && b <= ' ') {
            b = read();
        }
        return b;
    }

    private int read() throws IOException {
        if (bufferPointer == bytesRead) {
            bytesRead = stream.read(buffer, 0, BUFFER_SIZE);
            bufferPointer = 0;
            if (bytesRead == -1) {
                return -1;
            }
        }
        return buffer[bufferPointer++];
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
