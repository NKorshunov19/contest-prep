package contest.algo;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Выдача наименьшего свободного номера с возвратом — «полки», «столики», «пул id».
 *
 * Тоже задача с их экзамена: приходят события «занять» и «освободить», надо каждый раз
 * отдавать минимальный доступный номер.
 *
 * Идея: держим счётчик «сколько номеров вообще раздавали» и кучу освобождённых.
 * Если в куче что-то есть — берём минимум оттуда, иначе выдаём следующий новый.
 * Это O(log n) на операцию против O(n) у наивного перебора «ищу первый свободный».
 *
 * Куча, а не TreeSet: номера в куче не повторяются по построению, а PriorityQueue
 * экономнее по памяти и константе. Если бы нужны были ещё и запросы «есть ли номер X»
 * или «следующий больший», тогда брали бы TreeSet.
 */
public class MinFreeNumber {

    private final Queue<Integer> released = new PriorityQueue<>();
    private int nextFresh;

    public MinFreeNumber(int firstNumber) {
        this.nextFresh = firstNumber;
    }

    /** Занимает и возвращает наименьший свободный номер. */
    public int acquire() {
        Integer reused = released.poll();
        return reused != null ? reused : nextFresh++;
    }

    /**
     * Возвращает номер в пул.
     * Повторное освобождение того же номера здесь не проверяется — на контесте
     * этого обычно не требуют, но в проде это была бы дыра.
     */
    public void release(int number) {
        released.add(number);
    }
}
