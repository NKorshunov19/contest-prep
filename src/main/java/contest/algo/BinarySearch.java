package contest.algo;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

/**
 * Бинарный поиск по ответу.
 *
 * Приём применим, когда ответ — число, и есть проверка «подходит ли значение X»,
 * причём проверка монотонна: если X подходит, то и все большие подходят (или наоборот).
 * Тогда вместо перебора всех значений мы ищем границу за O(log(диапазон) * стоимость проверки).
 *
 * Типичные формулировки: «минимальное время, за которое успеем», «максимальная длина,
 * при которой хватит», «минимальный уровень воды, при котором граф развалится».
 *
 * Главная ловушка — переполнение в mid = (lo + hi) / 2 при больших границах.
 * Здесь используется lo + (hi - lo) / 2, который не переполняется.
 */
public final class BinarySearch {

    private BinarySearch() {
    }

    /**
     * Наименьшее значение в [lo, hi], для которого предикат истинен.
     * Предполагается, что предикат имеет вид ложь-ложь-...-истина-истина.
     * Если истины нет нигде, вернётся hi + 1.
     */
    public static int firstTrue(int lo, int hi, IntPredicate predicate) {
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return predicate.test(lo) ? lo : lo + 1;
    }

    /** Наибольшее значение в [lo, hi], для которого предикат истинен (истина-...-ложь). */
    public static int lastTrue(int lo, int hi, IntPredicate predicate) {
        while (lo < hi) {
            // Сдвиг вверх обязателен, иначе на lo = hi - 1 цикл зациклится.
            int mid = lo + (hi - lo + 1) / 2;
            if (predicate.test(mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return predicate.test(lo) ? lo : lo - 1;
    }

    /**
     * То же для диапазона long. Имя отличается намеренно: при одинаковом имени
     * вызов firstTrue(0, 100, x -> ...) неоднозначен — int-литералы расширяются
     * до long, и лямбда подходит под оба варианта, компилятор не может выбрать.
     */
    public static long firstTrueLong(long lo, long hi, LongPredicate predicate) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return predicate.test(lo) ? lo : lo + 1;
    }
}
