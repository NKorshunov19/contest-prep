import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Main {

    private static BufferedReader in;
    private static StringTokenizer tokenizer = new StringTokenizer("");

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));

        int n = nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nextLong();
        }
        out.println(sum);

        // без flush вывод останется в буфере и проверяющий увидит пустой ответ
        out.flush();
    }

    // токены берутся построчно, чтобы не зависеть от того, как разбит ввод
    private static String next() throws IOException {
        while (!tokenizer.hasMoreTokens()) {
            String line = in.readLine();
            if (line == null) {
                throw new NoSuchElementException("ввод закончился");
            }
            tokenizer = new StringTokenizer(line);
        }
        return tokenizer.nextToken();
    }

    private static int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    private static long nextLong() throws IOException {
        return Long.parseLong(next());
    }
}
