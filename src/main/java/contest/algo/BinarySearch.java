package contest.algo;

import java.util.function.DoublePredicate;
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

    private static final int DOUBLE_ITERATIONS = 100;

    private BinarySearch() {
    }

    /**
     * Наименьшее значение в [lo, hi], для которого предикат истинен.
     * Предполагается, что предикат имеет вид ложь-ложь-...-истина-истина.
     * Если истины нет нигде, вернётся hi + 1.
     */
    public static int firstTrue(int lo, int hi, IntPredicate predicate) {
        requireRange(lo, hi);
        while (lo < hi) {
            // считаю в long: lo + hi на границах int переполнилось бы
            int mid = (int) (((long) lo + hi) >> 1);
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
        requireRange(lo, hi);
        while (lo < hi) {
            // Сдвиг вверх обязателен, иначе на lo = hi - 1 цикл зациклится.
            int mid = (int) (((long) lo + hi + 1) >> 1);
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
        requireRange(lo, hi);
        while (lo < hi) {
            // hi - lo может уйти в минус, но как беззнаковое это всё ещё верная разность
            long mid = lo + ((hi - lo) >>> 1);
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return predicate.test(lo) ? lo : lo + 1;
    }

    /**
     * Граница ложь→истина на вещественном отрезке. Фиксированные 100 итераций вместо
     * условия hi - lo > eps: с eps цикл легко становится бесконечным, когда до соседнего
     * представимого double уже ближе, чем заказанная точность.
     */
    public static double firstTrueDouble(double lo, double hi, DoublePredicate predicate) {
        if (lo > hi) {
            throw new IllegalArgumentException("пустой отрезок: lo=" + lo + " > hi=" + hi);
        }
        for (int i = 0; i < DOUBLE_ITERATIONS; i++) {
            double mid = lo + (hi - lo) / 2;
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid;
            }
        }
        return hi;
    }

    private static void requireRange(long lo, long hi) {
        if (lo > hi) {
            throw new IllegalArgumentException("пустой диапазон: lo=" + lo + " > hi=" + hi);
        }
    }
}
