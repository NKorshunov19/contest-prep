package contest.algo;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public final class BinarySearch {

    private static final int DOUBLE_ITERATIONS = 100;

    private BinarySearch() {
    }

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

    public static int lastTrue(int lo, int hi, IntPredicate predicate) {
        requireRange(lo, hi);
        while (lo < hi) {
            int mid = (int) (((long) lo + hi + 1) >> 1);
            if (predicate.test(mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return predicate.test(lo) ? lo : lo - 1;
    }

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
