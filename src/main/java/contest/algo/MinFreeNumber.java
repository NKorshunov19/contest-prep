package contest.algo;

import java.util.NavigableSet;
import java.util.TreeSet;

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
 * TreeSet, а не куча: он и отдаёт минимум за O(log n), и сам ловит повторное
 * освобождение одного номера — куча пропустила бы дубликат и выдала номер дважды.
 */
public class MinFreeNumber {

    private final NavigableSet<Integer> released = new TreeSet<>();
    private final int firstNumber;
    private int nextFresh;

    public MinFreeNumber(int firstNumber) {
        this.firstNumber = firstNumber;
        this.nextFresh = firstNumber;
    }

    /** Занимает и возвращает наименьший свободный номер. */
    public int acquire() {
        Integer reused = released.pollFirst();
        return reused != null ? reused : nextFresh++;
    }

    /**
     * Возвращает номер в пул. Номер, который не выдавался или уже свободен, — ошибка
     * в вызывающем коде: тихо проглотив её, мы выдали бы один номер двум владельцам.
     */
    public void release(int number) {
        if (number < firstNumber || number >= nextFresh) {
            throw new IllegalArgumentException("номер " + number + " не выдавался");
        }
        if (!released.add(number)) {
            throw new IllegalStateException("номер " + number + " уже свободен");
        }
    }
}
