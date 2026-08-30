import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * ШАБЛОН ДЛЯ КОНТЕСТА. Копировать целиком в окно ответа.
 *
 * Правила Т-Банка, которые ломают людей чаще всего:
 *  - класс называется Main, всё в одном файле, вместе с импортами;
 *  - ввод только из stdin, вывод только в stdout;
 *  - НИКАКИХ приглашений вида "Введите N:" — это считается частью ответа и валит тесты;
 *  - только стандартная библиотека;
 *  - во время экзамена виден вердикт лишь по ПЕРВОМУ тесту, остальные прогонят потом.
 *    Значит, свои краевые случаи надо проверять самому, локально.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // PrintWriter с буфером: печатать в System.out в цикле на 10^5 строк — верный TL
        PrintWriter out = new PrintWriter(System.out);

        StringTokenizer st = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(st.nextToken());

        long sum = 0;
        st = new StringTokenizer(in.readLine());
        for (int i = 0; i < n; i++) {
            sum += Long.parseLong(st.nextToken());
        }

        out.println(sum);
        out.flush();   // без flush вывод может не дойти — частая причина «пустого ответа»
    }
}
